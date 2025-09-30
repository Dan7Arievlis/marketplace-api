package io.github.dan7arievlis.marketplaceapi.controller.dto.cart;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.github.dan7arievlis.marketplaceapi.controller.dto.base.BaseResponseDTO;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

public record CartResponseDTO(
        UUID owner,
        BigDecimal total,
        BigDecimal paidAmount,
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", timezone = "UTC")
        LocalDateTime purchaseMoment,
        String status,
        BaseResponseDTO metaData
) {
}
