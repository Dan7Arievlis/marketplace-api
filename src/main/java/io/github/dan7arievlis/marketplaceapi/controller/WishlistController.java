package io.github.dan7arievlis.marketplaceapi.controller;

import io.github.dan7arievlis.marketplaceapi.controller.dto.mapper.WishlistMapper;
import io.github.dan7arievlis.marketplaceapi.controller.dto.product.ProductNestedResponseDTO;
import io.github.dan7arievlis.marketplaceapi.controller.dto.wishlist.WishlistResponseDTO;
import io.github.dan7arievlis.marketplaceapi.controller.dto.wishlist.WishlistUpdateRequestDTO;
import io.github.dan7arievlis.marketplaceapi.service.CategoryService;
import io.github.dan7arievlis.marketplaceapi.service.UserService;
import io.github.dan7arievlis.marketplaceapi.service.WishlistService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("users/{id}/wishlist")
@RequiredArgsConstructor
public class WishlistController implements GenericController {
    private final WishlistService service;
    private final UserService userService;
    private final WishlistMapper mapper;
    private final CategoryService categoryService;

    // details GET /
    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    public ResponseEntity<WishlistResponseDTO> findById(@PathVariable String id) {
        return userService.findById(UUID.fromString(id))
                .map(user -> {
                    var optional = service.findById(user.getWishlist().getId());
                    return optional.map(wishlist ->
                            ResponseEntity.ok(mapper.entityToResponse(wishlist))
                    ).orElse(null);
                })
                .orElseThrow(() -> new EntityNotFoundException("User not found with id: " + id));
    }

    @GetMapping("products")
    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    public ResponseEntity<?> getProducts(
           @PathVariable String id,
           @RequestParam(name = "name", required = false)
           String name,
           @RequestParam(name = "category", required = false)
           String category,
           @RequestParam(value = "page", defaultValue = "0")
           Integer page,
           @RequestParam(value = "page-size", defaultValue = "10")
           Integer pageSize
    ) {
        return userService.findById(UUID.fromString(id))
                .map(user -> {
                    var optional = service.findById(user.getWishlist().getId());
                    var categoryEntity = (category == null || category.isBlank())
                            ? null
                            : categoryService.findByName(category).orElse(null);

                    Page<ProductNestedResponseDTO> products = optional.map(wishlist ->
                            service.filterProducts(name, categoryEntity, page, pageSize)
                                    .map(mapper::toProductNested)
                    ).orElse(null);
                    return ResponseEntity.ok(products);
                })
                .orElseThrow(() -> new EntityNotFoundException("User not found with id: " + id));
    }

    // update wishlist products PATCH /
    @PatchMapping("products")
    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    public ResponseEntity<?> updateProducts(@PathVariable String id, @RequestBody @Valid WishlistUpdateRequestDTO request) {
        return userService.findById(UUID.fromString(id))
                .map(user -> {
                    service.updateProducts(user, request);
                    return ResponseEntity.noContent().build();
                })
                .orElseThrow( () -> new EntityNotFoundException("User not found."));
    }
}
