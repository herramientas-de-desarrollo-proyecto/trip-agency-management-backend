package com.tripagencymanagement.template.liquidations.presentation.dto;

import java.util.List;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class AddTourServiceDto {

    @NotNull(message = "La tarifa de la tasa de comisión es obligatoria")
    @Positive(message = "La tarifa de la tasa de comisión debe ser positiva")
    private Float tariffRate;
    
    @NotNull(message = "El campo de impuesto es obligatorio")
    private Boolean isTaxed;

    @NotBlank(message = "El campo de moneda es obligatorio")
    private String currency;

    @NotEmpty(message = "Se requiere al menos un tour")
    @Valid
    private List<TourDto> tours;
}
