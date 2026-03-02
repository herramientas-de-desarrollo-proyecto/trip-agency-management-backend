package com.tripagencymanagement.template.liquidations.application.commands.handlers;

import org.springframework.stereotype.Service;

import com.tripagencymanagement.template.general.utils.exceptions.HtpExceptionUtils;
import com.tripagencymanagement.template.liquidations.application.commands.DeactivateHotelBookingCommand;
import com.tripagencymanagement.template.liquidations.application.services.LiquidationTotalsService;
import com.tripagencymanagement.template.liquidations.infrastructure.entities.HotelBooking;
import com.tripagencymanagement.template.liquidations.infrastructure.repositories.interfaces.IHotelBookingJpaRepository;
import com.tripagencymanagement.template.liquidations.infrastructure.repositories.interfaces.ILiquidationJpaRepository;

import jakarta.transaction.Transactional;

@Service
public class DeactivateHotelBookingCommandHandler {
    private final IHotelBookingJpaRepository hotelBookingJpaRepository;
    private final ILiquidationJpaRepository liquidationJpaRepository;
    private final LiquidationTotalsService liquidationTotalsService;

    public DeactivateHotelBookingCommandHandler(IHotelBookingJpaRepository hotelBookingJpaRepository,
                                                 ILiquidationJpaRepository liquidationJpaRepository,
                                                 LiquidationTotalsService liquidationTotalsService) {
        this.hotelBookingJpaRepository = hotelBookingJpaRepository;
        this.liquidationJpaRepository = liquidationJpaRepository;
        this.liquidationTotalsService = liquidationTotalsService;
    }

    @Transactional
    public HotelBooking execute(DeactivateHotelBookingCommand command) {
        try {
            // Verify liquidation exists
            if (!liquidationJpaRepository.existsById(command.liquidationId())) {
                throw new IllegalArgumentException("No existe una liquidación con el ID: " + command.liquidationId());
            }

            HotelBooking existingBooking = hotelBookingJpaRepository.findById(command.hotelBookingId())
                .orElseThrow(() -> new IllegalArgumentException("No existe una reserva de hotel con el ID: " + command.hotelBookingId()));

            existingBooking.setIsActive(false);
            HotelBooking saved = hotelBookingJpaRepository.save(existingBooking);

            liquidationTotalsService.recalculateAndSaveTotals(command.liquidationId());

            return saved;
        } catch (Exception e) {
            throw HtpExceptionUtils.processHttpException(e);
        }
    }
}
