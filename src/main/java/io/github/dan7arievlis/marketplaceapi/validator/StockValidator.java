package io.github.dan7arievlis.marketplaceapi.validator;

import io.github.dan7arievlis.marketplaceapi.exceptions.DuplicatedRegisterException;
import io.github.dan7arievlis.marketplaceapi.model.Cart;
import io.github.dan7arievlis.marketplaceapi.model.Stock;
import io.github.dan7arievlis.marketplaceapi.repository.StockRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class StockValidator {
    private final StockRepository repository;

    public void validate(Stock stock) {
        if(existsRegistered(stock))
            throw new DuplicatedRegisterException("Stock already exists.");
    }


    private boolean existsRegistered(Stock stock) {
        Optional<Stock> foundEntity = repository.findByOwnerAndName(stock.getOwner(), stock.getName());
        if (stock.getId() == null)
            return foundEntity.isPresent();

        return foundEntity.isPresent() && !stock.getId().equals(foundEntity.get().getId());
    }
}