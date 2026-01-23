package com.tripagencymanagement.template.liquidations.application.commands.handlers;

import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.tripagencymanagement.template.liquidations.application.commands.AddFlightServiceCommand;
import com.tripagencymanagement.template.liquidations.domain.entities.DFlightBooking;
import com.tripagencymanagement.template.liquidations.domain.entities.DFlightService;
import com.tripagencymanagement.template.liquidations.domain.entities.DLiquidation;
import com.tripagencymanagement.template.liquidations.domain.repositories.ILiquidationRepository;
import com.tripagencymanagement.template.liquidations.presentation.dto.AddFlightServiceDto;
import com.tripagencymanagement.template.liquidations.presentation.dto.FlightBookingDto;
import com.tripagencymanagement.template.users.domain.enums.DCurrency;

@Service
public class AddFlightServiceCommandHandler {
    
    private final ILiquidationRepository liquidationRepository;
    
    public AddFlightServiceCommandHandler(ILiquidationRepository liquidationRepository) {
        this.liquidationRepository = liquidationRepository;
    }
    
    @Transactional
    public DLiquidation execute(AddFlightServiceCommand command) {
        DLiquidation liquidation = liquidationRepository.findById(command.liquidationId())
                .orElseThrow(() -> new IllegalArgumentException("La liquidación no fue encontrada con id: " + command.liquidationId()));
        
        AddFlightServiceDto dto = command.flightServiceDto();
        
        DFlightService flightService = new DFlightService(
                dto.getTariffRate(),
                dto.getIsTaxed(),
                DCurrency.valueOf(dto.getCurrency()),
                command.liquidationId()
        );
        
        for (FlightBookingDto bookingDto : dto.getFlightBookings()) {
            DFlightBooking booking = new DFlightBooking(
                    bookingDto.getOrigin(),
                    bookingDto.getDestiny(),
                    bookingDto.getDepartureDate(),
                    bookingDto.getArrivalDate(),
                    bookingDto.getAeroline(),
                    bookingDto.getAerolineBookingCode(),
                    Optional.ofNullable(bookingDto.getCostamarBookingCode()),
                    bookingDto.getTktNumbers(),
                    bookingDto.getTotalPrice(),
                    DCurrency.valueOf(bookingDto.getCurrency()),
                    null
            );
            flightService.addFlightBooking(booking);
        }
        
        liquidation.addFlightService(flightService);
        
        return liquidationRepository.save(liquidation);
    }
}
