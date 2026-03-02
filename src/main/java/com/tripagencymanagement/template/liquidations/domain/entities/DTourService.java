package com.tripagencymanagement.template.liquidations.domain.entities;

import java.util.ArrayList;
import java.util.List;

import com.tripagencymanagement.template.users.domain.enums.DCurrency;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class DTourService extends DBaseAbstractService {
    private List<DTour> tours;

    public DTourService(float tariffRate, boolean isTaxed, DCurrency currency, Long liquidationId) {
        super(tariffRate, isTaxed, currency, liquidationId);
        this.tours = new ArrayList<>();
    }

    public void addTour(DTour tour) {
        if (tour == null) {
            throw new IllegalArgumentException("El tour no puede ser nulo");
        }
        this.tours.add(tour);
    }

    public void removeTour(DTour tour) {
        this.tours.remove(tour);
    }

    @Override
    public float calculateTotal() {
        float baseTotal = calculateBaseAmount();
        return applyTariffAndTax(baseTotal);
    }

    @Override
    public float calculateBaseAmount() {
        if (tours == null || tours.isEmpty()) {
            return 0f;
        }
        
        return tours.stream()
            .filter(tour -> Boolean.TRUE.equals(tour.getIsActive()))
            .map(DTour::getPrice)
            .reduce(0f, Float::sum);
    }

    public boolean hasTours() {
        return tours != null && !tours.isEmpty();
    }

    public int getTourCount() {
        return tours != null ? tours.size() : 0;
    }
}
