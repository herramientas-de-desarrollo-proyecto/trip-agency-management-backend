package com.tripagency.ptc.ptcagencydemo.customers.application.commands.handlers;

import org.springframework.stereotype.Service;

import com.tripagency.ptc.ptcagencydemo.customers.application.commands.DeactivateCustomerCommand;
import com.tripagency.ptc.ptcagencydemo.customers.domain.entities.DCustomer;
import com.tripagency.ptc.ptcagencydemo.customers.domain.repositories.ICustomerRepository;
import com.tripagency.ptc.ptcagencydemo.general.utils.exceptions.HtpExceptionUtils;

import jakarta.transaction.Transactional;

@Service
public class DeactivateCustomerCommandHandler {
    private final ICustomerRepository customerRepository;

    public DeactivateCustomerCommandHandler(ICustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    @Transactional
    public DCustomer execute(DeactivateCustomerCommand command) {
        try {
            DCustomer existingCustomer = customerRepository.findById(command.customerId());
            if (existingCustomer == null) {
                throw new IllegalArgumentException("No existe un cliente con el ID: " + command.customerId());
            }

            existingCustomer.setIsActive(false);
            return customerRepository.update(existingCustomer);
        } catch (Exception e) {
            throw HtpExceptionUtils.processHttpException(e);
        }
    }
}
