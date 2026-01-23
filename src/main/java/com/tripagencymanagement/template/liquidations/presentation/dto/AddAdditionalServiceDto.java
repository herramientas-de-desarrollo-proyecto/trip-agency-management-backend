package com.tripagencymanagement.template.liquidations.presentation.dto;

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
public class AddAdditionalServiceDto {
    
    @NotNull(message = "La tarifa de la tasa de comisión es obligatorio")
    @Positive(message = "La tarifa de la tasa de comisión debe ser positiva")
    private Float tariffRate;

    @NotNull(message = "El campo de impuesto es obligatorio")
    private Boolean isTaxed;
    
    @NotBlank(message = "El campo de moneda es obligatorio")
    private String currency;

    @NotNull(message = "El campo de precio es obligatorio")
    @Positive(message = "El campo de precio debe ser positivo")
    private Float price;
    
    @NotBlank(message = "El campo de estado es obligatorio")
    private String status;
}
