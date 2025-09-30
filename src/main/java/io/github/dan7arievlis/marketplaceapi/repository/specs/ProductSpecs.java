package io.github.dan7arievlis.marketplaceapi.repository.specs;

import io.github.dan7arievlis.marketplaceapi.model.Category;
import io.github.dan7arievlis.marketplaceapi.model.Product;
import io.github.dan7arievlis.marketplaceapi.model.User;
import io.github.dan7arievlis.marketplaceapi.model.enums.UserRoles;
import jakarta.persistence.criteria.Expression;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.JoinType;
import org.springframework.data.jpa.domain.Specification;

import java.math.BigDecimal;
import java.util.UUID;

public class ProductSpecs {
    public static Specification<Product> nameLike(String name) {
        return (root, query, cb) -> cb.like(cb.upper(root.get("name")), "%" + name.toUpperCase() + "%");
    }

    public static Specification<Product> descriptionLike(String description) {
        return (root, query, cb) -> cb.like(cb.upper(root.get("description")), "%" + description.toUpperCase() + "%");
    }

    public static Specification<Product> isUnderPrice(BigDecimal price) {
        return (root, query, cb) -> cb.lessThanOrEqualTo(root.get("price"), price);
    }

    public static Specification<Product> hasVendorId(UUID vendorId) {
        return (root, query, cb) -> cb.equal(root.get("vendor").get("id"), vendorId);
    }

    public static Specification<Product> hasCategory(Category category) {
        return (root, query, cb) -> cb.isMember(category, root.get("categories"));
    }
}
