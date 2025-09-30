package io.github.dan7arievlis.marketplaceapi.repository.specs;

import io.github.dan7arievlis.marketplaceapi.model.Provider;
import org.springframework.data.jpa.domain.Specification;

public class ProviderSpecs {
    public static Specification<Provider> nameLikeIgnoresCase(String name) {
        return (root, query, cb) -> cb.like(cb.upper(root.get("name")), "%" + name.toUpperCase() + "%");
    }
}
