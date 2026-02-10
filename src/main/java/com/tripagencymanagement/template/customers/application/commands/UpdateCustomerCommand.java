package com.tripagency.ptc.ptcagencydemo.customers.application.commands;

import org.jmolecules.architecture.cqrs.Command;

import com.tripagency.ptc.ptcagencydemo.customers.presentation.dto.UpdateCustomerDto;

@Command
public record UpdateCustomerCommand(Long customerId, UpdateCustomerDto dto) {
}
