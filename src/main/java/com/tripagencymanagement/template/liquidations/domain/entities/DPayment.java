package com.tripagencymanagement.template.liquidations.domain.entities;

import com.tripagencymanagement.template.general.entities.domainEntities.BaseAbstractDomainEntity;
import com.tripagencymanagement.template.liquidations.domain.enums.DCurrency;
import com.tripagencymanagement.template.liquidations.domain.enums.DPaymentMethod;
import com.tripagencymanagement.template.liquidations.domain.enums.DPaymentValidity;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@Schema(description = "Entidad de dominio para pagos")
public class DPayment extends BaseAbstractDomainEntity {
    
    @Schema(description = "Método de pago utilizado")
    private DPaymentMethod method;
    
    @Schema(description = "Monto del pago")
    private float amount;
    
    @Schema(description = "Moneda del pago (PEN o USD)")
    private DCurrency currency;
    
    @Schema(description = "ID de la liquidación asociada")
    private Long liquidationId;
    
    @Schema(description = "Estado de validación del pago")
    private DPaymentValidity validationStatus;
    
    @Schema(description = "URL de la evidencia del pago (imagen o PDF)")
    private String evidenceUrl;

    public DPayment(DPaymentMethod method, float amount, DCurrency currency, Long liquidationId) {
        this(method, amount, currency, liquidationId, null);
    }

    public DPayment(DPaymentMethod method, float amount, DCurrency currency, Long liquidationId, String evidenceUrl) {
        super();
        validatePayment(method, amount, liquidationId);
        
        this.method = method;
        this.amount = amount;
        this.currency = currency != null ? currency : DCurrency.PEN;
        this.liquidationId = liquidationId;
        this.validationStatus = DPaymentValidity.PENDING;
        this.evidenceUrl = evidenceUrl;
    }

    private void validatePayment(DPaymentMethod method, float amount, Long liquidationId) {
        if (method == null) {
            throw new IllegalArgumentException("El metodo de pago no puede ser nulo");
        }
        if (amount <= 0) {
            throw new IllegalArgumentException("El monto del pago debe ser mayor que cero");
        }
        if (liquidationId == null) {
            throw new IllegalArgumentException("El ID de liquidación no puede ser nulo");
        }
    }

    public void markAsValid() {
        this.validationStatus = DPaymentValidity.VALID;
    }

    public void markAsInvalid() {
        this.validationStatus = DPaymentValidity.INVALID;
    }

    public boolean isValid() {
        return this.validationStatus == DPaymentValidity.VALID;
    }

    public boolean isPending() {
        return this.validationStatus == DPaymentValidity.PENDING;
    }
}
