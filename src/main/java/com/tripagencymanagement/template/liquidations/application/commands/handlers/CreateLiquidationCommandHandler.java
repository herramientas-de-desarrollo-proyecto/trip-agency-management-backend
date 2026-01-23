package com.tripagencymanagement.template.liquidations.application.commands.handlers;

import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.tripagencymanagement.template.customers.domain.entities.DCustomer;
import com.tripagencymanagement.template.customers.domain.repositories.ICustomerRepository;
import com.tripagencymanagement.template.liquidations.application.commands.CreateLiquidationCommand;
import com.tripagencymanagement.template.liquidations.application.events.LiquidationCreatedDomainEvent;
import com.tripagencymanagement.template.liquidations.domain.entities.DLiquidation;
import com.tripagencymanagement.template.liquidations.domain.repositories.ILiquidationRepository;

@Service
public class CreateLiquidationCommandHandler {

    private final ILiquidationRepository liquidationRepository;
    private final ICustomerRepository customerRepository;
    private final ApplicationEventPublisher eventPublisher;

    public CreateLiquidationCommandHandler(
            ILiquidationRepository liquidationRepository,
            ICustomerRepository customerRepository,
            ApplicationEventPublisher eventPublisher) {
        this.liquidationRepository = liquidationRepository;
        this.customerRepository = customerRepository;
        this.eventPublisher = eventPublisher;
    }

    @Transactional
    public DLiquidation execute(CreateLiquidationCommand command) {
        DLiquidation liquidation = new DLiquidation(
                command.currencyRate(),
                command.paymentDeadline(),
                command.companion(),
                command.customerId(),
                command.staffId());

        DLiquidation savedLiquidation = liquidationRepository.save(liquidation);

        // Obtener nombre del cliente para la notificación
        DCustomer customer = customerRepository.findById(command.customerId());
        String customerName = customer != null 
                ? customer.getFirstName() + " " + customer.getLastName()
                : "Cliente #" + command.customerId();

        // Generar el identificador de archivo
        String file = String.format("#%06d", savedLiquidation.getId());

        // Publicar evento de dominio
        eventPublisher.publishEvent(new LiquidationCreatedDomainEvent(
                savedLiquidation.getId(),
                file,
                customerName,
                command.staffId()
        ));

        return savedLiquidation;
    }
}
