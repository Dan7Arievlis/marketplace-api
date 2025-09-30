package io.github.dan7arievlis.marketplaceapi.service;

import io.github.dan7arievlis.marketplaceapi.model.Category;
import io.github.dan7arievlis.marketplaceapi.model.Product;
import io.github.dan7arievlis.marketplaceapi.model.User;
import io.github.dan7arievlis.marketplaceapi.model.Wishlist;
import io.github.dan7arievlis.marketplaceapi.repository.CategoryRepository;
import io.github.dan7arievlis.marketplaceapi.repository.ProductRepository;
import io.github.dan7arievlis.marketplaceapi.repository.specs.ProductSpecs;
import io.github.dan7arievlis.marketplaceapi.security.SecurityService;
import io.github.dan7arievlis.marketplaceapi.service.component.OptionalSpecificationSearch;
import io.github.dan7arievlis.marketplaceapi.validator.ProductValidator;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.util.ReflectionUtils;

import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Stream;

@Service
@RequiredArgsConstructor
public class ProductService implements OptionalSpecificationSearch {
    private final ProductRepository repository;
    private final CategoryRepository categoryRepository;
    private final ProductValidator validator;
    private final SecurityService securityService;

    public Optional<Product> findById(UUID id) {
        return repository.findById(id);
    }

    @Transactional
    public void save(Product product) {
        validator.validate(product);
        product.setUser(securityService.getLoggedUser());
        repository.save(product);
    }

    @Transactional
    public void create(Product product) {
        restore(product);
        save(product);
    }

    public Page<Product> search(String name, String description, Category category, BigDecimal price, UUID vendorId, Integer page, Integer pageSize) {
        Specification<Product> specs = Stream.of(
                optSpec(name, ProductSpecs::nameLike),
                optSpec(description, ProductSpecs::descriptionLike),
                optSpec(category, ProductSpecs::hasCategory),
                optSpec(price, ProductSpecs::isUnderPrice),
                optSpec(vendorId, ProductSpecs::hasVendorId)
            ).reduce(Specification.allOf((root, query, cb) -> cb.isTrue(root.get("active"))), Specification::and);

        Pageable pageable = PageRequest.of(page, pageSize);

        return repository.findAll(specs, pageable);
    }

    @Transactional
    public void update(Product product) {
        if (product.getId() == null)
            throw new IllegalArgumentException("Is necessary to have a saved product in db to update it");

        save(product);
    }

    @Transactional
    public void updateFields(Product product, Map<String, Object> fields) {
        var blackList = Set.of("id", "createdAt", "updatedAt", "deletedAt", "user", "active", "vendor", "categories", "publishDate");
        fields.forEach((key, value) -> {
            if (blackList.contains(key)) return;
            Field field = ReflectionUtils.findField(Product.class, key);
            assert field != null;
            try {
                field.setAccessible(true);
                parseSpecialTypes(field, product, value);
            } catch (IllegalAccessException e) {
                throw new RuntimeException(e);
            }
        });
        save(product);
    }

    private void parseSpecialTypes(Field field, Product product, Object value) throws IllegalAccessException {
        if (value == null) {
            field.set(product, null);
            return;
        }

        switch (field.getType().getSimpleName()) {
            case "BigDecimal":
                field.set(product, new BigDecimal(value.toString()));
                break;
            case "LocalDate":
                field.set(product, LocalDate.parse(value.toString()));
                break;
            default:
                field.set(product, value);
        }
    }

    @Transactional
    public void replaceCategories(UUID productId, Set<UUID> categoryIds) {
        var product = repository.findById(productId)
                .orElseThrow(() -> new EntityNotFoundException("Product not found."));

        var categories = categoryRepository.findAllById(categoryIds);
        product.setCategories(new HashSet<>(categories));
        save(product);
    }

    @Transactional
    public void delete(Product product) {
        product.delete();
        save(product);
    }

    @Transactional
    public void restore(Product product) {
        product.restore();
        save(product);
    }
}
