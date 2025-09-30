package io.github.dan7arievlis.marketplaceapi.service;

import io.github.dan7arievlis.marketplaceapi.model.*;
import io.github.dan7arievlis.marketplaceapi.repository.CartItemRepository;
import io.github.dan7arievlis.marketplaceapi.repository.StockItemRepository;
import io.github.dan7arievlis.marketplaceapi.repository.specs.CartItemSpecs;
import io.github.dan7arievlis.marketplaceapi.repository.specs.StockItemSpecs;
import io.github.dan7arievlis.marketplaceapi.security.SecurityService;
import io.github.dan7arievlis.marketplaceapi.service.component.OptionalSpecificationSearch;
import io.github.dan7arievlis.marketplaceapi.validator.CartItemValidator;
import io.github.dan7arievlis.marketplaceapi.validator.StockItemValidator;
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
public class StockItemService implements OptionalSpecificationSearch {
    private final StockItemRepository repository;
    private final StockItemValidator validator;
    private final SecurityService securityService;
    private final ProductService productService;

    public Optional<StockItem> findById(UUID id) {
        return repository.findById(id);
    }

    @Transactional
    public void save(StockItem stockItem) {
        validator.validate(stockItem);
        stockItem.setUser(securityService.getLoggedUser());
        repository.save(stockItem);
    }

    @Transactional
    public void create(StockItem stockItem) {
        restore(stockItem);
        save(stockItem);
    }

    @Transactional
    public void fullUpdate(StockItem aux, String itemId) {
        var item = findById(UUID.fromString(itemId));
        if (item.isPresent()) {
            item.get().setItemStatus(aux.getItemStatus());
            update(item.get());
        }
    }

    @Transactional
    public void update(StockItem stockItem) {
        if (stockItem.getId() == null)
            throw new IllegalArgumentException("Is necessary to have a saved stock item in db to update it");

        save(stockItem);
    }

    @Transactional
    public void delete(StockItem stockItem) {
        stockItem.delete();
        save(stockItem);
    }

    @Transactional
    public void restore(StockItem stockItem) {
        stockItem.restore();
        save(stockItem);
    }

    @Transactional
    public void addItemsToStock(Stock stock, UUID productId, Integer quantity) {
        Optional<Product> product = productService.findById(productId);
        if (product.isPresent()) {
            Optional<StockItem> stockItemOptional = stock.itemByProduct(product.get());
            if (stockItemOptional.isEmpty()) {
                StockItem stockItem = new StockItem();
                stockItem.setProduct(product.get());
                stockItem.setQuantity(quantity);
                stockItem.setUser(securityService.getLoggedUser());

                stock.addItem(stockItem);
                restore(stockItem);
            } else {
                var stockItem = stockItemOptional.get();
                stockItem.setQuantity(stockItem.getQuantity() + quantity);
                restore(stockItem);
            }
        }
    }

    @Transactional
    public void removeItemsFromStock(Stock stock, UUID productId, Integer quantity) {
        var product = productService.findById(productId);
        if (product.isPresent()) {
            Optional<StockItem> stockItemOptional = stock.itemByProduct(product.get());
            if (stockItemOptional.isPresent()) {
                var stockItem = stockItemOptional.get();
                stockItem.setQuantity(stockItem.getQuantity() - quantity);
                save(stockItem);

                if (stockItem.getQuantity() <= 0) {
                    stockItem.setQuantity(0);
                    stock.removeItem(stockItem);
                }
            }
        }
    }

    public Page<StockItem> filterItems(Stock stock, String name, Category category, Integer page, Integer pageSize) {
        Specification<StockItem> specs = Stream.of(
                optSpec(name, StockItemSpecs::nameLike),
                optSpec(category, StockItemSpecs::hasCategory),
                optSpec(stock, StockItemSpecs::inStock)
        ).reduce(Specification.allOf((root, query, cb) ->
                cb.isTrue(root.get("active"))), Specification::and);

        Pageable pageable = PageRequest.of(page, pageSize);

        return repository.findAll(specs, pageable);
    }
}
