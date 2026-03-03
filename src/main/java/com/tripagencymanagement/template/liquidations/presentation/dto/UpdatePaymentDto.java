package com.tripagencymanagement.template.liquidations.presentation.dto;

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
public class UpdatePaymentDto {
    
    @JsonProperty("payment_method")
    @NotBlank(message = "El método de pago es obligatorio")
    private String paymentMethod;

    @Positive(message = "El monto debe ser mayor que cero")
    @NotNull(message = "El monto es obligatorio")
    private Float amount;

    @JsonProperty("validation_status")
    @NotBlank(message = "El estado de validación es obligatorio")
    private String validationStatus;
}
