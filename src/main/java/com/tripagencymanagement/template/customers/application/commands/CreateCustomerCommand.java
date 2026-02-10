package com.tripagency.ptc.ptcagencydemo.customers.application.commands;

import org.jmolecules.architecture.cqrs.Command;

import com.tripagency.ptc.ptcagencydemo.customers.presentation.dto.CreateCustomerDto;
@Command
public record CreateCustomerCommand(CreateCustomerDto customerDto) {
    
}
