package io.github.dan7arievlis.marketplaceapi.repository.specs;

import io.github.dan7arievlis.marketplaceapi.model.Category;
import org.springframework.data.jpa.domain.Specification;

public class CategorySpecs {
    public static Specification<Category> nameLikeIgnoresCase(String name) {
        return (root, query, cb) -> cb.like(cb.upper(root.get("name")), "%" + name.toUpperCase() + "%");
    }
}
