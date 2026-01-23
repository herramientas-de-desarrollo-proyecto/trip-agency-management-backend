package com.tripagencymanagement.template.liquidations.application.events;

import org.jmolecules.event.annotation.DomainEvent;

/**
 * Evento emitido cuando se elimina una liquidación
 */
@DomainEvent
public record LiquidationDeletedDomainEvent(
    Long liquidationId,
    String file,
    Long triggerUserId
) {}
