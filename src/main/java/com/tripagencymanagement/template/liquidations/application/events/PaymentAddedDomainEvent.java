package com.tripagencymanagement.template.liquidations.application.events;

import org.jmolecules.event.annotation.DomainEvent;

/**
 * Evento emitido cuando se agrega un pago a una liquidación
 */
@DomainEvent
public record PaymentAddedDomainEvent(
    Long liquidationId,
    String file,
    String paymentMethod,
    String currency,
    Double amount,
    Long triggerUserId
) {}
