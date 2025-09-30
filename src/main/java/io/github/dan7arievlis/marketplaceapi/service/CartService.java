package io.github.dan7arievlis.marketplaceapi.service;

import io.github.dan7arievlis.marketplaceapi.controller.dto.cart.CartItemUpdateRequestDTO;
import io.github.dan7arievlis.marketplaceapi.controller.dto.cart.CartUpdateRequestDTO;
import io.github.dan7arievlis.marketplaceapi.controller.dto.cart.PaymentCreateRequestDTO;
import io.github.dan7arievlis.marketplaceapi.exceptions.OperationNotAllowedException;
import io.github.dan7arievlis.marketplaceapi.model.*;
import io.github.dan7arievlis.marketplaceapi.model.enums.CartStatus;
import io.github.dan7arievlis.marketplaceapi.model.enums.ItemStatus;
import io.github.dan7arievlis.marketplaceapi.repository.CartRepository;
import io.github.dan7arievlis.marketplaceapi.security.SecurityService;
import io.github.dan7arievlis.marketplaceapi.service.component.OptionalSpecificationSearch;
import io.github.dan7arievlis.marketplaceapi.validator.CartValidator;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.util.ReflectionUtils;

import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.*;

@Service
@RequiredArgsConstructor
public class CartService implements OptionalSpecificationSearch {
    private final CartRepository repository;
    private final CartValidator validator;
    private final SecurityService securityService;
    private final ProductService productService;
    private final CartItemService cartItemService;
    private final PaymentService paymentService;
    private final UserService userService;
    private final ProviderService providerService;

    public Optional<Cart> findById(UUID id) {
        return repository.findById(id);
    }

    @Transactional
    public void save(Cart cart) {
        validator.validate(cart);
        manageStatus(cart);
        cart.setUser(securityService.getLoggedUser());
        repository.save(cart);
    }

    @Transactional
    public void create(Cart cart) {
        restore(cart);
        save(cart);
    }

    @Transactional
    public void updateStatus(Cart cart, CartUpdateRequestDTO request) {
        var status = CartStatus.valueOf(request.status());
        if (!Set.of(CartStatus.OPEN, CartStatus.CANCELLED).contains(status))
            return;
        cart.setStatus(status);

        save(cart);
    }

    @Transactional
    public void update(Cart cart) {
        if (cart.getId() == null)
            throw new IllegalArgumentException("Is necessary to have a saved cart in db to update it");

        save(cart);
    }

    @Transactional
    public void manageStatus(Cart cart) {
        if (cart.getTotal().compareTo(BigDecimal.ZERO) > 0)
            cart.setStatus(CartStatus.WAITING_PAYMENT);

        if (cart.getPaidAmount().compareTo(cart.getTotal()) == 0) {
            cart.setStatus(CartStatus.PAID);
        }

        switch (cart.getStatus()) {
            case CANCELLED:
                cart.getItems().stream()
                        .filter(item -> cart.getPurchaseMoment().isBefore(item.getUpdatedAt()))
                        .filter(item -> item.getItemStatus().equals(ItemStatus.ALLOCATED))
                        .forEach(item -> {
                            item.setItemStatus(ItemStatus.OPEN);
                            cart.getOwner().refund(item.getSubtotal());
                            //TODO tirar dinheiro do vendedor
                            userService.update(cart.getOwner());
                            cartItemService.update(item);
                        });
                break;
            case PAID:
                // TODO check de stock
                cart.getItems().forEach(item -> {
                    item.setItemStatus(ItemStatus.ALLOCATED);
                    cartItemService.update(item);
                    cart.setPurchaseMoment(LocalDateTime.now());
                });
                break;
        }
    }

    @Transactional
    public void delete(Cart cart) {
        cart.delete();
        save(cart);
    }

    @Transactional
    public void restore(Cart cart) {
        cart.restore();
        save(cart);
    }

    @Transactional
    public void addPayment(User user, Payment payment, PaymentCreateRequestDTO request) {
        Cart cart = user.getCart();
        payment.setProvider(providerService.findById(request.providerId())
                .orElseThrow(() -> new EntityNotFoundException("Provider not found.")));
        cart.addPayment(payment);
        paymentService.create(user, payment);
        update(cart);
    }

    @Transactional
    public void updateItems(User user, CartItemUpdateRequestDTO request) {
        if (!user.getId().equals(securityService.getLoggedUser().getId()))
            throw new OperationNotAllowedException("Is necessary to be logged in to update your cart");

        var cart = user.getCart();
        request.add().forEach(item -> {
            var product = productService.findById(item.productId());
            product.ifPresent(value -> cartItemService.addItemsToCart(cart, item.productId(), item.quantity()));
        });

        request.remove().forEach(item -> {
            var product = productService.findById(item.productId());
            product.ifPresent(value -> cartItemService.removeItemsFromCart(cart, item.productId(), item.quantity()));
        });

        update(cart);
    }

    public Page<CartItem> filterItems(String name, Category category, Integer page, Integer pageSize, Cart cart) {
        return cartItemService.filterItems(cart, name, category, page, pageSize);
    }
}
