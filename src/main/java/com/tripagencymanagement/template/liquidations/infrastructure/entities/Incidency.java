package com.tripagencymanagement.template.liquidations.infrastructure.entities;

import com.tripagencymanagement.template.general.entities.repositoryEntites.BaseAbstractEntity;
import com.tripagencymanagement.template.liquidations.infrastructure.enums.IncidencyStatus;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "incidencies")
@Getter
@Setter
@NoArgsConstructor
public class Incidency extends BaseAbstractEntity {
    
    @NotBlank(message = "Reason is required")
    @Column(nullable = false, length = 500)
    private String reason;
    
    @PositiveOrZero(message = "Amount cannot be negative")
    @Column
    private Float amount;
    
    @NotNull(message = "Incidency date is required")
    @Column(name = "incidency_date", nullable = false)
    private LocalDateTime incidencyDate;
    
    @Enumerated(EnumType.STRING)
    @Column(name = "incidency_status", nullable = false)
    private IncidencyStatus incidencyStatus;
    
    @NotNull(message = "Liquidation ID is required")
    @Column(name = "liquidation_id", nullable = false)
    private Long liquidationId;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "liquidation_id", insertable = false, updatable = false)
    private Liquidation liquidation;
}
