package com.tripagencymanagement.template.liquidations.domain.entities;

import java.time.LocalDateTime;
import java.util.Optional;

import com.tripagencymanagement.template.general.entities.domainEntities.BaseAbstractDomainEntity;
import com.tripagencymanagement.template.liquidations.domain.enums.DServiceStatus;
import com.tripagencymanagement.template.users.domain.enums.DCurrency;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class DHotelBooking extends BaseAbstractDomainEntity {
    private LocalDateTime checkIn;
    private LocalDateTime checkOut;
    private String hotel;
    private String room;
    private Optional<String> roomDescription;
    private float priceByNight;
    private DCurrency currency;
    private DServiceStatus status;
    private Long hotelServiceId;

    public DHotelBooking(LocalDateTime checkIn, LocalDateTime checkOut, String hotel,
                        String room, Optional<String> roomDescription, float priceByNight,
                        DCurrency currency, Long hotelServiceId) {
        super();
        validateHotelBooking(checkIn, checkOut, hotel, room, priceByNight, currency, hotelServiceId);
        
        this.checkIn = checkIn;
        this.checkOut = checkOut;
        this.hotel = hotel;
        this.room = room;
        this.roomDescription = roomDescription != null ? roomDescription : Optional.empty();
        this.priceByNight = priceByNight;
        this.currency = currency;
        this.status = DServiceStatus.PENDING;
        this.hotelServiceId = hotelServiceId;
    }

    private void validateHotelBooking(LocalDateTime checkIn, LocalDateTime checkOut, String hotel,
                                     String room, float priceByNight, DCurrency currency, Long hotelServiceId) {
        if (checkIn == null) {
            throw new IllegalArgumentException("La fecha de check-in no puede ser nula");
        }
        if (checkOut == null) {
            throw new IllegalArgumentException("La fecha de check-out no puede ser nula");
        }
        if (checkOut.isBefore(checkIn) || checkOut.isEqual(checkIn)) {
            throw new IllegalArgumentException("La fecha de check-out debe ser posterior a la fecha de check-in");
        }
        if (hotel == null || hotel.trim().isEmpty()) {
            throw new IllegalArgumentException("El nombre del hotel no puede ser nulo o vacío");
        }
        if (room == null || room.trim().isEmpty()) {
            throw new IllegalArgumentException("El nombre de la habitación no puede ser nulo o vacío");
        }
        if (priceByNight < 0) {
            throw new IllegalArgumentException("El precio por noche no puede ser negativo");
        }
        if (currency == null) {
            throw new IllegalArgumentException("Currency cannot be null");
        }
        // hotelServiceId can be null temporarily when creating the booking before persisting
    }

    public float calculateTotalPrice() {
        long nights = java.time.Duration.between(checkIn, checkOut).toDays();
        return priceByNight * nights;
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
