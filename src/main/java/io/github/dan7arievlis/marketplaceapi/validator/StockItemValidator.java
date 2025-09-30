package io.github.dan7arievlis.marketplaceapi.validator;

import io.github.dan7arievlis.marketplaceapi.exceptions.DuplicatedRegisterException;
import io.github.dan7arievlis.marketplaceapi.model.StockItem;
import io.github.dan7arievlis.marketplaceapi.repository.StockItemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class StockItemValidator {
    private final StockItemRepository repository;

    public void validate(StockItem stockItem) {
        if(existsRegistered(stockItem))
            throw new DuplicatedRegisterException("Stock item already exists.");
    }


    private boolean existsRegistered(StockItem stockItem) {
        Optional<StockItem> foundEntity = repository.findByProductAndStock(stockItem.getProduct(), stockItem.getStock());
        if (stockItem.getId() == null)
            return foundEntity.isPresent();

        return foundEntity.isPresent() && !stockItem.getId().equals(foundEntity.get().getId());
    }
}