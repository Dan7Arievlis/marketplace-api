package io.github.dan7arievlis.marketplaceapi.controller.dto.user;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import org.springframework.format.annotation.DateTimeFormat;

import java.math.BigDecimal;
import java.time.LocalDate;

@Schema(name = "UserRequest")
public record UserCreateRequestDTO(
    @NotBlank(message = "obligatory field.")
    @Size(min = 2, max = 30, message = "field out of bounds. ({min} - {max})")
    String username,
    @NotBlank(message = "obligatory field.")
    @Email(message = "invalid email.")
    @Size(min = 5, max = 80, message = "field out of bounds. ({min} - {max})")
    String email,
    @NotBlank(message = "obligatory field.")
    @Size(min = 8, message = "field must have at least {min} characters.")
    String password,
    @NotBlank(message = "obligatory field.")
    String confirmPassword,
    @NotBlank(message = "obligatory field.")
    String firstName,
    @NotBlank(message = "obligatory field.")
    String lastName,
    @NotNull(message = "obligatory field.")
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    @PastOrPresent(message = "date con't be in future.")
    LocalDate birthDate,
    BigDecimal balance,
    @NotNull(message = "obligatory field.")
    String address,
    String phone,
    @NotNull(message = "obligatory field.")
    String role
){}
