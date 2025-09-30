package io.github.dan7arievlis.marketplaceapi.service;

import io.github.dan7arievlis.marketplaceapi.model.Category;
import io.github.dan7arievlis.marketplaceapi.repository.CategoryRepository;
import io.github.dan7arievlis.marketplaceapi.repository.specs.CategorySpecs;
import io.github.dan7arievlis.marketplaceapi.security.SecurityService;
import io.github.dan7arievlis.marketplaceapi.service.component.OptionalSpecificationSearch;
import io.github.dan7arievlis.marketplaceapi.validator.CategoryValidator;
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
public class CategoryService implements OptionalSpecificationSearch {
    private final CategoryRepository repository;
    private final CategoryValidator validator;
    private final SecurityService securityService;

    public Optional<Category> findById(UUID id) {
        return repository.findById(id);
    }

    public Optional<Category> findByName(String name) {
        return repository.findByName(name);
    }

    @Transactional
    public void save(Category category) {
        validator.validate(category);
        category.setName(category.getName().toUpperCase().replaceAll(" ", "_"));
        category.setUser(securityService.getLoggedUser());
        repository.save(category);
    }

    @Transactional
    public void create(Category category) {
        restore(category);
        save(category);
    }

    public Page<Category> search(String name, Integer page, Integer pageSize) {
        Specification<Category> specs = Stream.of(
                optSpec(name, CategorySpecs::nameLikeIgnoresCase)
        ).reduce(Specification.allOf((root, query, cb) -> cb.isTrue(root.get("active"))), Specification::and);

        Pageable pageRequest = PageRequest.of(page, pageSize);
        return repository.findAll(specs, pageRequest);
    }

    @Transactional
    public void update(Category category) {
        if (category.getId() == null)
            throw new IllegalArgumentException("Is necessary to have a saved category in db to update it");

        save(category);
    }

    @Transactional
    public void delete(Category category) {
        category.delete();
        repository.save(category);
    }

    @Transactional
    public void restore(Category category) {
        category.restore();
        repository.save(category);
    }
}
