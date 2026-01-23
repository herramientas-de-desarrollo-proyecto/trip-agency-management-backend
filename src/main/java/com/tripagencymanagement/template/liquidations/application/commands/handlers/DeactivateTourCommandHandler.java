package com.tripagencymanagement.template.liquidations.application.commands.handlers;

import org.springframework.stereotype.Service;

import com.tripagencymanagement.template.general.utils.exceptions.HtpExceptionUtils;
import com.tripagencymanagement.template.liquidations.application.commands.DeactivateTourCommand;
import com.tripagencymanagement.template.liquidations.infrastructure.entities.Tour;
import com.tripagencymanagement.template.liquidations.infrastructure.repositories.interfaces.ILiquidationJpaRepository;
import com.tripagencymanagement.template.liquidations.infrastructure.repositories.interfaces.ITourJpaRepository;

import jakarta.transaction.Transactional;

@Service
public class DeactivateTourCommandHandler {
    private final ITourJpaRepository tourJpaRepository;
    private final ILiquidationJpaRepository liquidationJpaRepository;

    public DeactivateTourCommandHandler(ITourJpaRepository tourJpaRepository,
                                        ILiquidationJpaRepository liquidationJpaRepository) {
        this.tourJpaRepository = tourJpaRepository;
        this.liquidationJpaRepository = liquidationJpaRepository;
    }

    @Transactional
    public Tour execute(DeactivateTourCommand command) {
        try {
            // Verify liquidation exists
            if (!liquidationJpaRepository.existsById(command.liquidationId())) {
                throw new IllegalArgumentException("No existe una liquidación con el ID: " + command.liquidationId());
            }

            Tour existingTour = tourJpaRepository.findById(command.tourId())
                .orElseThrow(() -> new IllegalArgumentException("No existe un tour con el ID: " + command.tourId()));

            existingTour.setIsActive(false);
            return tourJpaRepository.save(existingTour);
        } catch (Exception e) {
            throw HtpExceptionUtils.processHttpException(e);
        }
    }
}
