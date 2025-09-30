package io.github.dan7arievlis.marketplaceapi.controller.dto.cart;

import jakarta.validation.constraints.Positive;

import java.util.UUID;

public record CartItemNestedRequestDTO(
        UUID productId,
        @Positive
        Integer quantity
) {
}
