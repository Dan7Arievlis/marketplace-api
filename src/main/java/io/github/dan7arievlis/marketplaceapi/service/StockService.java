package io.github.dan7arievlis.marketplaceapi.service;

import io.github.dan7arievlis.marketplaceapi.controller.dto.stock.StockItemUpdateRequestDTO;
import io.github.dan7arievlis.marketplaceapi.exceptions.OperationNotAllowedException;
import io.github.dan7arievlis.marketplaceapi.model.Category;
import io.github.dan7arievlis.marketplaceapi.model.Stock;
import io.github.dan7arievlis.marketplaceapi.model.StockItem;
import io.github.dan7arievlis.marketplaceapi.model.User;
import io.github.dan7arievlis.marketplaceapi.repository.StockRepository;
import io.github.dan7arievlis.marketplaceapi.security.SecurityService;
import io.github.dan7arievlis.marketplaceapi.validator.StockValidator;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class StockService {
    private final StockRepository repository;
    private final StockValidator validator;
    private final SecurityService securityService;
    private final ProductService productService;
    private final StockItemService stockItemService;

    public Optional<Stock> findStockById(UUID id) {
        return repository.findById(id);
    }

    public Optional<Stock> findStockByName(User owner, String name) {
        return repository.findByOwnerAndName(owner, name);
    }

    @Transactional
    public void save(Stock stock) {
        validator.validate(stock);
        stock.setUser(securityService.getLoggedUser());
        repository.save(stock);
    }

    @Transactional
    public void create(Stock stock) {
        restore(stock);
        save(stock);
    }

    @Transactional
    public void update(Stock stock) {
        if (stock.getId() == null)
            throw new IllegalArgumentException("Is necessary to have a saved stock in db to update it.");

        save(stock);
    }

    @Transactional
    public void delete(Stock stock) {
        stock.delete();
        save(stock);
    }

    @Transactional
    public void restore(Stock stock) {
        stock.restore();
        save(stock);
    }

    @Transactional
    public void updateItems(User user, StockItemUpdateRequestDTO request) {
        if (!user.getId().equals(securityService.getLoggedUser().getId()))
            throw new OperationNotAllowedException("Is necessary to be logged in to update your stock");

        var stockOptional = findStockByName(user, request.name());
        assert stockOptional.isPresent();
        var stock = stockOptional.get();
        request.add().forEach(item -> {
            var product = productService.findById(item.productId());
            product.ifPresent(value -> stockItemService.addItemsToStock(stock, item.productId(), item.quantity()));
        });

        request.remove().forEach(item -> {
            var product = productService.findById(item.productId());
            product.ifPresent(value -> stockItemService.removeItemsFromStock(stock, item.productId(), item.quantity()));
        });

        update(stock);
    }

    public Page<StockItem> filterItems(String name, Category category, Integer page, Integer pageSize, Stock stock) {
        return stockItemService.filterItems(stock, name, category, page, pageSize);
    }
}
