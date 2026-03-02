package com.tripagencymanagement.template.liquidations.domain.entities;

import com.tripagencymanagement.template.users.domain.enums.DCurrency;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class DHotelService extends DBaseAbstractService {
    private List<DHotelBooking> hotelBookings;

    public DHotelService(float tariffRate, boolean isTaxed, DCurrency currency, Long liquidationId) {
        super(tariffRate, isTaxed, currency, liquidationId);
        this.hotelBookings = new ArrayList<>();
    }

    public void addHotelBooking(DHotelBooking hotelBooking) {
        if (hotelBooking == null) {
            throw new IllegalArgumentException("La reserva de hotel no puede ser nula");
        }
        this.hotelBookings.add(hotelBooking);
    }

    public void removeHotelBooking(DHotelBooking hotelBooking) {
        this.hotelBookings.remove(hotelBooking);
    }

    @Override
    public float calculateTotal() {
        float baseTotal = calculateBaseAmount();
        return applyTariffAndTax(baseTotal);
    }

    @Override
    public float calculateBaseAmount() {
        if (hotelBookings == null || hotelBookings.isEmpty()) {
            return 0f;
        }
        
        return hotelBookings.stream()
            .filter(booking -> Boolean.TRUE.equals(booking.getIsActive()))
            .map(DHotelBooking::calculateTotalPrice)
            .reduce(0f, Float::sum);
    }

    public boolean hasBookings() {
        return hotelBookings != null && !hotelBookings.isEmpty();
    }

    public int getBookingCount() {
        return hotelBookings != null ? hotelBookings.size() : 0;
    }
}
