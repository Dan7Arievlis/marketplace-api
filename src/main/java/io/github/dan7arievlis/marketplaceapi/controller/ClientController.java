package io.github.dan7arievlis.marketplaceapi.controller;

import io.github.dan7arievlis.marketplaceapi.controller.dto.client.ClientRequestDTO;
import io.github.dan7arievlis.marketplaceapi.controller.dto.mapper.ClientMapper;
import io.github.dan7arievlis.marketplaceapi.service.ClientService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;

@RestController
@RequestMapping("clients")
@RequiredArgsConstructor
public class ClientController implements GenericController {
    private final ClientService service;
    private final ClientMapper mapper;

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> create(@RequestBody ClientRequestDTO request) {
        var user = mapper.requestToEntity(request);
        service.create(user);
        URI location = generateHeaderLocation(user.getId());
        return ResponseEntity.created(location).build();
    }
}
