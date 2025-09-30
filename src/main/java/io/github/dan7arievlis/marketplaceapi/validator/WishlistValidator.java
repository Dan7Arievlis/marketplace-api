package io.github.dan7arievlis.marketplaceapi.validator;

import io.github.dan7arievlis.marketplaceapi.exceptions.DuplicatedRegisterException;
import io.github.dan7arievlis.marketplaceapi.model.Wishlist;
import io.github.dan7arievlis.marketplaceapi.repository.WishlistRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class WishlistValidator {
    private final WishlistRepository repository;

    public void validate(Wishlist wishlist) {
        if(existsRegistered(wishlist))
            throw new DuplicatedRegisterException("Wishlist already exists.");
    }

    private boolean existsRegistered(Wishlist wishlist) {
        Optional<Wishlist> foundEntity = repository.findByOwner(wishlist.getOwner());
        if (wishlist.getId() == null)
            return foundEntity.isPresent();

        return foundEntity.isPresent() && !wishlist.getId().equals(foundEntity.get().getId());
    }
}