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
@Table(name = "tours")
@Getter
@Setter
@NoArgsConstructor
public class Tour extends BaseAbstractEntity {

    @NotNull(message = "Start date is required")
    @Column(name = "start_date", nullable = false)
    private LocalDateTime startDate;

    @NotNull(message = "End date is required")
    @Column(name = "end_date", nullable = false)
    private LocalDateTime endDate;

    @NotBlank(message = "Title is required")
    @Column(nullable = false)
    private String title;

    @PositiveOrZero(message = "Price cannot be negative")
    @Column(nullable = false)
    private float price;

    @NotBlank(message = "Place is required")
    @Column(nullable = false)
    private String place;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Currency currency;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ServiceStatus status;

    @Column(name = "tour_service_id", nullable = false, insertable = false, updatable = false)
    private Long tourServiceId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "tour_service_id")
    private TourService tourService;
}
