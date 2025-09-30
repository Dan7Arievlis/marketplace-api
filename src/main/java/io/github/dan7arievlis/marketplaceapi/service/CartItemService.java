package io.github.dan7arievlis.marketplaceapi.service;

import io.github.dan7arievlis.marketplaceapi.model.Cart;
import io.github.dan7arievlis.marketplaceapi.model.CartItem;
import io.github.dan7arievlis.marketplaceapi.model.Category;
import io.github.dan7arievlis.marketplaceapi.model.Product;
import io.github.dan7arievlis.marketplaceapi.model.enums.ItemStatus;
import io.github.dan7arievlis.marketplaceapi.repository.CartItemRepository;
import io.github.dan7arievlis.marketplaceapi.repository.specs.CartItemSpecs;
import io.github.dan7arievlis.marketplaceapi.security.SecurityService;
import io.github.dan7arievlis.marketplaceapi.service.component.OptionalSpecificationSearch;
import io.github.dan7arievlis.marketplaceapi.validator.CartItemValidator;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;
import java.util.stream.Stream;

@Service
@RequiredArgsConstructor
public class CartItemService implements OptionalSpecificationSearch {
    private final CartItemRepository repository;
    private final CartItemValidator validator;
    private final SecurityService securityService;
    private final ProductService productService;

    public Optional<CartItem> findById(UUID id) {
        return repository.findById(id);
    }

    @Transactional
    public void save(CartItem cartItem) {
        validator.validate(cartItem);
        cartItem.setUser(securityService.getLoggedUser());
        repository.save(cartItem);
    }

    @Transactional
    public void create(CartItem cartItem) {
        restore(cartItem);
        save(cartItem);
    }

    @Transactional
    public void fullUpdate(CartItem aux, String itemId) {
        var item = findById(UUID.fromString(itemId));
        if (item.isPresent()) {
            item.get().setItemStatus(aux.getItemStatus());
            update(item.get());
        }
    }

    @Transactional
    public void update(CartItem cartItem) {
        if (cartItem.getId() == null)
            throw new IllegalArgumentException("Is necessary to have a saved cart item in db to update it");

        save(cartItem);
    }

    @Transactional
    public void delete(CartItem cartItem) {
        cartItem.delete();
        save(cartItem);
    }

    @Transactional
    public void restore(CartItem cartItem) {
        cartItem.restore();
        save(cartItem);
    }

    @Transactional
    public void addItemsToCart(Cart cart, UUID productId, Integer quantity) {
        Optional<Product> product = productService.findById(productId);
        if (product.isPresent()) {
            Optional<CartItem> cartItemOptional = cart.itemByProduct(product.get());
            if (cartItemOptional.isEmpty()) {
                CartItem cartItem = new CartItem();
                cartItem.setProduct(product.get());
                cartItem.setQuantity(quantity);
                cartItem.setUser(securityService.getLoggedUser());

                cart.addItem(cartItem);
                restore(cartItem);
            } else {
                var cartItem = cartItemOptional.get();
                cartItem.setQuantity(cartItem.getQuantity() + quantity);
                restore(cartItem);
            }
        }
    }

    @Transactional
    public void removeItemsFromCart(Cart cart, UUID productId, Integer quantity) {
        var product = productService.findById(productId);
        if (product.isPresent()) {
            Optional<CartItem> cartItemOptional = cart.itemByProduct(product.get());
            if (cartItemOptional.isPresent()) {
                var cartItem = cartItemOptional.get();
                cartItem.setQuantity(cartItem.getQuantity() - quantity);
                save(cartItem);

                if (cartItem.getQuantity() <= 0) {
                    cartItem.setQuantity(0);
                    cart.removeItem(cartItem);
                }
            }
        }
    }

    public Page<CartItem> filterItems(Cart cart, String name, Category category, Integer page, Integer pageSize) {
        Specification<CartItem> specs = Stream.of(
                optSpec(name, CartItemSpecs::nameLike),
                optSpec(category, CartItemSpecs::hasCategory),
                optSpec(cart, CartItemSpecs::inCart)
        ).reduce(Specification.allOf((root, query, cb) ->
                cb.isTrue(root.get("active"))), Specification::and);

        Pageable pageable = PageRequest.of(page, pageSize);

        return repository.findAll(specs, pageable);
    }
}
