package com.tripagencymanagement.template.liquidations.application.commands.handlers;

import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

import com.tripagencymanagement.template.general.utils.exceptions.HtpExceptionUtils;
import com.tripagencymanagement.template.liquidations.application.commands.DeactivateLiquidationCommand;
import com.tripagencymanagement.template.liquidations.application.events.LiquidationDeletedDomainEvent;
import com.tripagencymanagement.template.liquidations.infrastructure.entities.Liquidation;
import com.tripagencymanagement.template.liquidations.infrastructure.repositories.interfaces.ILiquidationJpaRepository;

import jakarta.transaction.Transactional;

@Service
public class DeactivateLiquidationCommandHandler {
    private final ILiquidationJpaRepository liquidationJpaRepository;
    private final ApplicationEventPublisher eventPublisher;

    public DeactivateLiquidationCommandHandler(
            ILiquidationJpaRepository liquidationJpaRepository,
            ApplicationEventPublisher eventPublisher) {
        this.liquidationJpaRepository = liquidationJpaRepository;
        this.eventPublisher = eventPublisher;
    }

    @Transactional
    public Liquidation execute(DeactivateLiquidationCommand command) {
        try {
            Liquidation existingLiquidation = liquidationJpaRepository.findById(command.liquidationId())
                .orElseThrow(() -> new IllegalArgumentException("No existe una liquidación con el ID: " + command.liquidationId()));

            existingLiquidation.setIsActive(false);
            Liquidation savedLiquidation = liquidationJpaRepository.save(existingLiquidation);

            // Generar identificador de archivo
            String file = String.format("#%06d", command.liquidationId());

            // Publicar evento de dominio
            eventPublisher.publishEvent(new LiquidationDeletedDomainEvent(
                command.liquidationId(),
                file,
                existingLiquidation.getStaffId()
            ));

            return savedLiquidation;
        } catch (Exception e) {
            throw HtpExceptionUtils.processHttpException(e);
        }
    }
}
