package io.github.dan7arievlis.marketplaceapi.controller.dto.provider;

import jakarta.validation.constraints.NotBlank;

public record ProviderRequestDTO(
        @NotBlank(message = "obligatory field.")
        String name
) {
}
