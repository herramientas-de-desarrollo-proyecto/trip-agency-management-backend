package com.tripagencymanagement.template.liquidations.domain.entities;

import java.time.LocalDateTime;

import com.tripagencymanagement.template.general.entities.domainEntities.BaseAbstractDomainEntity;
import com.tripagencymanagement.template.liquidations.domain.enums.DServiceStatus;
import com.tripagencymanagement.template.users.domain.enums.DCurrency;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class DTour extends BaseAbstractDomainEntity {
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private String title;
    private float price;
    private String place;
    private DCurrency currency;
    private DServiceStatus status;
    private Long tourServiceId;

    public DTour(LocalDateTime startDate, LocalDateTime endDate, String title,
            float price, String place, DCurrency currency, Long tourServiceId) {
        super();
        validateTour(startDate, endDate, title, price, place, currency, tourServiceId);

        this.startDate = startDate;
        this.endDate = endDate;
        this.title = title;
        this.price = price;
        this.place = place;
        this.currency = currency;
        this.status = DServiceStatus.PENDING;
        this.tourServiceId = tourServiceId;
    }

    private void validateTour(LocalDateTime startDate, LocalDateTime endDate, String title,
            float price, String place, DCurrency currency, Long tourServiceId) {
        if (startDate == null) {
            throw new IllegalArgumentException("La fecha de inicio no puede ser nula");
        }
        if (endDate == null) {
            throw new IllegalArgumentException("La fecha de finalización no puede ser nula");
        }
        if (endDate.isBefore(startDate)) {
            throw new IllegalArgumentException("La fecha de finalización debe ser posterior o igual a la fecha de inicio");
        }
        if (title == null || title.trim().isEmpty()) {
            throw new IllegalArgumentException("El título no puede ser nulo o vacío");
        }
        if (price < 0) {
            throw new IllegalArgumentException("El precio no puede ser negativo");
        }
        if (place == null || place.trim().isEmpty()) {
            throw new IllegalArgumentException("El lugar no puede ser nulo o vacío");
        }
        if (currency == null) {
            throw new IllegalArgumentException("La moneda no puede ser nula");
        }
        // tourServiceId can be null temporarily when creating the tour before
        // persisting
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
