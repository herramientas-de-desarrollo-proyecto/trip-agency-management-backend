package com.tripagencymanagement.template.auth.presentation.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO for logout response.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LogoutResponseDto {
    
    private String message;
    private boolean success;
}
