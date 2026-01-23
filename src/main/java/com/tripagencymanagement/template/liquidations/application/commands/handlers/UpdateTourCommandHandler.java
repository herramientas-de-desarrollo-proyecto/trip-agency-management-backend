package com.tripagencymanagement.template.liquidations.application.commands.handlers;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.tripagencymanagement.template.liquidations.application.commands.UpdateTourCommand;
import com.tripagencymanagement.template.liquidations.application.services.LiquidationTotalsService;
import com.tripagencymanagement.template.liquidations.infrastructure.entities.Tour;
import com.tripagencymanagement.template.liquidations.infrastructure.enums.ServiceStatus;
import com.tripagencymanagement.template.liquidations.infrastructure.repositories.interfaces.ITourJpaRepository;
import com.tripagencymanagement.template.liquidations.presentation.dto.UpdateTourDto;
import com.tripagencymanagement.template.users.infrastructure.enums.Currency;

@Service
public class UpdateTourCommandHandler {
    
    private final ITourJpaRepository tourRepository;
    private final LiquidationTotalsService liquidationTotalsService;
    
    public UpdateTourCommandHandler(
            ITourJpaRepository tourRepository,
            LiquidationTotalsService liquidationTotalsService) {
        this.tourRepository = tourRepository;
        this.liquidationTotalsService = liquidationTotalsService;
    }
    
    @Transactional
    public Tour execute(UpdateTourCommand command) {
        Tour tour = tourRepository.findById(command.tourId())
                .orElseThrow(() -> new IllegalArgumentException("El tour no fue encontrado con id: " + command.tourId()));
        
        // Verificar que el tour pertenece al tour service correcto
        if (!tour.getTourServiceId().equals(command.tourServiceId())) {
            throw new IllegalArgumentException("El tour no pertenece al servicio de tour especificado");
        }
        
        // Guardar el liquidationId antes de la actualización
        Long liquidationId = tour.getTourService().getLiquidationId();
        
        UpdateTourDto dto = command.tourDto();
        
        tour.setStartDate(dto.getStartDate());
        tour.setEndDate(dto.getEndDate());
        tour.setTitle(dto.getTitle());
        tour.setPrice(dto.getPrice());
        tour.setPlace(dto.getPlace());
        tour.setCurrency(Currency.valueOf(dto.getCurrency()));
        tour.setStatus(ServiceStatus.valueOf(dto.getStatus()));
        
        Tour saved = tourRepository.save(tour);
        
        // Recalcular totales de la liquidación
        liquidationTotalsService.recalculateAndSaveTotals(liquidationId);
        
        return saved;
    }
}
