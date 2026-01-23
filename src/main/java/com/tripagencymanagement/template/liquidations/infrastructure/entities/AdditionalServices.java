package com.tripagencymanagement.template.liquidations.infrastructure.entities;

import com.tripagencymanagement.template.general.entities.repositoryEntites.BaseAbstractEntity;
import com.tripagencymanagement.template.liquidations.infrastructure.enums.ServiceStatus;
import com.tripagencymanagement.template.users.infrastructure.enums.Currency;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "additional_services")
@Getter
@Setter
@NoArgsConstructor
public class AdditionalServices extends BaseAbstractEntity {
    
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
    
    @PositiveOrZero(message = "Price cannot be negative")
    @Column(nullable = false)
    private float price;
    
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ServiceStatus status;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "liquidation_id", insertable = false, updatable = false)
    private Liquidation liquidation;
}
