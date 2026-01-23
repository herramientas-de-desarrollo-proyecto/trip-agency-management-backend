package com.tripagencymanagement.template.liquidations.application.events;

import org.jmolecules.event.annotation.DomainEvent;

@DomainEvent
public record IncidencyAddedDomainEvent(
        Long liquidationId,
        Long staffId,
        String reason,
        String message) {
}
