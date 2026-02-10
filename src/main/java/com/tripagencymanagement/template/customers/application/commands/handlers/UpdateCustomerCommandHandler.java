package com.tripagency.ptc.ptcagencydemo.customers.application.commands.handlers;

import java.util.Optional;

import org.springframework.stereotype.Service;

import com.tripagency.ptc.ptcagencydemo.customers.application.commands.UpdateCustomerCommand;
import com.tripagency.ptc.ptcagencydemo.customers.domain.entities.DCustomer;
import com.tripagency.ptc.ptcagencydemo.customers.domain.repositories.ICustomerRepository;
import com.tripagency.ptc.ptcagencydemo.customers.presentation.dto.UpdateCustomerDto;
import com.tripagency.ptc.ptcagencydemo.general.utils.exceptions.HtpExceptionUtils;

import jakarta.transaction.Transactional;

@Service
public class UpdateCustomerCommandHandler {
    private final ICustomerRepository customerRepository;

    public UpdateCustomerCommandHandler(ICustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    @Transactional
    public DCustomer execute(UpdateCustomerCommand command) {
        try {
            DCustomer existingCustomer = customerRepository.findById(command.customerId());
            if (existingCustomer == null) {
                throw new IllegalArgumentException("No existe un cliente con el ID: " + command.customerId());
            }

            UpdateCustomerDto dto = command.dto();

            if (dto.getFirstName() != null) {
                existingCustomer.setFirstName(dto.getFirstName());
            }

            if (dto.getLastName() != null) {
                existingCustomer.setLastName(dto.getLastName());
            }

            if (dto.getEmail() != null) {
                existingCustomer.setEmail(Optional.of(dto.getEmail()));
            }

            if (dto.getPhoneNumber() != null) {
                existingCustomer.setPhoneNumber(Optional.of(dto.getPhoneNumber()));
            }

            if (dto.getBirthDate() != null) {
                existingCustomer.setBirthDate(dto.getBirthDate());
            }

            if (dto.getIdDocumentType() != null) {
                existingCustomer.setIdDocumentType(dto.getIdDocumentType());
            }

            if (dto.getIdDocumentNumber() != null) {
                existingCustomer.setIdDocumentNumber(dto.getIdDocumentNumber());
            }

            if (dto.getAddress() != null) {
                existingCustomer.setAddress(Optional.of(dto.getAddress()));
            }

            if (dto.getNationality() != null) {
                existingCustomer.setNationality(Optional.of(dto.getNationality()));
            }

            return customerRepository.update(existingCustomer);
        } catch (Exception e) {
            throw HtpExceptionUtils.processHttpException(e);
        }
    }
}
