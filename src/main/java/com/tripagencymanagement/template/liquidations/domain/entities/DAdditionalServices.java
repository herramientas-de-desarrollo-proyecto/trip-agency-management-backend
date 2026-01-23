package com.tripagencymanagement.template.liquidations.domain.entities;

import com.tripagencymanagement.template.liquidations.domain.enums.DServiceStatus;
import com.tripagencymanagement.template.users.domain.enums.DCurrency;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class DAdditionalServices extends DBaseAbstractService {
    private float price;
    private DServiceStatus status;

    public DAdditionalServices(float tariffRate, boolean isTaxed, DCurrency currency, 
                              Long liquidationId, float price) {
        super(tariffRate, isTaxed, currency, liquidationId);
        validatePrice(price);
        this.price = price;
        this.status = DServiceStatus.PENDING;
    }

    private void validatePrice(float price) {
        if (price < 0) {
            throw new IllegalArgumentException("El precio no puede ser negativo.");
        }
    }

    @Override
    public float calculateTotal() {
        return applyTariffAndTax(price);
    }

    @Override
    public float calculateBaseAmount() {
        return price;
    }

    public void complete() {
        this.status = DServiceStatus.COMPLETED;
    }

    public void cancel() {
        this.status = DServiceStatus.CANCELED;
    }

    public boolean isPending() {
        return this.status == DServiceStatus.PENDING;
    }
}
