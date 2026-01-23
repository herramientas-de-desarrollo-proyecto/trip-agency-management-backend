package com.tripagencymanagement.template.liquidations.application.queries.handlers;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.tripagencymanagement.template.liquidations.application.queries.GetLiquidationsByStatusQuery;
import com.tripagencymanagement.template.liquidations.domain.entities.DLiquidation;
import com.tripagencymanagement.template.liquidations.domain.repositories.ILiquidationRepository;
import com.tripagencymanagement.template.liquidations.infrastructure.mappers.ILiquidationWithDetailsMapper;
import com.tripagencymanagement.template.liquidations.presentation.dto.LiquidationWithDetailsDto;

@Service
public class GetLiquidationsByStatusQueryHandler {
    
    private final ILiquidationRepository liquidationRepository;
    private final ILiquidationWithDetailsMapper detailsMapper;

    public GetLiquidationsByStatusQueryHandler(
            ILiquidationRepository liquidationRepository,
            ILiquidationWithDetailsMapper detailsMapper) {
        this.liquidationRepository = liquidationRepository;
        this.detailsMapper = detailsMapper;
    }

    public Page<LiquidationWithDetailsDto> execute(GetLiquidationsByStatusQuery query) {
        Pageable pageable = PageRequest.of(
            query.requestDto().getPage(),
            query.requestDto().getSize()
        );
        Page<DLiquidation> liquidations = liquidationRepository.findByStatus(query.status(), pageable);
        return liquidations.map(detailsMapper::toDto);
    }
}
