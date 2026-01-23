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
@Table(name = "hotel_bookings")
@Getter
@Setter
@NoArgsConstructor
public class HotelBooking extends BaseAbstractEntity {

    @NotNull(message = "Check-in date is required")
    @Column(name = "check_in", nullable = false)
    private LocalDateTime checkIn;

    @NotNull(message = "Check-out date is required")
    @Column(name = "check_out", nullable = false)
    private LocalDateTime checkOut;

    @NotBlank(message = "Hotel name is required")
    @Column(nullable = false)
    private String hotel;

    @NotBlank(message = "Room is required")
    @Column(nullable = false)
    private String room;

    @Column(name = "room_description")
    private String roomDescription;

    @PositiveOrZero(message = "Price by night cannot be negative")
    @Column(name = "price_by_night", nullable = false)
    private float priceByNight;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Currency currency;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ServiceStatus status;

    @Column(name = "hotel_service_id", nullable = false, insertable = false, updatable = false)
    private Long hotelServiceId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "hotel_service_id")
    private HotelService hotelService;
}
