package io.github.dan7arievlis.marketplaceapi.controller.dto.product;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

public record ProductNestedResponseDTO(
        String name,
        List<String> categories,
        BigDecimal price,
        UUID vendorId
) {
}
