package io.github.dan7arievlis.marketplaceapi.validator;

import io.github.dan7arievlis.marketplaceapi.exceptions.DuplicatedRegisterException;
import io.github.dan7arievlis.marketplaceapi.model.Category;
import io.github.dan7arievlis.marketplaceapi.model.Provider;
import io.github.dan7arievlis.marketplaceapi.repository.CategoryRepository;
import io.github.dan7arievlis.marketplaceapi.repository.ProviderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class ProviderValidator {
    private final ProviderRepository repository;

    public void validate(Provider provider) {
        if(existsRegistered(provider))
            throw new DuplicatedRegisterException("Provider already exists.");
    }

    private boolean existsRegistered(Provider provider) {
        Optional<Provider> foundEntity = repository.findByNameIgnoreCase(provider.getName());
        if (provider.getId() == null)
            return foundEntity.isPresent();

        return foundEntity.isPresent() && !provider.getId().equals(foundEntity.get().getId());
    }
}