package com.tripagencymanagement.template.liquidations.infrastructure.entities;

import com.tripagencymanagement.template.customers.infrastructure.entities.Customer;
import com.tripagencymanagement.template.general.entities.repositoryEntites.BaseAbstractEntity;
import com.tripagencymanagement.template.liquidations.infrastructure.enums.LiquidationStatus;
import com.tripagencymanagement.template.liquidations.infrastructure.enums.PaymentStatus;
import com.tripagencymanagement.template.users.infrastructure.entities.Staff;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "liquidations")
@Getter
@Setter
@NoArgsConstructor
public class Liquidation extends BaseAbstractEntity {

    @Positive(message = "Currency rate must be greater than zero")
    @Column(name = "currency_rate", nullable = false)
    private float currencyRate;

    @PositiveOrZero(message = "Total amount cannot be negative")
    @Column(name = "total_amount", nullable = false)
    private float totalAmount;

    @PositiveOrZero(message = "Total amount USD cannot be negative")
    @Column(name = "total_amount_usd", nullable = true)
    private Float totalAmountUSD;

    @PositiveOrZero(message = "Total commission PEN cannot be negative")
    @Column(name = "total_commission_pen", nullable = true)
    private Float totalCommissionPEN;

    @PositiveOrZero(message = "Total commission USD cannot be negative")
    @Column(name = "total_commission_usd", nullable = true)
    private Float totalCommissionUSD;

    @NotNull(message = "Payment deadline is required")
    @Column(name = "payment_deadline", nullable = false)
    private LocalDateTime paymentDeadline;

    @PositiveOrZero(message = "Companion count cannot be negative")
    @Column(nullable = false)
    private int companion;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private LiquidationStatus status;

    @Enumerated(EnumType.STRING)
    @Column(name = "payment_status", nullable = false)
    private PaymentStatus paymentStatus;

    @NotNull(message = "Customer ID is required")
    @Column(name = "customer_id", nullable = false)
    private Long customerId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "customer_id", insertable = false, updatable = false)
    private Customer customer;

    @NotNull(message = "Staff ID is required")
    @Column(name = "staff_id", nullable = false)
    private Long staffId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "staff_id", insertable = false, updatable = false)
    private Staff staffOnCharge;

    @OneToMany(mappedBy = "liquidation", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Payment> payments = new ArrayList<>();

    @OneToMany(mappedBy = "liquidation", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<FlightService> flightServices = new ArrayList<>();

    @OneToMany(mappedBy = "liquidation", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<HotelService> hotelServices = new ArrayList<>();

    @OneToMany(mappedBy = "liquidation", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<TourService> tourServices = new ArrayList<>();

    @OneToMany(mappedBy = "liquidation", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<AdditionalServices> additionalServices = new ArrayList<>();

    @OneToMany(mappedBy = "liquidation", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Incidency> incidencies = new ArrayList<>();
}
