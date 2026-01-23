package com.tripagencymanagement.template.liquidations.application.commands.handlers;

import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.tripagencymanagement.template.liquidations.application.commands.UpdateLiquidationStatusCommand;
import com.tripagencymanagement.template.liquidations.application.events.LiquidationStatusChangedDomainEvent;
import com.tripagencymanagement.template.liquidations.domain.entities.DLiquidation;
import com.tripagencymanagement.template.liquidations.domain.enums.DLiquidationStatus;
import com.tripagencymanagement.template.liquidations.domain.enums.DPaymentStatus;
import com.tripagencymanagement.template.liquidations.domain.repositories.ILiquidationRepository;

@Service
@Transactional
public class UpdateLiquidationStatusCommandHandler {

    private final ILiquidationRepository liquidationRepository;
    private final ApplicationEventPublisher eventPublisher;

    public UpdateLiquidationStatusCommandHandler(
            ILiquidationRepository liquidationRepository,
            ApplicationEventPublisher eventPublisher) {
        this.liquidationRepository = liquidationRepository;
        this.eventPublisher = eventPublisher;
    }

    public DLiquidation execute(UpdateLiquidationStatusCommand command) {
        DLiquidation liquidation = liquidationRepository.findById(command.liquidationId())
                .orElseThrow(() -> new IllegalArgumentException(
                        "Liquidación no encontrada con ID: " + command.liquidationId()));

        DLiquidationStatus currentStatus = liquidation.getStatus();
        DLiquidationStatus targetStatus = command.targetStatus();

        // Validate state transition
        validateStateTransition(currentStatus, targetStatus, liquidation);

        // Apply the transition
        liquidation.setStatus(targetStatus);

        DLiquidation savedLiquidation = liquidationRepository.save(liquidation);

        // Generar identificador de archivo
        String file = String.format("#%06d", command.liquidationId());

        // Publicar evento de dominio
        eventPublisher.publishEvent(new LiquidationStatusChangedDomainEvent(
            command.liquidationId(),
            file,
            currentStatus.name(),
            targetStatus.name(),
            liquidation.getStaffId()
        ));

        return savedLiquidation;
    }

    private void validateStateTransition(DLiquidationStatus current, DLiquidationStatus target, DLiquidation liquidation) {
        // Same state - no transition needed
        if (current == target) {
            throw new IllegalStateException("La liquidación ya se encuentra en el estado: " + target);
        }

        switch (current) {
            case IN_QUOTE:
                // From IN_QUOTE can go to PENDING or ON_COURSE
                if (target != DLiquidationStatus.PENDING && target != DLiquidationStatus.ON_COURSE) {
                    throw new IllegalStateException(
                            "Desde el estado EN COTIZACIÓN solo se puede pasar a PENDIENTE o EN CURSO");
                }
                // Validate has services before leaving IN_QUOTE
                if (!liquidation.hasServices()) {
                    throw new IllegalStateException(
                            "No se puede cambiar el estado sin servicios registrados en la liquidación");
                }
                break;

            case PENDING:
                // From PENDING can go to ON_COURSE or back to IN_QUOTE
                if (target != DLiquidationStatus.ON_COURSE && target != DLiquidationStatus.IN_QUOTE) {
                    throw new IllegalStateException(
                            "Desde el estado PENDIENTE solo se puede pasar a EN CURSO o volver a EN COTIZACIÓN");
                }
                break;

            case ON_COURSE:
                // From ON_COURSE can go to COMPLETED or back to PENDING
                if (target != DLiquidationStatus.COMPLETED && target != DLiquidationStatus.PENDING) {
                    throw new IllegalStateException(
                            "Desde el estado EN CURSO solo se puede pasar a COMPLETADO o volver a PENDIENTE");
                }
                // To COMPLETED, payment status must be COMPLETED
                if (target == DLiquidationStatus.COMPLETED) {
                    if (liquidation.getPaymentStatus() != DPaymentStatus.COMPLETED) {
                        throw new IllegalStateException(
                                "No se puede completar la liquidación sin que los pagos estén completados");
                    }
                }
                break;

            case COMPLETED:
                // From COMPLETED cannot go anywhere (final state)
                throw new IllegalStateException(
                        "No se puede cambiar el estado de una liquidación completada");
        }
    }
}
