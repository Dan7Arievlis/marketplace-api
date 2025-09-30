package io.github.dan7arievlis.marketplaceapi.controller.dto.user;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import org.springframework.format.annotation.DateTimeFormat;

import java.math.BigDecimal;
import java.time.LocalDate;

@Schema(name = "UserRequest")
public record UserUpdateRequestDTO(
    @NotBlank(message = "obligatory field.")
    @Size(min = 2, max = 30, message = "field out of bounds. ({min} - {max})")
    String username,
    @NotBlank(message = "obligatory field.")
    @Email(message = "invalid email.")
    @Size(min = 5, max = 80, message = "field out of bounds. ({min} - {max})")
    String email,
    @NotBlank(message = "obligatory field.")
    String firstName,
    @NotBlank(message = "obligatory field.")
    String lastName,
    @NotNull(message = "obligatory field.")
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    @PastOrPresent(message = "date con't be in future.")
    LocalDate birthDate,
    @NotNull(message = "obligatory field.")
    BigDecimal balance,
    @NotNull(message = "obligatory field.")
    String address,
    @NotBlank(message = "obligatory field.")
    String phone,
    @NotNull(message = "obligatory field.")
    String role
){}
