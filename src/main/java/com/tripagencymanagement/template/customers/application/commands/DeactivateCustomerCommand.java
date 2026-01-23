package com.tripagencymanagement.template.customers.application.commands;

import org.jmolecules.architecture.cqrs.Command;

@Command
public record DeactivateCustomerCommand(Long customerId) {
}
