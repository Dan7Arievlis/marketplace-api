package io.github.dan7arievlis.marketplaceapi.validator;

import io.github.dan7arievlis.marketplaceapi.exceptions.DuplicatedRegisterException;
import io.github.dan7arievlis.marketplaceapi.model.Category;
import io.github.dan7arievlis.marketplaceapi.repository.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class CategoryValidator {
    private final CategoryRepository repository;

    public void validate(Category category) {
        if(existsRegistered(category))
            throw new DuplicatedRegisterException("Category already exists.");
    }

    private boolean existsRegistered(Category category) {
        Optional<Category> foundEntity = repository.findByName(category.getName());
        if (category.getId() == null)
            return foundEntity.isPresent();

        return foundEntity.isPresent() && !category.getId().equals(foundEntity.get().getId());
    }
}