package io.github.dan7arievlis.marketplaceapi.service;

import io.github.dan7arievlis.marketplaceapi.exceptions.DuplicatedRegisterException;
import io.github.dan7arievlis.marketplaceapi.model.Category;
import io.github.dan7arievlis.marketplaceapi.model.Provider;
import io.github.dan7arievlis.marketplaceapi.repository.CategoryRepository;
import io.github.dan7arievlis.marketplaceapi.repository.ProviderRepository;
import io.github.dan7arievlis.marketplaceapi.repository.specs.CategorySpecs;
import io.github.dan7arievlis.marketplaceapi.repository.specs.ProviderSpecs;
import io.github.dan7arievlis.marketplaceapi.security.SecurityService;
import io.github.dan7arievlis.marketplaceapi.service.component.OptionalSpecificationSearch;
import io.github.dan7arievlis.marketplaceapi.validator.CategoryValidator;
import io.github.dan7arievlis.marketplaceapi.validator.ProviderValidator;
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
public class ProviderService implements OptionalSpecificationSearch {
    private final ProviderRepository repository;
    private final ProviderValidator validator;
    private final SecurityService securityService;

    public Optional<Provider> findById(UUID id) {
        return repository.findById(id);
    }

    @Transactional
    public void save(Provider provider) {
        try {
            validator.validate(provider);
            provider.setName(provider.getName().toUpperCase().replaceAll(" ", "_"));
            provider.setUser(securityService.getLoggedUser());
            repository.save(provider);
        } catch (DuplicatedRegisterException ignored) {}
    }

    @Transactional
    public void create(Provider provider) {
        restore(provider);
        save(provider);
    }

    public Page<Provider> search(String name, Integer page, Integer pageSize) {
        Specification<Provider> specs = Stream.of(
                optSpec(name, ProviderSpecs::nameLikeIgnoresCase)
        ).reduce(Specification.allOf((root, query, cb) -> cb.isTrue(root.get("active"))), Specification::and);

        Pageable pageRequest = PageRequest.of(page, pageSize);
        return repository.findAll(specs, pageRequest);
    }

    @Transactional
    public void update(Provider provider) {
        if (provider.getId() == null)
            throw new IllegalArgumentException("Is necessary to have a saved provider in db to update it");

        save(provider);
    }

    @Transactional
    public void restore(Provider provider) {
        provider.restore();
        repository.save(provider);
    }

    @Transactional
    public void delete(Provider provider) {
        provider.delete();
        repository.save(provider);
    }
}
