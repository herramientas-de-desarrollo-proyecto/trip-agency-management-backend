package com.tripagencymanagement.template.liquidations.presentation.dto;

import java.time.LocalDateTime;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class FlightBookingDto {
    
    @NotBlank(message = "El origin es obligatorio")
    private String origin;

    @NotBlank(message = "El destiny es obligatorio")
    private String destiny;
    
    @NotNull(message = "La fecha de salida es obligatoria")
    private LocalDateTime departureDate;
    
    @NotNull(message = "La fecha de llegada es obligatoria")
    private LocalDateTime arrivalDate;
    
    @NotBlank(message = "El aeroline es obligatorio")
    private String aeroline;
    
    @NotBlank(message = "El código de reserva de aeroline es obligatorio")
    private String aerolineBookingCode;
    
    private String costamarBookingCode;
    
    @NotBlank(message = "Los números de TKT son obligatorios")
    private String tktNumbers;
    
    @NotBlank(message = "El estado es obligatorio")
    private String status;
    
    @NotNull(message = "El precio total es obligatorio")
    @Positive(message = "El precio total debe ser positivo")
    private Float totalPrice;
    
    @NotBlank(message = "La moneda es obligatoria")
    private String currency;
}
