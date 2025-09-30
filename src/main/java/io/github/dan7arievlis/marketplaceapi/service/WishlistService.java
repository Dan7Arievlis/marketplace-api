package io.github.dan7arievlis.marketplaceapi.service;

import io.github.dan7arievlis.marketplaceapi.controller.dto.wishlist.WishlistUpdateRequestDTO;
import io.github.dan7arievlis.marketplaceapi.exceptions.OperationNotAllowedException;
import io.github.dan7arievlis.marketplaceapi.model.Category;
import io.github.dan7arievlis.marketplaceapi.model.Product;
import io.github.dan7arievlis.marketplaceapi.model.User;
import io.github.dan7arievlis.marketplaceapi.model.Wishlist;
import io.github.dan7arievlis.marketplaceapi.repository.WishlistRepository;
import io.github.dan7arievlis.marketplaceapi.security.SecurityService;
import io.github.dan7arievlis.marketplaceapi.service.component.OptionalSpecificationSearch;
import io.github.dan7arievlis.marketplaceapi.validator.WishlistValidator;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class WishlistService implements OptionalSpecificationSearch {
    private final WishlistRepository repository;
    private final WishlistValidator validator;
    private final SecurityService securityService;
    private final ProductService productService;

    public Optional<Wishlist> findById(UUID id) {
        return repository.findById(id);
    }

    @Transactional
    public void save(Wishlist wishlist) {
        validator.validate(wishlist);
        wishlist.setUser(securityService.getLoggedUser());
        repository.save(wishlist);
    }

    @Transactional
    public void create(Wishlist wishlist) {
        restore(wishlist);
        save(wishlist);
    }

    @Transactional
    public void update(Wishlist wishlist) {
        if (wishlist.getId() == null)
            throw new IllegalArgumentException("Is necessary to have a saved wishlist in db to update it");

        save(wishlist);
    }

    @Transactional
    public void delete(Wishlist wishlist) {
        wishlist.delete();
        repository.save(wishlist);
    }

    @Transactional
    public void restore(Wishlist wishlist) {
        wishlist.restore();
        repository.save(wishlist);
    }

    @Transactional
    public void updateProducts(User user, WishlistUpdateRequestDTO request) {
        if (!user.getId().equals(securityService.getLoggedUser().getId()))
            throw new OperationNotAllowedException("Is necessary to be logged in to update your wishlist");

        var wishlist = user.getWishlist();
        request.add().forEach(productId -> {
            var product = productService.findById(productId);
            product.ifPresent(value -> wishlist.getProducts().add(value));
        });

        request.remove().forEach(productId -> {
            var product = productService.findById(productId);
            product.ifPresent(value -> wishlist.getProducts().remove(value));
        });

        update(wishlist);
    }

    public Page<Product> filterProducts(String name, Category category, Integer page, Integer pageSize) {
        return productService.search(name, null, category, null, null, page, pageSize);
    }
}
