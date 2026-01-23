package com.tripagencymanagement.template.liquidations.application.commands.handlers;

import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.tripagencymanagement.template.liquidations.application.commands.AddPaymentCommand;
import com.tripagencymanagement.template.liquidations.application.events.PaymentAddedDomainEvent;
import com.tripagencymanagement.template.liquidations.domain.entities.DLiquidation;
import com.tripagencymanagement.template.liquidations.domain.entities.DPayment;
import com.tripagencymanagement.template.liquidations.domain.repositories.ILiquidationRepository;

@Service
public class AddPaymentCommandHandler {
    
    private final ILiquidationRepository liquidationRepository;
    private final ApplicationEventPublisher eventPublisher;
    
    public AddPaymentCommandHandler(
            ILiquidationRepository liquidationRepository,
            ApplicationEventPublisher eventPublisher) {
        this.liquidationRepository = liquidationRepository;
        this.eventPublisher = eventPublisher;
    }
    
    @Transactional
    public DLiquidation execute(AddPaymentCommand command) {
        DLiquidation liquidation = liquidationRepository.findById(command.liquidationId())
            .orElseThrow(() -> new IllegalArgumentException("La liquidación no fue encontrada con id: " + command.liquidationId()));
        
        DPayment payment = new DPayment(
            command.paymentMethod(),
            command.amount(),
            command.currency(),
            command.liquidationId(),
            command.evidenceUrl()
        );
        
        liquidation.addPayment(payment);
        DLiquidation savedLiquidation = liquidationRepository.save(liquidation);

        // Generar identificador de archivo
        String file = String.format("#%06d", command.liquidationId());

        // Publicar evento de dominio
        eventPublisher.publishEvent(new PaymentAddedDomainEvent(
            command.liquidationId(),
            file,
            command.paymentMethod().name(),
            command.currency().name(),
            (double) command.amount(),
            liquidation.getStaffId()
        ));
        
        return savedLiquidation;
    }
}
