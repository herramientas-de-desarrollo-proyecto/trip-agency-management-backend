package com.tripagencymanagement.template.liquidations.presentation.dto;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class CreateLiquidationDto {

    @JsonProperty("currency_rate")
    @Positive(message = "La tasa de cambio debe ser mayor que cero")
    @NotNull(message = "La tasa de cambio es obligatoria")
    private Float currencyRate;

    @JsonProperty("payment_deadline")
    @NotNull(message = "La fecha límite de pago es obligatoria")
    private LocalDateTime paymentDeadline;

    @PositiveOrZero(message = "El número de acompañantes no puede ser negativo")
    @NotNull(message = "El número de acompañantes es obligatorio")
    private Integer companion;

    @JsonProperty("customer_id")
    @NotNull(message = "El ID del cliente es obligatorio")
    private Long customerId;

    @JsonProperty("staff_id")
    @NotNull(message = "El ID del personal es obligatorio")
    private Long staffId;
}
