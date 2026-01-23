package com.tripagencymanagement.template.customers.application.commands;

import org.jmolecules.architecture.cqrs.Command;

import com.tripagencymanagement.template.customers.presentation.dto.CreateCustomerDto;
@Command
public record CreateCustomerCommand(CreateCustomerDto customerDto) {
    
}
