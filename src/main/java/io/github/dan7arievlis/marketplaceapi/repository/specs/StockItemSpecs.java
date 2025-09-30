package io.github.dan7arievlis.marketplaceapi.repository.specs;

import io.github.dan7arievlis.marketplaceapi.model.*;
import org.springframework.data.jpa.domain.Specification;

public class StockItemSpecs {
    public static Specification<StockItem> nameLike(String name) {
        return (root, query, cb) ->
                cb.like(cb.upper(root.join("products").get("products.name")), "%" + name.toUpperCase() + "%");
    }

    public static Specification<StockItem> hasCategory(Category category) {
        return (root, query, cb) ->
                cb.isMember(category, root.join("products").get("products.categories"));
    }

    public static Specification<StockItem> inStock(Stock stock) {
        return (root, query, cb) -> cb.equal(root.get("stock").get("id"), stock.getId());
    }
}
