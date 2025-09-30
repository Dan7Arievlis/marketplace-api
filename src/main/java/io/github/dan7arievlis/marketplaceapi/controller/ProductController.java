package io.github.dan7arievlis.marketplaceapi.controller;

import io.github.dan7arievlis.marketplaceapi.controller.dto.product.ProductCategoriesReplaceRequestDTO;
import io.github.dan7arievlis.marketplaceapi.controller.dto.product.ProductCreateRequestDTO;
import io.github.dan7arievlis.marketplaceapi.controller.dto.product.ProductResponseDTO;
import io.github.dan7arievlis.marketplaceapi.controller.dto.product.ProductUpdateRequestDTO;
import io.github.dan7arievlis.marketplaceapi.controller.dto.mapper.ProductMapper;
import io.github.dan7arievlis.marketplaceapi.model.Product;
import io.github.dan7arievlis.marketplaceapi.service.CategoryService;
import io.github.dan7arievlis.marketplaceapi.service.ProductService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.net.URI;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("products")
@RequiredArgsConstructor
public class ProductController implements GenericController {
    private final ProductService service;
    private final CategoryService categoryService;
    public final ProductMapper mapper;

    // create POST /
    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    public ResponseEntity<Void> create(@RequestBody @Valid ProductCreateRequestDTO request) {
        var product = mapper.requestToEntity(request, categoryService);
        service.create(product);
        URI location = generateHeaderLocation(product.getId());
        return ResponseEntity.created(location).build();
    }

    // details GET /id
    @GetMapping("{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    public ResponseEntity<ProductResponseDTO> findById(@PathVariable String id) {
        return service.findById(UUID.fromString(id))
                .map(product -> ResponseEntity.ok(mapper.entityToResponse(product)))
                .orElseThrow(() -> new EntityNotFoundException("Product not found with id: " + id));
    }

    // search GET /
    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    public ResponseEntity<Page<ProductResponseDTO>> search(
            @RequestParam(name = "name", required = false)
            String name,
            @RequestParam(name = "description", required = false)
            String description,
            @RequestParam(name = "category", required = false)
            String category,
            @RequestParam(name = "price", required = false)
            String price,
            @RequestParam(name = "vendor-id", required = false)
            String vendorId,
            @RequestParam(value = "page", defaultValue = "0")
            Integer page,
            @RequestParam(value = "page-size", defaultValue = "10")
            Integer pageSize
    ) {
        var categoryEntity = (category == null || category.isBlank())
                ? null
                : categoryService.findByName(category).orElse(null);

        var priceBD = (price == null || price.isBlank()) ? null : new BigDecimal(price);
        var vendorUUID = (vendorId == null || vendorId.isBlank()) ? null : UUID.fromString(vendorId);

        Page<ProductResponseDTO> result = service
                .search(name, description, categoryEntity, priceBD, vendorUUID, page, pageSize)
                .map(mapper::entityToResponse);

        return ResponseEntity.ok(result);
    }

    // update PUT /id
    @PutMapping("{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> update(@PathVariable String id, @RequestBody @Valid ProductUpdateRequestDTO request) {
        return service.findById(UUID.fromString(id))
                .map(product -> {
                    Product aux = mapper.updateToEntity(request, categoryService);
                    product.setName(aux.getName());
                    product.setDescription(aux.getDescription());
                    product.setCategories(aux.getCategories());
                    product.setPrice(aux.getPrice());
                    product.setVendor(aux.getVendor());
                    product.setFabDate(aux.getFabDate());
                    product.setExpDate(aux.getExpDate());
                    service.update(product);

                    return ResponseEntity.noContent().build();
                })
                .orElseThrow( () -> new EntityNotFoundException("Product not found."));
    }

    // Update fields PATCH /id
    @PatchMapping("{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> updateFields(@PathVariable String id, @RequestBody @Valid Map<String, Object> fields) {
        return service.findById(UUID.fromString(id))
                .map(product -> {
                    service.updateFields(product, fields);
                    return ResponseEntity.noContent().build();
                })
                .orElseThrow( () -> new EntityNotFoundException("Product not found."));
    }

    // delete DELETE /id
    @DeleteMapping("{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> delete(@PathVariable String id) {
        return service.findById(UUID.fromString(id))
                .map(product -> {
                    service.delete(product);
                    return ResponseEntity.noContent().build();
                })
                .orElseThrow( () -> new EntityNotFoundException("Product not found."));
    }

    // restore PATCH /id/restore
    @PatchMapping("{id}/restore")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> restore(@PathVariable String id) {
        return service.findById(UUID.fromString(id))
                .map(product -> {
                    service.restore(product);
                    return ResponseEntity.noContent().build();
                })
                .orElseThrow( () -> new EntityNotFoundException("Product not found."));
    }

    // change categories PATCH id/categories
    @PatchMapping("{id}/categories")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> updateCategories(@PathVariable String id, @RequestBody @Valid ProductCategoriesReplaceRequestDTO request) {
        service.replaceCategories(UUID.fromString(id), request.categoryIds());
        return ResponseEntity.noContent().build();
    }
}
