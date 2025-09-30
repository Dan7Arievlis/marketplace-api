package io.github.dan7arievlis.marketplaceapi.controller.dto.provider;

import io.github.dan7arievlis.marketplaceapi.controller.dto.base.BaseResponseDTO;

public record ProviderResponseDTO(
        String name,
        BaseResponseDTO metaData
) {
}
