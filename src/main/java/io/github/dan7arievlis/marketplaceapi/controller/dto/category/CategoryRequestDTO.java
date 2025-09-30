package io.github.dan7arievlis.marketplaceapi.controller.dto.category;

import jakarta.validation.constraints.NotBlank;

public record CategoryRequestDTO(
        @NotBlank(message = "obligatory field.")
        String name
) {
}
