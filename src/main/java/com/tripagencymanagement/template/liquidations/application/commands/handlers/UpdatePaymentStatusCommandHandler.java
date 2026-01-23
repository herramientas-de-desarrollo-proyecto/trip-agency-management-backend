package com.tripagencymanagement.template.liquidations.application.commands.handlers;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.tripagencymanagement.template.liquidations.application.commands.UpdatePaymentStatusCommand;
import com.tripagencymanagement.template.liquidations.domain.entities.DLiquidation;
import com.tripagencymanagement.template.liquidations.domain.entities.DPayment;
import com.tripagencymanagement.template.liquidations.domain.enums.DPaymentStatus;
import com.tripagencymanagement.template.liquidations.domain.repositories.ILiquidationRepository;

@Service
@Transactional
public class UpdatePaymentStatusCommandHandler {

    private final ILiquidationRepository liquidationRepository;

    // Tolerance for floating point comparison (0.01 = 1 centavo)
    private static final float PAYMENT_TOLERANCE = 0.01f;

    public UpdatePaymentStatusCommandHandler(ILiquidationRepository liquidationRepository) {
        this.liquidationRepository = liquidationRepository;
    }

    public DLiquidation execute(UpdatePaymentStatusCommand command) {
        DLiquidation liquidation = liquidationRepository.findById(command.liquidationId())
                .orElseThrow(() -> new IllegalArgumentException(
                        "Liquidación no encontrada con ID: " + command.liquidationId()));

        DPaymentStatus currentStatus = liquidation.getPaymentStatus();
        DPaymentStatus targetStatus = command.targetStatus();

        // Validate state transition
        validateStateTransition(currentStatus, targetStatus, liquidation);

        // Apply the transition
        liquidation.setPaymentStatus(targetStatus);

        return liquidationRepository.save(liquidation);
    }

    private void validateStateTransition(DPaymentStatus current, DPaymentStatus target, DLiquidation liquidation) {
        // Same state - no transition needed
        if (current == target) {
            throw new IllegalStateException("El estado de pago ya se encuentra en: " + target);
        }

        switch (current) {
            case PENDING:
                // From PENDING can go to ON_COURSE (when first payment is made)
                if (target != DPaymentStatus.ON_COURSE) {
                    throw new IllegalStateException(
                            "Desde el estado PENDIENTE solo se puede pasar a EN CURSO");
                }
                // Validate there's at least one valid payment
                if (!hasValidPayments(liquidation)) {
                    throw new IllegalStateException(
                            "Debe haber al menos un pago válido para cambiar a EN CURSO");
                }
                break;

            case ON_COURSE:
                // From ON_COURSE can go to COMPLETED or back to PENDING
                if (target != DPaymentStatus.COMPLETED && target != DPaymentStatus.PENDING) {
                    throw new IllegalStateException(
                            "Desde el estado EN CURSO solo se puede pasar a COMPLETADO o volver a PENDIENTE");
                }
                // To COMPLETED, validate all amounts are paid
                if (target == DPaymentStatus.COMPLETED) {
                    validatePaymentsComplete(liquidation);
                }
                break;

            case COMPLETED:
                // From COMPLETED can go back to ON_COURSE (if payment is invalidated)
                if (target != DPaymentStatus.ON_COURSE) {
                    throw new IllegalStateException(
                            "Desde el estado COMPLETADO solo se puede volver a EN CURSO");
                }
                break;
        }
    }

    private boolean hasValidPayments(DLiquidation liquidation) {
        if (liquidation.getPayments() == null || liquidation.getPayments().isEmpty()) {
            return false;
        }
        return liquidation.getPayments().stream()
                .anyMatch(DPayment::isValid);
    }

    private void validatePaymentsComplete(DLiquidation liquidation) {
        // Calculate total paid separated by currency
        float totalPaidPEN = 0f;
        float totalPaidUSD = 0f;

        if (liquidation.getPayments() != null) {
            for (DPayment payment : liquidation.getPayments()) {
                if (payment.isValid()) {
                    // Check payment currency
                    if (payment.getCurrency() != null && 
                        payment.getCurrency() == com.tripagencymanagement.template.liquidations.domain.enums.DCurrency.USD) {
                        totalPaidUSD += payment.getAmount();
                    } else {
                        // Default to PEN if currency is null
                        totalPaidPEN += payment.getAmount();
                    }
                }
            }
        }

        // Get expected totals
        float expectedPEN = liquidation.getTotalAmount();
        Float expectedUSD = liquidation.getTotalAmountUSD();

        // Validate PEN payments
        if (Math.abs(totalPaidPEN - expectedPEN) > PAYMENT_TOLERANCE) {
            throw new IllegalStateException(String.format(
                    "El total pagado en PEN (%.2f) no coincide con el total de la liquidación (%.2f). " +
                    "Diferencia: %.2f PEN",
                    totalPaidPEN, expectedPEN, expectedPEN - totalPaidPEN));
        }

        // Validate USD payments (if there are USD services)
        if (expectedUSD != null && expectedUSD > 0) {
            if (Math.abs(totalPaidUSD - expectedUSD) > PAYMENT_TOLERANCE) {
                throw new IllegalStateException(String.format(
                        "El total pagado en USD (%.2f) no coincide con el total de la liquidación (%.2f). " +
                        "Diferencia: %.2f USD",
                        totalPaidUSD, expectedUSD, expectedUSD - totalPaidUSD));
            }
        }
    }
}
