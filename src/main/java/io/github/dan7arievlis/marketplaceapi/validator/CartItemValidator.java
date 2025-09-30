package io.github.dan7arievlis.marketplaceapi.validator;

import io.github.dan7arievlis.marketplaceapi.exceptions.DuplicatedRegisterException;
import io.github.dan7arievlis.marketplaceapi.model.Cart;
import io.github.dan7arievlis.marketplaceapi.model.CartItem;
import io.github.dan7arievlis.marketplaceapi.model.enums.ItemStatus;
import io.github.dan7arievlis.marketplaceapi.repository.CartItemRepository;
import io.github.dan7arievlis.marketplaceapi.repository.CartRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class CartItemValidator {
    private final CartItemRepository repository;

    public void validate(CartItem cartItem) {
        if(existsRegistered(cartItem))
            throw new DuplicatedRegisterException("Cart item already exists.");
    }


    private boolean existsRegistered(CartItem cartItem) {
        Optional<CartItem> foundEntity = repository.findByProductAndCartAndCreatedAt(cartItem.getProduct(), cartItem.getCart(), cartItem.getCreatedAt());
        if (cartItem.getId() == null)
            return foundEntity.isPresent();

        return foundEntity.isPresent() && !cartItem.getId().equals(foundEntity.get().getId());
    }
}