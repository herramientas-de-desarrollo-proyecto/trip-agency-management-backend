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
public class DFlightBooking extends BaseAbstractDomainEntity {
    private String origin;
    private String destiny;
    private LocalDateTime departureDate;
    private LocalDateTime arrivalDate;
    private String aeroline;
    private String aerolineBookingCode;
    private Optional<String> costamarBookingCode;
    private String tktNumbers;
    private DServiceStatus status;
    private float totalPrice;
    private DCurrency currency;
    private Long flightServiceId;

    public DFlightBooking(String origin, String destiny, LocalDateTime departureDate, 
                         LocalDateTime arrivalDate, String aeroline, String aerolineBookingCode,
                         Optional<String> costamarBookingCode, String tktNumbers, 
                         float totalPrice, DCurrency currency, Long flightServiceId) {
        super();
        validateFlightBooking(origin, destiny, departureDate, arrivalDate, aeroline, 
                            aerolineBookingCode, tktNumbers, totalPrice, currency, flightServiceId);
        
        this.origin = origin;
        this.destiny = destiny;
        this.departureDate = departureDate;
        this.arrivalDate = arrivalDate;
        this.aeroline = aeroline;
        this.aerolineBookingCode = aerolineBookingCode;
        this.costamarBookingCode = costamarBookingCode != null ? costamarBookingCode : Optional.empty();
        this.tktNumbers = tktNumbers;
        this.status = DServiceStatus.PENDING;
        this.totalPrice = totalPrice;
        this.currency = currency;
        this.flightServiceId = flightServiceId;
    }

    private void validateFlightBooking(String origin, String destiny, LocalDateTime departureDate,
                                      LocalDateTime arrivalDate, String aeroline, String aerolineBookingCode,
                                      String tktNumbers, float totalPrice, DCurrency currency, Long flightServiceId) {
        if (origin == null || origin.trim().isEmpty()) {
            throw new IllegalArgumentException("El origen no puede ser nulo o vacío");
        }
        if (destiny == null || destiny.trim().isEmpty()) {
            throw new IllegalArgumentException("El destino no puede ser nulo o vacío");
        }
        if (departureDate == null) {
            throw new IllegalArgumentException("La fecha de salida no puede ser nula");
        }
        if (arrivalDate == null) {
            throw new IllegalArgumentException("La fecha de llegada no puede ser nula");
        }
        if (arrivalDate.isBefore(departureDate)) {
            throw new IllegalArgumentException("La fecha de llegada debe ser posterior a la fecha de salida");
        }
        if (aeroline == null || aeroline.trim().isEmpty()) {
            throw new IllegalArgumentException("La aerolínea no puede ser nula o vacía");
        }
        if (aerolineBookingCode == null || aerolineBookingCode.trim().isEmpty()) {
            throw new IllegalArgumentException("El código de reserva de la aerolínea no puede ser nulo o vacío");
        }
        if (tktNumbers == null || tktNumbers.trim().isEmpty()) {
            throw new IllegalArgumentException("Los números de ticket no pueden ser nulos o vacíos");
        }
        if (totalPrice < 0) {
            throw new IllegalArgumentException("El precio total no puede ser negativo");
        }
        if (currency == null) {
            throw new IllegalArgumentException("La moneda no puede ser nula");
        }
        // flightServiceId can be null temporarily when creating the booking before persisting
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
