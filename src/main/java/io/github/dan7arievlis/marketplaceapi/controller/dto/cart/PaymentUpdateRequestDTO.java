package io.github.dan7arievlis.marketplaceapi.controller.dto.cart;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;

@Schema(name = "PaymentUpdate")
public record PaymentUpdateRequestDTO(
    @NotBlank(message = "obligatory field.")
    String status
){}
