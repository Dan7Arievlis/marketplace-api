package io.github.dan7arievlis.marketplaceapi.controller.dto.wishlist;

import io.github.dan7arievlis.marketplaceapi.controller.dto.base.BaseResponseDTO;

import java.util.UUID;

public record WishlistResponseDTO(
        UUID owner,
        BaseResponseDTO metaData
) {
}
