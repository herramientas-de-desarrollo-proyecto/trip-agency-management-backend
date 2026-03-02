package com.tripagencymanagement.template.liquidations.application.commands.handlers;

import org.springframework.stereotype.Service;

import com.tripagencymanagement.template.general.utils.exceptions.HtpExceptionUtils;
import com.tripagencymanagement.template.liquidations.application.commands.DeactivateAdditionalServiceCommand;
import com.tripagencymanagement.template.liquidations.application.services.LiquidationTotalsService;
import com.tripagencymanagement.template.liquidations.infrastructure.entities.AdditionalServices;
import com.tripagencymanagement.template.liquidations.infrastructure.repositories.interfaces.IAdditionalServicesJpaRepository;
import com.tripagencymanagement.template.liquidations.infrastructure.repositories.interfaces.ILiquidationJpaRepository;

import jakarta.transaction.Transactional;

@Service
public class DeactivateAdditionalServiceCommandHandler {
    private final IAdditionalServicesJpaRepository additionalServicesJpaRepository;
    private final ILiquidationJpaRepository liquidationJpaRepository;
    private final LiquidationTotalsService liquidationTotalsService;

    public DeactivateAdditionalServiceCommandHandler(IAdditionalServicesJpaRepository additionalServicesJpaRepository,
                                                      ILiquidationJpaRepository liquidationJpaRepository,
                                                      LiquidationTotalsService liquidationTotalsService) {
        this.additionalServicesJpaRepository = additionalServicesJpaRepository;
        this.liquidationJpaRepository = liquidationJpaRepository;
        this.liquidationTotalsService = liquidationTotalsService;
    }

    @Transactional
    public AdditionalServices execute(DeactivateAdditionalServiceCommand command) {
        try {
            // Verify liquidation exists
            if (!liquidationJpaRepository.existsById(command.liquidationId())) {
                throw new IllegalArgumentException("No existe una liquidación con el ID: " + command.liquidationId());
            }

            AdditionalServices existingService = additionalServicesJpaRepository.findById(command.additionalServiceId())
                .orElseThrow(() -> new IllegalArgumentException("No existe un servicio adicional con el ID: " + command.additionalServiceId()));

            existingService.setIsActive(false);
            AdditionalServices saved = additionalServicesJpaRepository.save(existingService);

            liquidationTotalsService.recalculateAndSaveTotals(command.liquidationId());

            return saved;
        } catch (Exception e) {
            throw HtpExceptionUtils.processHttpException(e);
        }
    }
}
