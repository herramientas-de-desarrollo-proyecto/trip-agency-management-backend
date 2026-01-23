package com.tripagencymanagement.template.liquidations.presentation.dto;

import java.time.LocalDateTime;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class UpdateIncidencyDto {
    
    @NotBlank(message = "El motivo es obligatorio")
    private String reason;

    @PositiveOrZero(message = "El monto no puede ser negativo")
    private Float amount;
    
    @NotNull(message = "La fecha de incidencia es obligatoria")
    private LocalDateTime incidencyDate;
    
    @NotBlank(message = "El estado de la incidencia es obligatorio")
    private String incidencyStatus;
}
