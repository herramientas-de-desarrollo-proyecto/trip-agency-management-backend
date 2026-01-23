package com.tripagencymanagement.template.liquidations.domain.entities;

import java.time.LocalDateTime;
import java.util.Optional;

import com.tripagencymanagement.template.general.entities.domainEntities.BaseAbstractDomainEntity;
import com.tripagencymanagement.template.liquidations.domain.enums.DIncidencyStatus;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class DIncidency extends BaseAbstractDomainEntity {
    private String reason;
    private Optional<Float> amount;
    private LocalDateTime incidencyDate;
    private DIncidencyStatus incidencyStatus;
    private Long liquidationId;

    public DIncidency(String reason, Optional<Float> amount, LocalDateTime incidencyDate, Long liquidationId) {
        super();
        validateIncidency(reason, amount, incidencyDate, liquidationId);
        
        this.reason = reason;
        this.amount = amount;
        this.incidencyDate = incidencyDate;
        this.incidencyStatus = DIncidencyStatus.PENDING;
        this.liquidationId = liquidationId;
    }

    private void validateIncidency(String reason, Optional<Float> amount, LocalDateTime incidencyDate, Long liquidationId) {
        if (reason == null || reason.trim().isEmpty()) {
            throw new IllegalArgumentException("El motivo no puede ser nulo o vacío");
        }
        if (amount != null && amount.isPresent() && amount.get() < 0) {
            throw new IllegalArgumentException("El monto no puede ser negativo");
        }
        if (incidencyDate == null) {
            throw new IllegalArgumentException("La fecha de incidencia no puede ser nula");
        }
        if (liquidationId == null) {
            throw new IllegalArgumentException("El ID de liquidación no puede ser nulo");
        }
    }

    public void approve() {
        if (this.incidencyStatus != DIncidencyStatus.PENDING) {
            throw new IllegalStateException("Solo las incidencias pendientes pueden ser aprobadas");
        }
        this.incidencyStatus = DIncidencyStatus.APPROVED;
    }

    public void reject() {
        if (this.incidencyStatus != DIncidencyStatus.PENDING) {
            throw new IllegalStateException("Solo las incidencias pendientes pueden ser rechazadas");
        }
        this.incidencyStatus = DIncidencyStatus.REJECTED;
    }

    public boolean isPending() {
        return this.incidencyStatus == DIncidencyStatus.PENDING;
    }

    public boolean isApproved() {
        return this.incidencyStatus == DIncidencyStatus.APPROVED;
    }
}
