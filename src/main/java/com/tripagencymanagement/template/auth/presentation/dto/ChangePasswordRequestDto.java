package com.tripagencymanagement.template.auth.presentation.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO for change password request.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Solicitud de cambio de contraseña")
public class ChangePasswordRequestDto {

    @NotBlank(message = "La contraseña actual es requerida")
    @Schema(description = "Contraseña actual del usuario", example = "currentPassword123")
    private String currentPassword;

    @NotBlank(message = "La nueva contraseña es requerida")
    @Size(min = 8, message = "La nueva contraseña debe tener al menos 8 caracteres")
    @Schema(description = "Nueva contraseña (mínimo 8 caracteres)", example = "newSecurePassword456")
    private String newPassword;

    @NotBlank(message = "La confirmación de contraseña es requerida")
    @Schema(description = "Confirmación de la nueva contraseña", example = "newSecurePassword456")
    private String confirmPassword;
}
