package com.tripagencymanagement.template.liquidations.domain.entities;

import com.tripagencymanagement.template.general.entities.domainEntities.BaseAbstractDomainEntity;
import com.tripagencymanagement.template.users.domain.enums.DCurrency;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public abstract class DBaseAbstractService extends BaseAbstractDomainEntity {
    private float tariffRate;
    private boolean isTaxed;
    private DCurrency currency;
    private Long liquidationId;

    protected DBaseAbstractService(float tariffRate, boolean isTaxed, DCurrency currency, Long liquidationId) {
        super();
        validateBaseService(tariffRate, currency, liquidationId);
        
        this.tariffRate = tariffRate;
        this.isTaxed = isTaxed;
        this.currency = currency;
        this.liquidationId = liquidationId;
    }

    private void validateBaseService(float tariffRate, DCurrency currency, Long liquidationId) {
        if (tariffRate < 0) {
            throw new IllegalArgumentException("La tarifa no puede ser negativa.");
        }
        if (currency == null) {
            throw new IllegalArgumentException("La moneda no puede ser nula.");
        }
        if (liquidationId == null) {
            throw new IllegalArgumentException("El ID de liquidación no puede ser nulo.");
        }
    }

    /**
     * Calcula el total del servicio (base + comisión + impuestos si aplica)
     */
    public abstract float calculateTotal();

    /**
     * Calcula el monto base del servicio sin tarifa ni impuestos
     */
    public abstract float calculateBaseAmount();

    /**
     * Calcula solo la comisión del servicio.
     * El tariffRate se recibe desde el front como porcentaje (ej: 20 = 20%)
     * y se convierte a decimal (0.20) para el cálculo.
     */
    public float calculateCommission() {
        float baseAmount = calculateBaseAmount();
        // tariffRate viene como porcentaje (ej: 20), se divide entre 100 para obtener 0.20
        return baseAmount * (tariffRate / 100);
    }

    /**
     * Aplica la tarifa y el impuesto al monto base.
     * Si isTaxed = true, significa que el IGV ya está incluido en el precio.
     * Si isTaxed = false, se debe añadir el IGV (18%) al total.
     */
    protected float applyTariffAndTax(float baseAmount) {
        float withTariff = baseAmount + (baseAmount * tariffRate / 100);
        if (!isTaxed) {
            // Añadir IGV (18% en Perú) porque no está incluido en el precio
            return withTariff * 1.18f;
        }
        // El IGV ya está incluido en el precio base
        return withTariff;
    }

    /**
     * Verifica si el servicio está en PEN
     */
    public boolean isPEN() {
        return currency == DCurrency.PEN;
    }

    /**
     * Verifica si el servicio está en USD
     */
    public boolean isUSD() {
        return currency == DCurrency.USD;
    }
}
