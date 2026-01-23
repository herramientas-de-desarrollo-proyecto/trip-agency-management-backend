package com.tripagencymanagement.template.liquidations.application.events;

import org.jmolecules.event.annotation.DomainEvent;

/**
 * Evento emitido cuando se crea una nueva liquidación
 */
@DomainEvent
public record LiquidationCreatedDomainEvent(
    Long liquidationId,
    String file,
    String customerName,
    Long triggerUserId
) {}
