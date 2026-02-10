package com.tripagency.ptc.ptcagencydemo.customers.application.events;

import org.jmolecules.event.annotation.DomainEvent;

@DomainEvent
public record CustomerCreatedDomainEvent(Long customerId, String message) {
    
}
