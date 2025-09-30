package io.github.dan7arievlis.marketplaceapi.controller.dto.client;

import jakarta.validation.constraints.NotBlank;

public record ClientRequestDTO (
    @NotBlank(message = "obligatory field")
    String clientId,
    @NotBlank(message = "obligatory field")
    String clientSecret,
    @NotBlank(message = "obligatory field")
    String redirectURI,
    String scope
){}
