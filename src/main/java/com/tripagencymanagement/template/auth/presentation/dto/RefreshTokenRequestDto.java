package com.tripagencymanagement.template.auth.presentation.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO for refresh token request.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class RefreshTokenRequestDto {
    
    @NotBlank(message = "El refresh token es obligatorio")
    private String refreshToken;
}
