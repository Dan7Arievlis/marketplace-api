package io.github.dan7arievlis.marketplaceapi.controller.dto.cart;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Schema(name = "PaymentRequest")
public record PaymentCreateRequestDTO(
    @NotNull(message = "obligatory field.")
    BigDecimal amount,
    @NotNull(message = "obligatory field.")
    UUID providerId,
    @NotBlank(message = "obligatory field.")
    String externalId,
    @NotBlank(message = "obligatory field.")
    String authorizationCode
){}
