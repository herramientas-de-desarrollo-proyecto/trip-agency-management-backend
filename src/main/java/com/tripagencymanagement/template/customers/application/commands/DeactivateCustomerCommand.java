package com.tripagency.ptc.ptcagencydemo.customers.application.commands;

import org.jmolecules.architecture.cqrs.Command;

@Command
public record DeactivateCustomerCommand(Long customerId) {
}
