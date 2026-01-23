package com.tripagencymanagement.template.liquidations.presentation.dto;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
@Schema(description = "DTO para agregar un nuevo pago a una liquidación")
public class AddPaymentDto {
    
    @NotNull(message = "El método de pago es obligatorio")
    @Schema(description = "Método de pago", example = "DEBIT", requiredMode = Schema.RequiredMode.REQUIRED)
    private String paymentMethod;

    @Positive(message = "El monto debe ser mayor que cero")
    @NotNull(message = "El monto es obligatorio")
    @Schema(description = "Monto del pago", example = "150.50", requiredMode = Schema.RequiredMode.REQUIRED)
    private Float amount;

    @Schema(description = "Moneda del pago", example = "PEN", defaultValue = "PEN")
    private String currency = "PEN";

    @Schema(description = "URL de la evidencia del pago (imagen o PDF)", example = "https://example.com/evidence.jpg")
    private String evidenceUrl;
}
