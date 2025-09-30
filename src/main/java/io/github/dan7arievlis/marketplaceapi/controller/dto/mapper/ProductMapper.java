package io.github.dan7arievlis.marketplaceapi.controller.dto.mapper;

import io.github.dan7arievlis.marketplaceapi.controller.dto.product.ProductCreateRequestDTO;
import io.github.dan7arievlis.marketplaceapi.controller.dto.product.ProductNestedResponseDTO;
import io.github.dan7arievlis.marketplaceapi.controller.dto.product.ProductResponseDTO;
import io.github.dan7arievlis.marketplaceapi.controller.dto.product.ProductUpdateRequestDTO;
import io.github.dan7arievlis.marketplaceapi.model.Category;
import io.github.dan7arievlis.marketplaceapi.model.Product;
import io.github.dan7arievlis.marketplaceapi.model.User;
import io.github.dan7arievlis.marketplaceapi.service.CategoryService;
import org.mapstruct.*;

import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface ProductMapper {

    @Mapping(target = "vendor", source = "vendorId")
    @Mapping(target = "categories", source = "categories", qualifiedByName = "idsToCategories")
    Product requestToEntity(ProductCreateRequestDTO productCreateRequestDTO, @Context CategoryService service);

    @Mapping(target = "vendor", source = "vendorId")
    @Mapping(target = "categories", source = "categories", qualifiedByName = "idsToCategories")
    Product updateToEntity(ProductUpdateRequestDTO request, @Context CategoryService service);

    @Mapping(target = "vendorId", source = "vendor.id")
    @Mapping(target = "categories", source = "categories", qualifiedByName = "categoriesToNames")
    ProductResponseDTO entityToResponse(Product product);

    default User map(UUID id) {
        if (id == null) return null;
        User user = new User();
        user.setId(id);
        return user;
    }

    @Named(value = "idsToCategories")
    default Set<Category> map(Set<UUID> ids, @Context CategoryService service) {
        if (ids == null) return null;
        return ids.stream()
                .map(service::findById)
                .map(opt -> opt.orElse(null))
                .collect(Collectors.toSet());
    }

    @Named(value = "categoriesToNames")
    default List<String> map(Set<Category> categories) {
        if (categories == null) return List.of();
        return categories.stream()
                .map(Category::getName)
                .toList();
    }
}
