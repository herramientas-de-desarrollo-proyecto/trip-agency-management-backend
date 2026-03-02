package com.tripagencymanagement.template.liquidations.application.commands.handlers;

import org.springframework.stereotype.Service;

import com.tripagencymanagement.template.general.utils.exceptions.HtpExceptionUtils;
import com.tripagencymanagement.template.liquidations.application.commands.DeactivatePaymentCommand;
import com.tripagencymanagement.template.liquidations.application.services.LiquidationTotalsService;
import com.tripagencymanagement.template.liquidations.infrastructure.entities.Payment;
import com.tripagencymanagement.template.liquidations.infrastructure.repositories.interfaces.ILiquidationJpaRepository;
import com.tripagencymanagement.template.liquidations.infrastructure.repositories.interfaces.IPaymentJpaRepository;

import jakarta.transaction.Transactional;

@Service
public class DeactivatePaymentCommandHandler {
    private final IPaymentJpaRepository paymentJpaRepository;
    private final ILiquidationJpaRepository liquidationJpaRepository;
    private final LiquidationTotalsService liquidationTotalsService;

    public DeactivatePaymentCommandHandler(IPaymentJpaRepository paymentJpaRepository,
                                           ILiquidationJpaRepository liquidationJpaRepository,
                                           LiquidationTotalsService liquidationTotalsService) {
        this.paymentJpaRepository = paymentJpaRepository;
        this.liquidationJpaRepository = liquidationJpaRepository;
        this.liquidationTotalsService = liquidationTotalsService;
    }

    @Transactional
    public Payment execute(DeactivatePaymentCommand command) {
        try {
            // Verify liquidation exists
            if (!liquidationJpaRepository.existsById(command.liquidationId())) {
                throw new IllegalArgumentException("No existe una liquidación con el ID: " + command.liquidationId());
            }

            Payment existingPayment = paymentJpaRepository.findById(command.paymentId())
                .orElseThrow(() -> new IllegalArgumentException("No existe un pago con el ID: " + command.paymentId()));

            existingPayment.setIsActive(false);
            Payment saved = paymentJpaRepository.save(existingPayment);

            liquidationTotalsService.recalculateAndSaveTotals(command.liquidationId());

            return saved;
        } catch (Exception e) {
            throw HtpExceptionUtils.processHttpException(e);
        }
    }
}
