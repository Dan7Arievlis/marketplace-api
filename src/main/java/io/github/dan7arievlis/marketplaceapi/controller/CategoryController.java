package io.github.dan7arievlis.marketplaceapi.controller;

import io.github.dan7arievlis.marketplaceapi.controller.dto.category.CategoryRequestDTO;
import io.github.dan7arievlis.marketplaceapi.controller.dto.category.CategoryResponseDTO;
import io.github.dan7arievlis.marketplaceapi.controller.dto.mapper.CategoryMapper;
import io.github.dan7arievlis.marketplaceapi.model.Category;
import io.github.dan7arievlis.marketplaceapi.service.CategoryService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.UUID;

@RestController
@RequestMapping("categories")
@RequiredArgsConstructor
public class CategoryController implements GenericController {
    private final CategoryService service;
    public final CategoryMapper mapper;

    // create POST /
    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> create(@RequestBody @Valid CategoryRequestDTO categoryRequestDTO) {
        var category = mapper.requestToEntity(categoryRequestDTO);
        service.create(category);
        URI location = generateHeaderLocation(category.getId());
        return ResponseEntity.created(location).build();
    }

    // details GET /id
    @GetMapping("{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    public ResponseEntity<CategoryResponseDTO> findById(@PathVariable String id) {
        return service.findById(UUID.fromString(id))
                .map(category -> ResponseEntity.ok(mapper.entityToResponse(category)))
                .orElseThrow(() -> new EntityNotFoundException("Category not found with id: " + id));
    }

    // search GET /
    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    public ResponseEntity<Page<CategoryResponseDTO>> search(
            @RequestParam(name = "name", required = false)
            String name,
            @RequestParam(value = "page", defaultValue = "0")
            Integer page,
            @RequestParam(value = "page-size", defaultValue = "10")
            Integer pageSize
    ) {
        Page<CategoryResponseDTO> result = service.search(name, page, pageSize).map(mapper::entityToResponse);
        return ResponseEntity.ok(result);
    }

    // update PUT /id
    @PutMapping("{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> update(@PathVariable String id, @RequestBody @Valid CategoryRequestDTO request) {
        return service.findById(UUID.fromString(id))
                .map(category -> {
                    Category aux = mapper.requestToEntity(request);
                    category.setName(aux.getName());
                    service.update(category);

                    return ResponseEntity.noContent().build();
                })
                .orElseThrow( () -> new EntityNotFoundException("Category not found."));
    }

    // delete DELETE /id
    @DeleteMapping("{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> delete(@PathVariable String id) {
        return service.findById(UUID.fromString(id))
                .map(category -> {
                    service.delete(category);
                    return ResponseEntity.noContent().build();
                })
                .orElseThrow( () -> new EntityNotFoundException("Category not found."));
    }

    // restore PATCH /id/restore
    @PatchMapping("{id}/restore")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> restore(@PathVariable String id) {
        return service.findById(UUID.fromString(id))
                .map(category -> {
                    service.restore(category);
                    return ResponseEntity.noContent().build();
                })
                .orElseThrow( () -> new EntityNotFoundException("Category not found."));
    }
}
