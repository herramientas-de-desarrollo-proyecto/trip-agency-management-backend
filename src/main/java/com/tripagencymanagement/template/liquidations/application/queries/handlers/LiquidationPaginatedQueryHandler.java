package com.tripagencymanagement.template.liquidations.application.queries.handlers;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.Sort;

import com.tripagencymanagement.template.liquidations.application.queries.LiquidationPaginatedQuery;
import com.tripagencymanagement.template.liquidations.domain.entities.DLiquidation;
import com.tripagencymanagement.template.liquidations.domain.repositories.ILiquidationRepository;
import com.tripagencymanagement.template.liquidations.infrastructure.mappers.ILiquidationWithDetailsMapper;
import com.tripagencymanagement.template.liquidations.presentation.dto.LiquidationWithDetailsDto;

@Service
public class LiquidationPaginatedQueryHandler {

    private final ILiquidationRepository liquidationRepository;
    private final ILiquidationWithDetailsMapper detailsMapper;

    public LiquidationPaginatedQueryHandler(
            ILiquidationRepository liquidationRepository,
            ILiquidationWithDetailsMapper detailsMapper) {
        this.liquidationRepository = liquidationRepository;
        this.detailsMapper = detailsMapper;
    }

    public Page<LiquidationWithDetailsDto> execute(LiquidationPaginatedQuery query) {
        Pageable pageable = PageRequest.of(
                query.requestDto().getPage(),
                query.requestDto().getSize(),
                Sort.by(Sort.Direction.DESC, "createdDate"));
        Page<DLiquidation> liquidations = liquidationRepository.findAll(pageable);
        return liquidations.map(detailsMapper::toDto);
    }
}
