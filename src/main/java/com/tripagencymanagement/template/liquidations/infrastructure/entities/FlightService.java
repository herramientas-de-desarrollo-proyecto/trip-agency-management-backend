package com.tripagencymanagement.template.liquidations.infrastructure.entities;

import java.util.ArrayList;
import java.util.List;

import com.tripagencymanagement.template.general.entities.repositoryEntites.BaseAbstractEntity;
import com.tripagencymanagement.template.users.infrastructure.enums.Currency;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "flight_services")
@Getter
@Setter
@NoArgsConstructor
public class FlightService extends BaseAbstractEntity {

    @PositiveOrZero(message = "Tariff rate cannot be negative")
    @Column(name = "tariff_rate", nullable = false)
    private float tariffRate;

    @Column(name = "is_taxed", nullable = false)
    private boolean isTaxed;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Currency currency;

    @NotNull(message = "Liquidation ID is required")
    @Column(name = "liquidation_id", nullable = false)
    private Long liquidationId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "liquidation_id", insertable = false, updatable = false)
    private Liquidation liquidation;

    @OneToMany(mappedBy = "flightService", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<FlightBooking> flightBookings = new ArrayList<>();

    public void addFlightBooking(FlightBooking booking) {
        flightBookings.add(booking);
        booking.setFlightService(this);
    }
}
