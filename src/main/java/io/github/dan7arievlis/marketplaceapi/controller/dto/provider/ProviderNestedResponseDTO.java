package io.github.dan7arievlis.marketplaceapi.controller.dto.provider;

import java.util.UUID;

public record ProviderNestedResponseDTO(
        String name,
        UUID id
) {
}
