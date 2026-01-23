package com.tripagencymanagement.template.liquidations.application.commands.handlers;

import org.springframework.stereotype.Service;

import com.tripagencymanagement.template.general.utils.exceptions.HtpExceptionUtils;
import com.tripagencymanagement.template.liquidations.application.commands.DeactivateFlightBookingCommand;
import com.tripagencymanagement.template.liquidations.infrastructure.entities.FlightBooking;
import com.tripagencymanagement.template.liquidations.infrastructure.repositories.interfaces.IFlightBookingJpaRepository;
import com.tripagencymanagement.template.liquidations.infrastructure.repositories.interfaces.ILiquidationJpaRepository;

import jakarta.transaction.Transactional;

@Service
public class DeactivateFlightBookingCommandHandler {
    private final IFlightBookingJpaRepository flightBookingJpaRepository;
    private final ILiquidationJpaRepository liquidationJpaRepository;

    public DeactivateFlightBookingCommandHandler(IFlightBookingJpaRepository flightBookingJpaRepository,
                                                  ILiquidationJpaRepository liquidationJpaRepository) {
        this.flightBookingJpaRepository = flightBookingJpaRepository;
        this.liquidationJpaRepository = liquidationJpaRepository;
    }

    @Transactional
    public FlightBooking execute(DeactivateFlightBookingCommand command) {
        try {
            // Verify liquidation exists
            if (!liquidationJpaRepository.existsById(command.liquidationId())) {
                throw new IllegalArgumentException("No existe una liquidación con el ID: " + command.liquidationId());
            }

            FlightBooking existingBooking = flightBookingJpaRepository.findById(command.flightBookingId())
                .orElseThrow(() -> new IllegalArgumentException("No existe una reserva de vuelo con el ID: " + command.flightBookingId()));

            existingBooking.setIsActive(false);
            return flightBookingJpaRepository.save(existingBooking);
        } catch (Exception e) {
            throw HtpExceptionUtils.processHttpException(e);
        }
    }
}
