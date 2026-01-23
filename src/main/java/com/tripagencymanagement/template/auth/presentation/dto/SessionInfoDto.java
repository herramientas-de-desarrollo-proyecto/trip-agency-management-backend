package com.tripagencymanagement.template.auth.presentation.dto;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO for session information.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class SessionInfoDto {
    
    private Long id;
    private LocalDateTime createdAt;
    private LocalDateTime expiresAt;
    private String userAgent;
    private String ipAddress;
    private Boolean isCurrent;
    private Boolean isActive;
}
