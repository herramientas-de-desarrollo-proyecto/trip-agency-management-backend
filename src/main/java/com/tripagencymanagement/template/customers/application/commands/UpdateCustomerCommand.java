package com.tripagencymanagement.template.customers.application.commands;

import org.jmolecules.architecture.cqrs.Command;

import com.tripagencymanagement.template.customers.presentation.dto.UpdateCustomerDto;

@Command
public record UpdateCustomerCommand(Long customerId, UpdateCustomerDto dto) {
}
