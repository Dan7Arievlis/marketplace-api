package io.github.dan7arievlis.marketplaceapi.controller.dto.product;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.github.dan7arievlis.marketplaceapi.controller.dto.base.BaseResponseDTO;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

public record ProductResponseDTO(
        String name,
        String description,
        List<String> categories,
        BigDecimal price,
        UUID vendorId,
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
        LocalDate publishDate,
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
        LocalDate fabDate,
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
        LocalDate expDate,
        BaseResponseDTO metaData
) {
}
