package com.tripagency.ptc.ptcagencydemo.customers.application.commands.handlers;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

import com.tripagency.ptc.ptcagencydemo.customers.application.commands.CreateCustomerCommand;
import com.tripagency.ptc.ptcagencydemo.customers.application.events.CustomerCreatedDomainEvent;
import com.tripagency.ptc.ptcagencydemo.customers.domain.entities.DCustomer;
import com.tripagency.ptc.ptcagencydemo.customers.domain.repositories.ICustomerRepository;
import com.tripagency.ptc.ptcagencydemo.general.utils.exceptions.HtpExceptionUtils;

import jakarta.transaction.Transactional;

@Service
public class CreateCustomerCommandHandler {
    private final ICustomerRepository customerRepository;
    private final ApplicationEventPublisher eventPublisher;

    @Autowired
    public CreateCustomerCommandHandler(ICustomerRepository customerRepository,
            ApplicationEventPublisher eventPublisher) {
        this.customerRepository = customerRepository;
        this.eventPublisher = eventPublisher;
    }

    @Transactional
    public DCustomer execute(CreateCustomerCommand command) {
        try {
            // Validar si el email ya existe (solo si se proporciona email)
            if (command.customerDto().getEmail() != null && command.customerDto().getEmail().isPresent()) {
                String email = command.customerDto().getEmail().get();
                if (customerRepository.existsByEmail(email)) {
                    throw new IllegalArgumentException("Ya existe un customer con el email: " + email);
                }
            }

            // Convertir el DTO a una entidad de dominio
            DCustomer newCustomer = new DCustomer();
            newCustomer.setFirstName(command.customerDto().getFirstName());
            newCustomer.setLastName(command.customerDto().getLastName());
            newCustomer.setEmail(
                    command.customerDto().getEmail() != null ? command.customerDto().getEmail() : Optional.empty());
            newCustomer.setPhoneNumber(
                    command.customerDto().getPhoneNumber() != null ? command.customerDto().getPhoneNumber()
                            : Optional.empty());
            newCustomer.setBirthDate(command.customerDto().getBirthDate());
            newCustomer.setIdDocumentType(command.customerDto().getIdDocumentType());
            newCustomer.setIdDocumentNumber(command.customerDto().getIdDocumentNumber());
            newCustomer.setAddress(
                    command.customerDto().getAddress() != null ? command.customerDto().getAddress() : Optional.empty());
            newCustomer.setNationality(
                    command.customerDto().getNationality() != null ? command.customerDto().getNationality()
                            : Optional.empty());

            // Guardar el customer (solo una vez)
            DCustomer savedCustomer = customerRepository.save(newCustomer);

            eventPublisher
                    .publishEvent(new CustomerCreatedDomainEvent(savedCustomer.getId(), "Se creó un nuevo usuario"));

            return savedCustomer;
        } catch (Exception e) {
            throw HtpExceptionUtils.processHttpException(e);
        }
    }
}
