package io.github.dan7arievlis.marketplaceapi.controller.dto.user;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.github.dan7arievlis.marketplaceapi.controller.dto.base.BaseResponseDTO;

import java.math.BigDecimal;
import java.time.LocalDate;

public record UserResponseDTO(
        String username,
        String email,
        String firstName,
        String lastName,
        String fullName,
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
        LocalDate birthDate,
        Integer age,
        BigDecimal balance,
        String address,
        String phone,
        String role,
        BaseResponseDTO metaData
) {
}
