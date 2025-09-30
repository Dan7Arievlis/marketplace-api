package io.github.dan7arievlis.marketplaceapi.controller;

import io.github.dan7arievlis.marketplaceapi.controller.dto.category.CategoryRequestDTO;
import io.github.dan7arievlis.marketplaceapi.controller.dto.category.CategoryResponseDTO;
import io.github.dan7arievlis.marketplaceapi.controller.dto.mapper.ProviderMapper;
import io.github.dan7arievlis.marketplaceapi.controller.dto.provider.ProviderRequestDTO;
import io.github.dan7arievlis.marketplaceapi.controller.dto.provider.ProviderResponseDTO;
import io.github.dan7arievlis.marketplaceapi.model.Category;
import io.github.dan7arievlis.marketplaceapi.model.Provider;
import io.github.dan7arievlis.marketplaceapi.service.ProviderService;
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
@RequestMapping("providers")
@RequiredArgsConstructor
public class ProviderController implements GenericController {
    private final ProviderService service;
    public final ProviderMapper mapper;

    // create POST /
    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> create(@RequestBody @Valid ProviderRequestDTO request) {
        var provider = mapper.requestToEntity(request);
        service.create(provider);
        URI location = generateHeaderLocation(provider.getId());
        return ResponseEntity.created(location).build();
    }

    // details GET /id
    @GetMapping("{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    public ResponseEntity<ProviderResponseDTO> findById(@PathVariable String id) {
        return service.findById(UUID.fromString(id))
                .map(provider -> ResponseEntity.ok(mapper.entityToResponse(provider)))
                .orElseThrow(() -> new EntityNotFoundException("Provider not found with id: " + id));
    }

    // search GET /
    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    public ResponseEntity<Page<ProviderResponseDTO>> search(
            @RequestParam(name = "name", required = false)
            String name,
            @RequestParam(value = "page", defaultValue = "0")
            Integer page,
            @RequestParam(value = "page-size", defaultValue = "10")
            Integer pageSize
    ) {
        Page<ProviderResponseDTO> result = service.search(name, page, pageSize).map(mapper::entityToResponse);
        return ResponseEntity.ok(result);
    }

    // update PUT /id
    @PutMapping("{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> update(@PathVariable String id, @RequestBody @Valid ProviderRequestDTO request) {
        return service.findById(UUID.fromString(id))
                .map(provider -> {
                    Provider aux = mapper.requestToEntity(request);
                    provider.setName(aux.getName());
                    service.update(provider);

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
