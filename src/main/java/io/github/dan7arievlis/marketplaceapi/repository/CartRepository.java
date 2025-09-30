package io.github.dan7arievlis.marketplaceapi.repository;

import io.github.dan7arievlis.marketplaceapi.model.Cart;
import io.github.dan7arievlis.marketplaceapi.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.Optional;
import java.util.UUID;

public interface CartRepository extends JpaRepository<Cart, UUID>, JpaSpecificationExecutor<Cart> {
    Optional<Cart> findByOwner(User owner);
}
