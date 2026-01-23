package com.tripagencymanagement.template.liquidations.application.commands.handlers;

import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.tripagencymanagement.template.liquidations.application.commands.AddHotelServiceCommand;
import com.tripagencymanagement.template.liquidations.domain.entities.DHotelBooking;
import com.tripagencymanagement.template.liquidations.domain.entities.DHotelService;
import com.tripagencymanagement.template.liquidations.domain.entities.DLiquidation;
import com.tripagencymanagement.template.liquidations.domain.repositories.ILiquidationRepository;
import com.tripagencymanagement.template.liquidations.presentation.dto.AddHotelServiceDto;
import com.tripagencymanagement.template.liquidations.presentation.dto.HotelBookingDto;
import com.tripagencymanagement.template.users.domain.enums.DCurrency;

@Service
public class AddHotelServiceCommandHandler {
    
    private final ILiquidationRepository liquidationRepository;
    
    public AddHotelServiceCommandHandler(ILiquidationRepository liquidationRepository) {
        this.liquidationRepository = liquidationRepository;
    }
    
    @Transactional
    public DLiquidation execute(AddHotelServiceCommand command) {
        DLiquidation liquidation = liquidationRepository.findById(command.liquidationId())
                .orElseThrow(() -> new IllegalArgumentException("La liquidación no fue encontrada con id: " + command.liquidationId()));
        
        AddHotelServiceDto dto = command.hotelServiceDto();
        
        DHotelService hotelService = new DHotelService(
                dto.getTariffRate(),
                dto.getIsTaxed(),
                DCurrency.valueOf(dto.getCurrency()),
                command.liquidationId()
        );
        
        for (HotelBookingDto bookingDto : dto.getHotelBookings()) {
            DHotelBooking booking = new DHotelBooking(
                    bookingDto.getCheckIn(),
                    bookingDto.getCheckOut(),
                    bookingDto.getHotel(),
                    bookingDto.getRoom(),
                    Optional.ofNullable(bookingDto.getRoomDescription()),
                    bookingDto.getPriceByNight(),
                    DCurrency.valueOf(bookingDto.getCurrency()),
                    null
            );
            hotelService.addHotelBooking(booking);
        }
        
        liquidation.addHotelService(hotelService);
        
        return liquidationRepository.save(liquidation);
    }
}
