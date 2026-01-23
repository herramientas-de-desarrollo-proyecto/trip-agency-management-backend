package com.tripagencymanagement.template.liquidations.application.queries.handlers;

import java.util.Optional;

import org.springframework.stereotype.Service;

import com.tripagencymanagement.template.liquidations.application.queries.GetLiquidationByIdQuery;
import com.tripagencymanagement.template.liquidations.domain.entities.DLiquidation;
import com.tripagencymanagement.template.liquidations.domain.repositories.ILiquidationRepository;
import com.tripagencymanagement.template.liquidations.infrastructure.mappers.ILiquidationWithDetailsMapper;
import com.tripagencymanagement.template.liquidations.presentation.dto.LiquidationWithDetailsDto;

@Service
public class GetLiquidationByIdQueryHandler {
    
    private final ILiquidationRepository liquidationRepository;
    private final ILiquidationWithDetailsMapper detailsMapper;

    public GetLiquidationByIdQueryHandler(
            ILiquidationRepository liquidationRepository,
            ILiquidationWithDetailsMapper detailsMapper) {
        this.liquidationRepository = liquidationRepository;
        this.detailsMapper = detailsMapper;
    }

    public Optional<LiquidationWithDetailsDto> execute(GetLiquidationByIdQuery query) {
        return liquidationRepository.findById(query.liquidationId())
                .map(detailsMapper::toDto);
    }
}
