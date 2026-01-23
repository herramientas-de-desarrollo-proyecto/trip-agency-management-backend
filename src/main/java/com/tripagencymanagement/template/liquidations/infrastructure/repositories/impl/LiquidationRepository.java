package com.tripagencymanagement.template.liquidations.infrastructure.repositories.impl;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import com.tripagencymanagement.template.liquidations.domain.entities.DLiquidation;
import com.tripagencymanagement.template.liquidations.domain.repositories.ILiquidationRepository;
import com.tripagencymanagement.template.liquidations.infrastructure.mappers.ILiquidationEnumMapper;
import com.tripagencymanagement.template.liquidations.infrastructure.mappers.ILiquidationMapper;
import com.tripagencymanagement.template.liquidations.infrastructure.repositories.interfaces.ILiquidationJpaRepository;

@Component
public class LiquidationRepository implements ILiquidationRepository {

    private final ILiquidationJpaRepository jpaRepository;
    private final ILiquidationMapper mapper;
    private final ILiquidationEnumMapper enumMapper;

    public LiquidationRepository(ILiquidationJpaRepository jpaRepository, ILiquidationMapper mapper,
            ILiquidationEnumMapper enumMapper) {
        this.jpaRepository = jpaRepository;
        this.mapper = mapper;
        this.enumMapper = enumMapper;
    }

    @Override
    public DLiquidation save(DLiquidation liquidation) {
        // If liquidation has an ID, it's an update
        if (liquidation.getId() != null) {
            // Load existing entity from database
            var existingEntity = jpaRepository.findById(liquidation.getId())
                    .orElseThrow(() -> new IllegalArgumentException(
                            "La liquidación no fue encontrada con id: " + liquidation.getId()));

            // Update scalar fields
            existingEntity.setCurrencyRate(liquidation.getCurrencyRate());
            existingEntity.setTotalAmount(liquidation.getTotalAmount());
            existingEntity.setTotalAmountUSD(liquidation.getTotalAmountUSD());
            existingEntity.setTotalCommissionPEN(liquidation.getTotalCommissionPEN());
            existingEntity.setTotalCommissionUSD(liquidation.getTotalCommissionUSD());
            existingEntity.setPaymentDeadline(liquidation.getPaymentDeadline());
            existingEntity.setCompanion(liquidation.getCompanion());
            existingEntity.setStatus(enumMapper.toInfrastructure(liquidation.getStatus()));
            existingEntity.setPaymentStatus(enumMapper.toInfrastructure(liquidation.getPaymentStatus()));

            // Count existing items to know how many are new
            int existingPaymentsCount = existingEntity.getPayments().size();
            int existingFlightServicesCount = existingEntity.getFlightServices().size();
            int existingHotelServicesCount = existingEntity.getHotelServices().size();
            int existingTourServicesCount = existingEntity.getTourServices().size();
            int existingAdditionalServicesCount = existingEntity.getAdditionalServices().size();
            int existingIncidenciesCount = existingEntity.getIncidencies().size();

            // Map the entire liquidation to get all items (existing + new)
            var mappedInfrastructure = mapper.toInfrastructure(liquidation);

            // Add only NEW items (those beyond the existing count)
            // Payments
            if (mappedInfrastructure.getPayments() != null && 
                mappedInfrastructure.getPayments().size() > existingPaymentsCount) {
                for (int i = existingPaymentsCount; i < mappedInfrastructure.getPayments().size(); i++) {
                    var newPayment = mappedInfrastructure.getPayments().get(i);
                    newPayment.setLiquidation(existingEntity);
                    existingEntity.getPayments().add(newPayment);
                }
            }

            // Flight Services
            if (mappedInfrastructure.getFlightServices() != null && 
                mappedInfrastructure.getFlightServices().size() > existingFlightServicesCount) {
                for (int i = existingFlightServicesCount; i < mappedInfrastructure.getFlightServices().size(); i++) {
                    var newService = mappedInfrastructure.getFlightServices().get(i);
                    newService.setLiquidation(existingEntity);
                    existingEntity.getFlightServices().add(newService);
                }
            }

            // Hotel Services
            if (mappedInfrastructure.getHotelServices() != null && 
                mappedInfrastructure.getHotelServices().size() > existingHotelServicesCount) {
                for (int i = existingHotelServicesCount; i < mappedInfrastructure.getHotelServices().size(); i++) {
                    var newService = mappedInfrastructure.getHotelServices().get(i);
                    newService.setLiquidation(existingEntity);
                    existingEntity.getHotelServices().add(newService);
                }
            }

            // Tour Services
            if (mappedInfrastructure.getTourServices() != null && 
                mappedInfrastructure.getTourServices().size() > existingTourServicesCount) {
                for (int i = existingTourServicesCount; i < mappedInfrastructure.getTourServices().size(); i++) {
                    var newService = mappedInfrastructure.getTourServices().get(i);
                    newService.setLiquidation(existingEntity);
                    existingEntity.getTourServices().add(newService);
                }
            }

            // Additional Services
            if (mappedInfrastructure.getAdditionalServices() != null && 
                mappedInfrastructure.getAdditionalServices().size() > existingAdditionalServicesCount) {
                for (int i = existingAdditionalServicesCount; i < mappedInfrastructure.getAdditionalServices().size(); i++) {
                    var newService = mappedInfrastructure.getAdditionalServices().get(i);
                    newService.setLiquidation(existingEntity);
                    existingEntity.getAdditionalServices().add(newService);
                }
            }

            // Incidencies
            if (mappedInfrastructure.getIncidencies() != null && 
                mappedInfrastructure.getIncidencies().size() > existingIncidenciesCount) {
                for (int i = existingIncidenciesCount; i < mappedInfrastructure.getIncidencies().size(); i++) {
                    var newIncidency = mappedInfrastructure.getIncidencies().get(i);
                    newIncidency.setLiquidation(existingEntity);
                    existingEntity.getIncidencies().add(newIncidency);
                }
            }

            var savedEntity = jpaRepository.save(existingEntity);
            return mapper.toDomain(savedEntity);
        } else {
            // New liquidation - just map and save
            var infrastructureEntity = mapper.toInfrastructure(liquidation);
            var savedEntity = jpaRepository.save(infrastructureEntity);
            return mapper.toDomain(savedEntity);
        }
    }

    @Override
    public Optional<DLiquidation> findById(Long id) {
        return jpaRepository.findByIdAndIsActiveTrue(id)
                .map(mapper::toDomain);
    }

    @Override
    public Page<DLiquidation> findAll(Pageable pageable) {
        return jpaRepository.findByIsActiveTrue(pageable)
                .map(mapper::toDomain);
    }

    @Override
    public void deleteById(Long id) {
        jpaRepository.deleteById(id);
    }

    @Override
    public boolean existsById(Long id) {
        return jpaRepository.existsById(id);
    }

    @Override
    public Page<DLiquidation> findByCustomerId(Long customerId, Pageable pageable) {
        return jpaRepository.findByCustomerIdAndIsActiveTrue(customerId, pageable)
                .map(mapper::toDomain);
    }

    @Override
    public Page<DLiquidation> findByStatus(com.tripagencymanagement.template.liquidations.domain.enums.DLiquidationStatus status, Pageable pageable) {
        var infrastructureStatus = enumMapper.toInfrastructure(status);
        return jpaRepository.findByStatusAndIsActiveTrue(infrastructureStatus, pageable)
                .map(mapper::toDomain);
    }
}
