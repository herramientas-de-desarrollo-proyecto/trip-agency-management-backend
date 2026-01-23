package com.tripagencymanagement.template.auth.presentation.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO for change password response.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Respuesta de cambio de contraseña")
public class ChangePasswordResponseDto {

    @Schema(description = "Indica si el cambio fue exitoso", example = "true")
    private boolean success;

    @Schema(description = "Mensaje descriptivo del resultado", example = "Contraseña actualizada exitosamente")
    private String message;
}
