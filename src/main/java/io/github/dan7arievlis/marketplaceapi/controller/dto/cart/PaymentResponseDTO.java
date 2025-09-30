package io.github.dan7arievlis.marketplaceapi.controller.dto.cart;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.github.dan7arievlis.marketplaceapi.controller.dto.base.BaseResponseDTO;
import io.github.dan7arievlis.marketplaceapi.controller.dto.provider.ProviderNestedResponseDTO;
import io.github.dan7arievlis.marketplaceapi.controller.dto.provider.ProviderResponseDTO;
import io.github.dan7arievlis.marketplaceapi.model.enums.PaymentStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Schema(name = "PaymentRequest")
public record PaymentResponseDTO(
    UUID cart,
    BigDecimal amount,
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", timezone = "UTC")
    LocalDateTime paidAt,
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", timezone = "UTC")
    LocalDateTime confirmedAt,
    PaymentStatus paymentStatus,
    ProviderNestedResponseDTO provider,
    BaseResponseDTO metaData
){}
