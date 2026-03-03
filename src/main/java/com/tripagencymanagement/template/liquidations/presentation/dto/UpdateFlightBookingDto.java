package com.tripagencymanagement.template.liquidations.presentation.dto;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonProperty;
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
public class UpdateFlightBookingDto {
    
    @NotBlank(message = "El origen es obligatorio")
    private String origin;

    @NotBlank(message = "El destino es obligatorio")
    private String destiny;
    
    @JsonProperty("departure_date")
    @NotNull(message = "La fecha de salida es obligatoria")
    private LocalDateTime departureDate;

    @JsonProperty("arrival_date")
    @NotNull(message = "La fecha de llegada es obligatoria")
    private LocalDateTime arrivalDate;

    @NotBlank(message = "La aerolínea es obligatoria")
    private String aeroline;

    @JsonProperty("aeroline_booking_code")
    @NotBlank(message = "El código de reserva de aerolínea es obligatorio")
    private String aerolineBookingCode;

    @JsonProperty("costamar_booking_code")
    private String costamarBookingCode;

    @JsonProperty("tkt_numbers")
    @NotBlank(message = "Los números de TKT son obligatorios")
    private String tktNumbers;

    @NotBlank(message = "El estado es obligatorio")
    private String status;

    @JsonProperty("total_price")
    @NotNull(message = "El precio total es obligatorio")
    @Positive(message = "El precio total debe ser positivo")
    private Float totalPrice;
    
    @NotBlank(message = "La moneda es obligatoria")
    private String currency;
}
