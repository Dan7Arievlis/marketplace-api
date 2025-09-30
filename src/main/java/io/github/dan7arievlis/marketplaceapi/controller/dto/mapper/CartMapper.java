package io.github.dan7arievlis.marketplaceapi.controller.dto.mapper;

import io.github.dan7arievlis.marketplaceapi.controller.dto.cart.*;
import io.github.dan7arievlis.marketplaceapi.controller.dto.provider.ProviderNestedResponseDTO;
import io.github.dan7arievlis.marketplaceapi.model.*;
import io.github.dan7arievlis.marketplaceapi.model.enums.CartStatus;
import io.github.dan7arievlis.marketplaceapi.model.enums.ItemStatus;
import jakarta.validation.Valid;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import java.util.List;
import java.util.Set;
import java.util.UUID;

@Mapper(componentModel = "spring")
public interface CartMapper {
    @Mapping(target = "owner", source = "owner.id")
    @Mapping(target = "status", source = "status")
    CartResponseDTO entityToResponse(Cart cart);

    @Mapping(target = "status", source = "itemStatus")
    @Mapping(target = "product.categories", source = "product.categories", qualifiedByName = "categoriesToNames")
    @Mapping(target = "product.vendorId", source = "product.vendor.id")
    CartItemResponseDTO toCartItemResponse(CartItem cartItem);

    @Mapping(target = "cart", source = "cart.id")
    @Mapping(target = "provider", source = "provider")
    PaymentResponseDTO paymentToResponse(Payment payment);

    Payment createRequestToPayment(PaymentCreateRequestDTO request);

    CartItem updateToEntity(CartItemUpdateStatusDTO request);

    default ProviderNestedResponseDTO map(Provider provider) {
        return provider == null ? null : new ProviderNestedResponseDTO(provider.getName(), provider.getId());
    }

    default String map(CartStatus status) {
        return status == null ? null : status.toString();
    }

    default String map(ItemStatus status) {
        return status == null ? null : status.toString();
    }

    default User map(UUID id) {
        if (id == null) return null;
        User user = new User();
        user.setId(id);
        return user;
    }

    @Named(value = "categoriesToNames")
    default List<String> map(Set<Category> categories) {
        if (categories == null) return List.of();
        return categories.stream()
                .map(Category::getName)
                .toList();
    }
}
