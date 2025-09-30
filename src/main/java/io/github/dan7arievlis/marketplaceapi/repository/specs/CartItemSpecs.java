package io.github.dan7arievlis.marketplaceapi.repository.specs;

import io.github.dan7arievlis.marketplaceapi.model.Cart;
import io.github.dan7arievlis.marketplaceapi.model.CartItem;
import io.github.dan7arievlis.marketplaceapi.model.Category;
import io.github.dan7arievlis.marketplaceapi.model.Product;
import jakarta.persistence.criteria.JoinType;
import org.springframework.data.jpa.domain.Specification;

import java.math.BigDecimal;
import java.util.UUID;

public class CartItemSpecs {
    public static Specification<CartItem> nameLike(String name) {
        return (root, query, cb) ->
                cb.like(cb.upper(root.join("products").get("products.name")), "%" + name.toUpperCase() + "%");
    }

    public static Specification<CartItem> hasCategory(Category category) {
        return (root, query, cb) ->
                cb.isMember(category, root.join("products").get("products.categories"));
    }

    public static Specification<CartItem> inCart(Cart cart) {
        return (root, query, cb) -> cb.equal(root.get("cart").get("id"), cart.getId());
    }
}
