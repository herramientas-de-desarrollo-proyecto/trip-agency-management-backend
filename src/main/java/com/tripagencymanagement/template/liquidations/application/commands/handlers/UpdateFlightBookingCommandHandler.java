package com.tripagencymanagement.template.liquidations.application.commands.handlers;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.tripagencymanagement.template.liquidations.application.commands.UpdateFlightBookingCommand;
import com.tripagencymanagement.template.liquidations.application.services.LiquidationTotalsService;
import com.tripagencymanagement.template.liquidations.infrastructure.entities.FlightBooking;
import com.tripagencymanagement.template.liquidations.infrastructure.enums.ServiceStatus;
import com.tripagencymanagement.template.liquidations.infrastructure.repositories.interfaces.IFlightBookingJpaRepository;
import com.tripagencymanagement.template.liquidations.presentation.dto.UpdateFlightBookingDto;
import com.tripagencymanagement.template.users.infrastructure.enums.Currency;

@Service
public class UpdateFlightBookingCommandHandler {
    
    private final IFlightBookingJpaRepository flightBookingRepository;
    private final LiquidationTotalsService liquidationTotalsService;
    
    public UpdateFlightBookingCommandHandler(
            IFlightBookingJpaRepository flightBookingRepository,
            LiquidationTotalsService liquidationTotalsService) {
        this.flightBookingRepository = flightBookingRepository;
        this.liquidationTotalsService = liquidationTotalsService;
    }
    
    @Transactional
    public FlightBooking execute(UpdateFlightBookingCommand command) {
        FlightBooking flightBooking = flightBookingRepository.findById(command.flightBookingId())
                .orElseThrow(() -> new IllegalArgumentException("La reserva de vuelo no fue encontrada con id: " + command.flightBookingId()));
        
        // Verificar que la reserva pertenece al flight service correcto
        if (!flightBooking.getFlightServiceId().equals(command.flightServiceId())) {
            throw new IllegalArgumentException("La reserva no pertenece al servicio de vuelo especificado");
        }
        
        // Guardar el liquidationId antes de la actualización (a través de la relación con FlightService)
        Long liquidationId = flightBooking.getFlightService().getLiquidationId();
        
        UpdateFlightBookingDto dto = command.flightBookingDto();
        
        flightBooking.setOrigin(dto.getOrigin());
        flightBooking.setDestiny(dto.getDestiny());
        flightBooking.setDepartureDate(dto.getDepartureDate());
        flightBooking.setArrivalDate(dto.getArrivalDate());
        flightBooking.setAeroline(dto.getAeroline());
        flightBooking.setAerolineBookingCode(dto.getAerolineBookingCode());
        flightBooking.setCostamarBookingCode(dto.getCostamarBookingCode());
        flightBooking.setTktNumbers(dto.getTktNumbers());
        flightBooking.setTotalPrice(dto.getTotalPrice());
        flightBooking.setCurrency(Currency.valueOf(dto.getCurrency()));
        flightBooking.setStatus(ServiceStatus.valueOf(dto.getStatus()));
        
        FlightBooking saved = flightBookingRepository.save(flightBooking);
        
        // Recalcular totales de la liquidación
        liquidationTotalsService.recalculateAndSaveTotals(liquidationId);
        
        return saved;
    }
}
