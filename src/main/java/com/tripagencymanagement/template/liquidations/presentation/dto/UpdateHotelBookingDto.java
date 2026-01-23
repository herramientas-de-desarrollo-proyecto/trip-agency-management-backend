package com.tripagencymanagement.template.liquidations.presentation.dto;

import java.time.LocalDateTime;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class UpdateHotelBookingDto {
    
    @NotNull(message = "La fecha de check-in es obligatoria")
    private LocalDateTime checkIn;

    @NotNull(message = "La fecha de check-out es obligatoria")
    private LocalDateTime checkOut;
    
    @NotBlank(message = "El nombre del hotel es obligatorio")
    private String hotel;

    @NotBlank(message = "La habitación es obligatoria")
    private String room;
    
    private String roomDescription;
    
    @NotNull(message = "El precio por noche es obligatorio")
    @Positive(message = "El precio por noche debe ser positivo")
    private Float priceByNight;

    @NotBlank(message = "La moneda es obligatoria")
    private String currency;
    
    @NotBlank(message = "El estado es obligatorio")
    private String status;
}
