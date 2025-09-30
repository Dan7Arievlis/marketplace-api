package io.github.dan7arievlis.marketplaceapi.controller.dto.product;

import jakarta.validation.constraints.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Set;
import java.util.UUID;

public record ProductUpdateRequestDTO(
        @NotBlank(message = "obligatory field.")
        String name,
        @NotBlank(message = "obligatory field.")
        String description,
        @NotEmpty(message = "obligatory field.")
        Set<UUID> categories,
        @NotNull(message = "obligatory field.")
        BigDecimal price,
        @NotNull(message = "obligatory field.")
        @org.hibernate.validator.constraints.UUID
        UUID vendorId,
        @PastOrPresent(message = "Date must be in past or present.")
        LocalDate fabDate,
        @FutureOrPresent(message = "Date must be in future or present.")
        LocalDate expDate
) {
}
