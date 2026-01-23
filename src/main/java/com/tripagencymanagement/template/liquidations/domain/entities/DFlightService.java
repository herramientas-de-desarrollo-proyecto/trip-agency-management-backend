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
public class DFlightService extends DBaseAbstractService {
    private List<DFlightBooking> flightBookings;

    public DFlightService(float tariffRate, boolean isTaxed, DCurrency currency, Long liquidationId) {
        super(tariffRate, isTaxed, currency, liquidationId);
        this.flightBookings = new ArrayList<>();
    }

    public void addFlightBooking(DFlightBooking flightBooking) {
        if (flightBooking == null) {
            throw new IllegalArgumentException("La reserva de vuelo no puede ser nula");
        }
        this.flightBookings.add(flightBooking);
    }

    public void removeFlightBooking(DFlightBooking flightBooking) {
        this.flightBookings.remove(flightBooking);
    }

    @Override
    public float calculateTotal() {
        float baseTotal = calculateBaseAmount();
        return applyTariffAndTax(baseTotal);
    }

    @Override
    public float calculateBaseAmount() {
        if (flightBookings == null || flightBookings.isEmpty()) {
            return 0f;
        }
        
        return flightBookings.stream()
            .map(DFlightBooking::getTotalPrice)
            .reduce(0f, Float::sum);
    }

    public boolean hasBookings() {
        return flightBookings != null && !flightBookings.isEmpty();
    }

    public int getBookingCount() {
        return flightBookings != null ? flightBookings.size() : 0;
    }
}
