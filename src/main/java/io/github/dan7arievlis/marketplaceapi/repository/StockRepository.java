package io.github.dan7arievlis.marketplaceapi.repository;

import io.github.dan7arievlis.marketplaceapi.model.Cart;
import io.github.dan7arievlis.marketplaceapi.model.Stock;
import io.github.dan7arievlis.marketplaceapi.model.User;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;
import java.util.UUID;

public interface StockRepository extends CrudRepository<Stock, UUID> {
    Optional<Stock> findByOwnerAndName(User owner, String name);
}
