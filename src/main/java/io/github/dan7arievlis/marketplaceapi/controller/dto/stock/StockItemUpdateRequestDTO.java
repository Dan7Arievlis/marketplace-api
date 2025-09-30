package io.github.dan7arievlis.marketplaceapi.controller.dto.stock;

import jakarta.validation.constraints.NotBlank;

import java.util.List;

public record StockItemUpdateRequestDTO(
        @NotBlank(message = "obligatory filed.")
        String name,
        List<StockItemNestedRequestDTO> add,
        List<StockItemNestedRequestDTO> remove
) {
}
