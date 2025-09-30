package io.github.dan7arievlis.marketplaceapi.controller.dto.user;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@Schema(name = "ChangePasswordRequest")
public record ChangePasswordRequestDTO(
        @NotBlank(message = "obligatory field.")
        String currentPassword,
        @NotBlank(message = "obligatory field.")
        @Size(min = 8, message = "field must have at least {min} characters.")
        String newPassword,
        @NotBlank(message = "obligatory field.")
        String confirmPassword
) {
}
