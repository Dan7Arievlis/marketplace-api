package io.github.dan7arievlis.marketplaceapi.controller.dto.category;

import io.github.dan7arievlis.marketplaceapi.controller.dto.base.BaseResponseDTO;

public record CategoryResponseDTO(
        String name,
        BaseResponseDTO metaData
) {
}
