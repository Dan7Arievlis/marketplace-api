package io.github.dan7arievlis.marketplaceapi.controller.dto.product;

import jakarta.validation.constraints.NotEmpty;

import java.util.Set;
import java.util.UUID;

public record ProductCategoriesReplaceRequestDTO(
        @NotEmpty(message = "Obligatory field.")
        Set<UUID> categoryIds
) {
}
