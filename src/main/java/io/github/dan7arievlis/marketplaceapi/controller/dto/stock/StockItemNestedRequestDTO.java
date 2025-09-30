package io.github.dan7arievlis.marketplaceapi.controller.dto.stock;

import jakarta.validation.constraints.Positive;

import java.util.UUID;

public record StockItemNestedRequestDTO(
        UUID productId,
        @Positive
        Integer quantity
) {
}
