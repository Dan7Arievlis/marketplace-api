package io.github.dan7arievlis.marketplaceapi.controller.dto.mapper;

import io.github.dan7arievlis.marketplaceapi.controller.dto.product.ProductNestedResponseDTO;
import io.github.dan7arievlis.marketplaceapi.controller.dto.wishlist.WishlistResponseDTO;
import io.github.dan7arievlis.marketplaceapi.model.Category;
import io.github.dan7arievlis.marketplaceapi.model.Product;
import io.github.dan7arievlis.marketplaceapi.model.Wishlist;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring")
public interface WishlistMapper {

    @Mapping(target = "owner", source = "owner.id")
    WishlistResponseDTO entityToResponse(Wishlist wishlist);

    @Mapping(target = "vendorId", source = "vendor.id")
    @Mapping(target = "categories", source = "categories")
    ProductNestedResponseDTO toProductNested(Product product);

    default List<String> map(Set<Category> categories) {
        if (categories == null) return Collections.emptyList();
        return categories.stream()
                .map(Category::getName)
                .collect(Collectors.toList());
    }
}
