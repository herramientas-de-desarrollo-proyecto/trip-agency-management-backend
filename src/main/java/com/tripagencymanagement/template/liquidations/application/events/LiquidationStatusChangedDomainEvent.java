package com.tripagencymanagement.template.liquidations.application.events;

import org.jmolecules.event.annotation.DomainEvent;

/**
 * Evento emitido cuando se actualiza el estado de una liquidación
 */
@DomainEvent
public record LiquidationStatusChangedDomainEvent(
    Long liquidationId,
    String file,
    String oldStatus,
    String newStatus,
    Long triggerUserId
) {}
