package io.github.dan7arievlis.marketplaceapi.validator;

import io.github.dan7arievlis.marketplaceapi.exceptions.DuplicatedRegisterException;
import io.github.dan7arievlis.marketplaceapi.exceptions.InvalidFieldException;
import io.github.dan7arievlis.marketplaceapi.model.Category;
import io.github.dan7arievlis.marketplaceapi.model.Product;
import io.github.dan7arievlis.marketplaceapi.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class ProductValidator {
    private final ProductRepository repository;

    public void validate(Product product) {
        if(existsRegistered(product))
            throw new DuplicatedRegisterException("Product already exists.");

        if(containsCategory(product, "COMIDA") || containsCategory(product, "PERECIVEL")) {
            if (product.getFabDate() == null)
                throw new InvalidFieldException("fabDate", "Perishable products must have fabrication date.");
            if (product.getExpDate() == null)
                throw new InvalidFieldException("expDate", "Perishable products must have expiry date.");
        }

        if(product.getFabDate() != null && product.getExpDate() != null)
            if(product.getFabDate().isAfter(product.getExpDate()))
                throw new IllegalArgumentException("Fabrication date must be after expiry date.");
    }

    public boolean containsCategory(Product product, String category) {
        return product.getCategories()
                .stream()
                .map(Category::getName)
                .collect(Collectors.toSet())
                .contains(category);
    }

    private boolean existsRegistered(Product product) {
        Optional<Product> foundEntity = repository.findByName(product.getName());
        if (product.getId() == null)
            return foundEntity.isPresent();

        return foundEntity.isPresent() && !product.getId().equals(foundEntity.get().getId());
    }
}