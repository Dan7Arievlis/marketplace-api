package io.github.dan7arievlis.marketplaceapi.controller.dto.wishlist;

import java.util.List;
import java.util.UUID;

public record WishlistUpdateRequestDTO(
        List<UUID> add,
        List<UUID> remove
) {
}
