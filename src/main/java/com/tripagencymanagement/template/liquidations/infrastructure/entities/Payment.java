package com.tripagencymanagement.template.liquidations.infrastructure.entities;

import com.tripagencymanagement.template.general.entities.repositoryEntites.BaseAbstractEntity;
import com.tripagencymanagement.template.liquidations.infrastructure.enums.Currency;
import com.tripagencymanagement.template.liquidations.infrastructure.enums.PaymentMethod;
import com.tripagencymanagement.template.liquidations.infrastructure.enums.PaymentValidity;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "payments")
@Getter
@Setter
@NoArgsConstructor
public class Payment extends BaseAbstractEntity {

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private PaymentMethod method;

    @Positive(message = "Amount must be greater than zero")
    @Column(nullable = false)
    private float amount;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Currency currency = Currency.PEN;

    @NotNull(message = "Liquidation ID is required")
    @Column(name = "liquidation_id", nullable = false)
    private Long liquidationId;

    @Enumerated(EnumType.STRING)
    @Column(name = "validation_status", nullable = false)
    private PaymentValidity validationStatus;

    @Column(name = "evidence_url")
    private String evidenceUrl;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "liquidation_id", insertable = false, updatable = false)
    private Liquidation liquidation;
}
