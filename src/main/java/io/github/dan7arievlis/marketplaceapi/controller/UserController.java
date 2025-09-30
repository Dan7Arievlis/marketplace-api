package io.github.dan7arievlis.marketplaceapi.controller;

import io.github.dan7arievlis.marketplaceapi.controller.dto.user.ChangePasswordRequestDTO;
import io.github.dan7arievlis.marketplaceapi.controller.dto.user.UserCreateRequestDTO;
import io.github.dan7arievlis.marketplaceapi.controller.dto.user.UserResponseDTO;
import io.github.dan7arievlis.marketplaceapi.controller.dto.user.UserUpdateRequestDTO;
import io.github.dan7arievlis.marketplaceapi.controller.dto.mapper.UserMapper;
import io.github.dan7arievlis.marketplaceapi.model.User;
import io.github.dan7arievlis.marketplaceapi.model.enums.UserRoles;
import io.github.dan7arievlis.marketplaceapi.service.UserService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("users")
@RequiredArgsConstructor
public class UserController implements GenericController {
    public final UserService service;
    public final UserMapper mapper;

    // create POST /
    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> create(@RequestBody @Valid UserCreateRequestDTO request) {
        service.matchConfirmPassword(request.password(), request.confirmPassword());
        var user = mapper.requestToEntity(request);
        service.create(user);
        URI location = generateHeaderLocation(user.getId());
        return ResponseEntity.created(location).build();
    }

    // Details GET /id
    @GetMapping("{id}")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public ResponseEntity<UserResponseDTO> findById(@PathVariable String id) {
        return service.findById(UUID.fromString(id))
                .map(user -> ResponseEntity.ok(mapper.entityToResponse(user)))
                .orElseThrow(() -> new EntityNotFoundException("User not found with id: " + id));
    }

    // Search GET /
    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    public ResponseEntity<Page<UserResponseDTO>> search(
            @RequestParam(value = "username", required = false)
            String username,
            @RequestParam(value = "email", required = false)
            String email,
            @RequestParam(value = "name", required = false)
            String name,
            @RequestParam(value = "address", required = false)
            String address,
            @RequestParam(value = "role", required = false)
            UserRoles role,
            @RequestParam(value = "page", defaultValue = "0")
            Integer page,
            @RequestParam(value = "page-size", defaultValue = "10")
            Integer pageSize
    ) {
        Page<UserResponseDTO> result = service.search(username, email, name, address, role, page, pageSize)
                .map(mapper::entityToResponse);

        return ResponseEntity.ok(result);
    }

    // Delete DELETE /id
    @DeleteMapping("{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> delete(@PathVariable String id) {
        return service.findById(UUID.fromString(id))
                .map(user -> {
                    service.delete(user);
                    return ResponseEntity.noContent().build();
                })
                .orElseThrow( () -> new EntityNotFoundException("User not found."));
    }

    // Restore PATCH /id/restore
    @PatchMapping("{id}/restore")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> restore(@PathVariable String id) {
        return service.findById(UUID.fromString(id))
                .map(user -> {
                    service.restore(user);
                    return ResponseEntity.noContent().build();
                })
                .orElseThrow( () -> new EntityNotFoundException("User not found."));
    }

    // Update PUT /id
    @PutMapping("{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> update(@PathVariable String id, @RequestBody @Valid UserUpdateRequestDTO request) {
        return service.findById(UUID.fromString(id))
                .map(user -> {
                    User aux = mapper.updateToEntity(request);
                    service.fullUpdate(aux, user);

                    return ResponseEntity.noContent().build();
                })
                .orElseThrow( () -> new EntityNotFoundException("User not found."));
    }

    // Update fields PATCH /id
    @PatchMapping("{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> updateFields(@PathVariable String id, @RequestBody @Valid Map<String, Object> fields) {
        return service.findById(UUID.fromString(id))
                .map(user -> {
                    service.updateFields(user, fields);
                    return ResponseEntity.noContent().build();
                })
                .orElseThrow( () -> new EntityNotFoundException("User not found."));
    }

    // Change password PATCH /id/password
    @PatchMapping("{id}/password")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> updateFields(@PathVariable String id, @RequestBody @Valid ChangePasswordRequestDTO request) {
        return service.findById(UUID.fromString(id))
                .map(user -> {
                    service.changePassword(user, request);
                    return ResponseEntity.noContent().build();
                })
                .orElseThrow( () -> new EntityNotFoundException("User not found."));
    }
}
