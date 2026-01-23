package com.tripagencymanagement.template.liquidations.application.commands.handlers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.tripagencymanagement.template.liquidations.application.commands.AddAdditionalServiceCommand;
import com.tripagencymanagement.template.liquidations.domain.entities.DAdditionalServices;
import com.tripagencymanagement.template.liquidations.domain.entities.DLiquidation;
import com.tripagencymanagement.template.liquidations.domain.repositories.ILiquidationRepository;
import com.tripagencymanagement.template.liquidations.presentation.dto.AddAdditionalServiceDto;
import com.tripagencymanagement.template.users.domain.enums.DCurrency;

@Service
public class AddAdditionalServiceCommandHandler {
    
    private final ILiquidationRepository liquidationRepository;
    
    @Autowired
    public AddAdditionalServiceCommandHandler(ILiquidationRepository liquidationRepository) {
        this.liquidationRepository = liquidationRepository;
    }
    
    @Transactional
    public DLiquidation execute(AddAdditionalServiceCommand command) {
        DLiquidation liquidation = liquidationRepository.findById(command.liquidationId())
                .orElseThrow(() -> new IllegalArgumentException("Liquidation not found with id: " + command.liquidationId()));
        
        AddAdditionalServiceDto dto = command.additionalServiceDto();
        
        DAdditionalServices additionalService = new DAdditionalServices(
                dto.getTariffRate(),
                dto.getIsTaxed(),
                DCurrency.valueOf(dto.getCurrency()),
                command.liquidationId(),
                dto.getPrice()
        );
        
        liquidation.addAdditionalService(additionalService);
        
    return liquidationRepository.save(liquidation);
    }
}
