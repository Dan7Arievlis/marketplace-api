package io.github.dan7arievlis.marketplaceapi.controller.dto.cart;

import jakarta.validation.constraints.NotNull;

public record CartItemUpdateStatusDTO(
        @NotNull(message = "obligatory field.")
        String status
) {
}
