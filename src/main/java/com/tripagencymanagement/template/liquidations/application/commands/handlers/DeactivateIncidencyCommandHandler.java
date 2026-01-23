package com.tripagencymanagement.template.liquidations.application.commands.handlers;

import org.springframework.stereotype.Service;

import com.tripagencymanagement.template.general.utils.exceptions.HtpExceptionUtils;
import com.tripagencymanagement.template.liquidations.application.commands.DeactivateIncidencyCommand;
import com.tripagencymanagement.template.liquidations.infrastructure.entities.Incidency;
import com.tripagencymanagement.template.liquidations.infrastructure.repositories.interfaces.IIncidencyJpaRepository;
import com.tripagencymanagement.template.liquidations.infrastructure.repositories.interfaces.ILiquidationJpaRepository;

import jakarta.transaction.Transactional;

@Service
public class DeactivateIncidencyCommandHandler {
    private final IIncidencyJpaRepository incidencyJpaRepository;
    private final ILiquidationJpaRepository liquidationJpaRepository;

    public DeactivateIncidencyCommandHandler(IIncidencyJpaRepository incidencyJpaRepository,
                                             ILiquidationJpaRepository liquidationJpaRepository) {
        this.incidencyJpaRepository = incidencyJpaRepository;
        this.liquidationJpaRepository = liquidationJpaRepository;
    }

    @Transactional
    public Incidency execute(DeactivateIncidencyCommand command) {
        try {
            // Verify liquidation exists
            if (!liquidationJpaRepository.existsById(command.liquidationId())) {
                throw new IllegalArgumentException("No existe una liquidación con el ID: " + command.liquidationId());
            }

            Incidency existingIncidency = incidencyJpaRepository.findById(command.incidencyId())
                .orElseThrow(() -> new IllegalArgumentException("No existe una incidencia con el ID: " + command.incidencyId()));

            existingIncidency.setIsActive(false);
            return incidencyJpaRepository.save(existingIncidency);
        } catch (Exception e) {
            throw HtpExceptionUtils.processHttpException(e);
        }
    }
}
