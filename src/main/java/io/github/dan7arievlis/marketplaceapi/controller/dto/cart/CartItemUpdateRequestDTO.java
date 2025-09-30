package io.github.dan7arievlis.marketplaceapi.controller.dto.cart;

import java.util.List;

public record CartItemUpdateRequestDTO(
        List<CartItemNestedRequestDTO> add,
        List<CartItemNestedRequestDTO> remove
) {
}
