package io.github.dan7arievlis.marketplaceapi.controller.dto.cart;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.github.dan7arievlis.marketplaceapi.controller.dto.base.BaseResponseDTO;
import io.github.dan7arievlis.marketplaceapi.controller.dto.product.ProductNestedResponseDTO;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

public record CartItemResponseDTO(
        Integer quantity,
        String status,
        BigDecimal subtotal,
        ProductNestedResponseDTO product
) {
}
