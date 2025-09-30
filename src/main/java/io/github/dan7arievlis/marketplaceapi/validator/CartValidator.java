package io.github.dan7arievlis.marketplaceapi.validator;

import io.github.dan7arievlis.marketplaceapi.exceptions.DuplicatedRegisterException;
import io.github.dan7arievlis.marketplaceapi.exceptions.InvalidFieldException;
import io.github.dan7arievlis.marketplaceapi.model.Cart;
import io.github.dan7arievlis.marketplaceapi.model.Category;
import io.github.dan7arievlis.marketplaceapi.model.Product;
import io.github.dan7arievlis.marketplaceapi.repository.CartRepository;
import io.github.dan7arievlis.marketplaceapi.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class CartValidator {
    private final CartRepository repository;

    public void validate(Cart cart) {
        if(existsRegistered(cart))
            throw new DuplicatedRegisterException("Cart already exists.");
    }


    private boolean existsRegistered(Cart cart) {
        Optional<Cart> foundEntity = repository.findByOwner(cart.getOwner());
        if (cart.getId() == null)
            return foundEntity.isPresent();

        return foundEntity.isPresent() && !cart.getId().equals(foundEntity.get().getId());
    }
}