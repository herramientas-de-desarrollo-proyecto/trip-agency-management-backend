package com.tripagencymanagement.template.liquidations.application.commands.handlers;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.tripagencymanagement.template.liquidations.application.commands.UpdateHotelBookingCommand;
import com.tripagencymanagement.template.liquidations.application.services.LiquidationTotalsService;
import com.tripagencymanagement.template.liquidations.infrastructure.entities.HotelBooking;
import com.tripagencymanagement.template.liquidations.infrastructure.enums.ServiceStatus;
import com.tripagencymanagement.template.liquidations.infrastructure.repositories.interfaces.IHotelBookingJpaRepository;
import com.tripagencymanagement.template.liquidations.presentation.dto.UpdateHotelBookingDto;
import com.tripagencymanagement.template.users.infrastructure.enums.Currency;

@Service
public class UpdateHotelBookingCommandHandler {
    
    private final IHotelBookingJpaRepository hotelBookingRepository;
    private final LiquidationTotalsService liquidationTotalsService;
    
    public UpdateHotelBookingCommandHandler(
            IHotelBookingJpaRepository hotelBookingRepository,
            LiquidationTotalsService liquidationTotalsService) {
        this.hotelBookingRepository = hotelBookingRepository;
        this.liquidationTotalsService = liquidationTotalsService;
    }
    
    @Transactional
    public HotelBooking execute(UpdateHotelBookingCommand command) {
        HotelBooking hotelBooking = hotelBookingRepository.findById(command.hotelBookingId())
                .orElseThrow(() -> new IllegalArgumentException("La reserva de hotel no fue encontrada con id: " + command.hotelBookingId()));
        
        // Verificar que la reserva pertenece al hotel service correcto
        if (!hotelBooking.getHotelServiceId().equals(command.hotelServiceId())) {
            throw new IllegalArgumentException("La reserva no pertenece al servicio de hotel especificado");
        }
        
        // Guardar el liquidationId antes de la actualización
        Long liquidationId = hotelBooking.getHotelService().getLiquidationId();
        
        UpdateHotelBookingDto dto = command.hotelBookingDto();
        
        hotelBooking.setCheckIn(dto.getCheckIn());
        hotelBooking.setCheckOut(dto.getCheckOut());
        hotelBooking.setHotel(dto.getHotel());
        hotelBooking.setRoom(dto.getRoom());
        hotelBooking.setRoomDescription(dto.getRoomDescription());
        hotelBooking.setPriceByNight(dto.getPriceByNight());
        hotelBooking.setCurrency(Currency.valueOf(dto.getCurrency()));
        hotelBooking.setStatus(ServiceStatus.valueOf(dto.getStatus()));
        
        HotelBooking saved = hotelBookingRepository.save(hotelBooking);
        
        // Recalcular totales de la liquidación
        liquidationTotalsService.recalculateAndSaveTotals(liquidationId);
        
        return saved;
    }
}
