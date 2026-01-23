package com.tripagencymanagement.template.liquidations.application.services;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.tripagencymanagement.template.liquidations.domain.entities.DLiquidation;
import com.tripagencymanagement.template.liquidations.domain.repositories.ILiquidationRepository;

/**
 * Servicio para recalcular los totales de una liquidación.
 * Se utiliza después de crear, actualizar o eliminar servicios.
 */
@Service
public class LiquidationTotalsService {
    
    private final ILiquidationRepository liquidationRepository;
    
    public LiquidationTotalsService(ILiquidationRepository liquidationRepository) {
        this.liquidationRepository = liquidationRepository;
    }
    
    /**
     * Recalcula y guarda los totales de una liquidación.
     * Obtiene la liquidación con todos sus servicios, recalcula los totales
     * y persiste los cambios.
     * 
     * @param liquidationId ID de la liquidación a recalcular
     * @return La liquidación con los totales actualizados
     */
    @Transactional
    public DLiquidation recalculateAndSaveTotals(Long liquidationId) {
        DLiquidation liquidation = liquidationRepository.findById(liquidationId)
                .orElseThrow(() -> new IllegalArgumentException(
                        "La liquidación no fue encontrada con id: " + liquidationId));
        
        // Recalcula todos los totales
        liquidation.recalculateTotals();
        
        // Guarda la liquidación con los nuevos totales
        return liquidationRepository.save(liquidation);
    }
}
