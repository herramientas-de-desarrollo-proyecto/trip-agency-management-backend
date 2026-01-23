package com.tripagencymanagement.template.liquidations.infrastructure.entities;

import java.time.LocalDateTime;

import com.tripagencymanagement.template.general.entities.repositoryEntites.BaseAbstractEntity;
import com.tripagencymanagement.template.liquidations.infrastructure.enums.ServiceStatus;
import com.tripagencymanagement.template.users.infrastructure.enums.Currency;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "flight_bookings")
@Getter
@Setter
@NoArgsConstructor
public class FlightBooking extends BaseAbstractEntity {

    @NotBlank(message = "Origin is required")
    @Column(nullable = false)
    private String origin;

    @NotBlank(message = "Destiny is required")
    @Column(nullable = false)
    private String destiny;

    @NotNull(message = "Departure date is required")
    @Column(name = "departure_date", nullable = false)
    private LocalDateTime departureDate;

    @NotNull(message = "Arrival date is required")
    @Column(name = "arrival_date", nullable = false)
    private LocalDateTime arrivalDate;

    @NotBlank(message = "Aeroline is required")
    @Column(nullable = false)
    private String aeroline;

    @NotBlank(message = "Aeroline booking code is required")
    @Column(name = "aeroline_booking_code", nullable = false)
    private String aerolineBookingCode;

    @Column(name = "costamar_booking_code")
    private String costamarBookingCode;

    @NotBlank(message = "Ticket numbers are required")
    @Column(name = "tkt_numbers", nullable = false)
    private String tktNumbers;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ServiceStatus status;

    @PositiveOrZero(message = "Total price cannot be negative")
    @Column(name = "total_price", nullable = false)
    private float totalPrice;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Currency currency;

    @Column(name = "flight_service_id", nullable = false, insertable = false, updatable = false)
    private Long flightServiceId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "flight_service_id")
    private FlightService flightService;
}
