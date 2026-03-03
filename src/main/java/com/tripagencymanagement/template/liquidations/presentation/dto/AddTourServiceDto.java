package com.tripagencymanagement.template.liquidations.presentation.dto;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class AddTourServiceDto {

    @JsonProperty("tariff_rate")
    @NotNull(message = "La tarifa de la tasa de comisión es obligatoria")
    @PositiveOrZero(message = "La tarifa de la tasa de comisión no puede ser negativa")
    private Float tariffRate;

    @JsonProperty("is_taxed")
    @NotNull(message = "El campo de impuesto es obligatorio")
    private Boolean isTaxed;

    @NotBlank(message = "El campo de moneda es obligatorio")
    private String currency;

    @NotEmpty(message = "Se requiere al menos un tour")
    @Valid
    private List<TourDto> tours;
}
