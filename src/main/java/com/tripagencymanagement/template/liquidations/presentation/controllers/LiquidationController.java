package com.tripagencymanagement.template.liquidations.presentation.controllers;

import org.springframework.data.domain.Page;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.tripagencymanagement.template.liquidations.application.commands.AddAdditionalServiceCommand;
import com.tripagencymanagement.template.liquidations.application.commands.AddFlightServiceCommand;
import com.tripagencymanagement.template.liquidations.application.commands.AddHotelServiceCommand;
import com.tripagencymanagement.template.liquidations.application.commands.AddIncidencyCommand;
import com.tripagencymanagement.template.liquidations.application.commands.AddPaymentCommand;
import com.tripagencymanagement.template.liquidations.application.commands.AddTourServiceCommand;
import com.tripagencymanagement.template.liquidations.application.commands.CreateLiquidationCommand;
import com.tripagencymanagement.template.liquidations.application.commands.DeactivateAdditionalServiceCommand;
import com.tripagencymanagement.template.liquidations.application.commands.DeactivateFlightBookingCommand;
import com.tripagencymanagement.template.liquidations.application.commands.DeactivateHotelBookingCommand;
import com.tripagencymanagement.template.liquidations.application.commands.DeactivateIncidencyCommand;
import com.tripagencymanagement.template.liquidations.application.commands.DeactivateLiquidationCommand;
import com.tripagencymanagement.template.liquidations.application.commands.DeactivatePaymentCommand;
import com.tripagencymanagement.template.liquidations.application.commands.DeactivateTourCommand;
import com.tripagencymanagement.template.liquidations.application.commands.UpdateAdditionalServiceCommand;
import com.tripagencymanagement.template.liquidations.application.commands.UpdateFlightBookingCommand;
import com.tripagencymanagement.template.liquidations.application.commands.UpdateHotelBookingCommand;
import com.tripagencymanagement.template.liquidations.application.commands.UpdateIncidencyCommand;
import com.tripagencymanagement.template.liquidations.application.commands.UpdateLiquidationStatusCommand;
import com.tripagencymanagement.template.liquidations.application.commands.UpdatePaymentCommand;
import com.tripagencymanagement.template.liquidations.application.commands.UpdatePaymentStatusCommand;
import com.tripagencymanagement.template.liquidations.application.commands.UpdateTourCommand;
import com.tripagencymanagement.template.liquidations.application.commands.handlers.AddAdditionalServiceCommandHandler;
import com.tripagencymanagement.template.liquidations.application.commands.handlers.AddFlightServiceCommandHandler;
import com.tripagencymanagement.template.liquidations.application.commands.handlers.AddHotelServiceCommandHandler;
import com.tripagencymanagement.template.liquidations.application.commands.handlers.AddIncidencyCommandHandler;
import com.tripagencymanagement.template.liquidations.application.commands.handlers.AddPaymentCommandHandler;
import com.tripagencymanagement.template.liquidations.application.commands.handlers.AddTourServiceCommandHandler;
import com.tripagencymanagement.template.liquidations.application.commands.handlers.CreateLiquidationCommandHandler;
import com.tripagencymanagement.template.liquidations.application.commands.handlers.DeactivateAdditionalServiceCommandHandler;
import com.tripagencymanagement.template.liquidations.application.commands.handlers.DeactivateFlightBookingCommandHandler;
import com.tripagencymanagement.template.liquidations.application.commands.handlers.DeactivateHotelBookingCommandHandler;
import com.tripagencymanagement.template.liquidations.application.commands.handlers.DeactivateIncidencyCommandHandler;
import com.tripagencymanagement.template.liquidations.application.commands.handlers.DeactivateLiquidationCommandHandler;
import com.tripagencymanagement.template.liquidations.application.commands.handlers.DeactivatePaymentCommandHandler;
import com.tripagencymanagement.template.liquidations.application.commands.handlers.DeactivateTourCommandHandler;
import com.tripagencymanagement.template.liquidations.application.commands.handlers.UpdateAdditionalServiceCommandHandler;
import com.tripagencymanagement.template.liquidations.application.commands.handlers.UpdateFlightBookingCommandHandler;
import com.tripagencymanagement.template.liquidations.application.commands.handlers.UpdateHotelBookingCommandHandler;
import com.tripagencymanagement.template.liquidations.application.commands.handlers.UpdateIncidencyCommandHandler;
import com.tripagencymanagement.template.liquidations.application.commands.handlers.UpdateLiquidationStatusCommandHandler;
import com.tripagencymanagement.template.liquidations.application.commands.handlers.UpdatePaymentCommandHandler;
import com.tripagencymanagement.template.liquidations.application.commands.handlers.UpdatePaymentStatusCommandHandler;
import com.tripagencymanagement.template.liquidations.application.commands.handlers.UpdateTourCommandHandler;
import com.tripagencymanagement.template.liquidations.application.queries.GetLiquidationByIdQuery;
import com.tripagencymanagement.template.liquidations.application.queries.GetLiquidationsByCustomerQuery;
import com.tripagencymanagement.template.liquidations.application.queries.GetLiquidationsByStatusQuery;
import com.tripagencymanagement.template.liquidations.application.queries.LiquidationPaginatedQuery;
import com.tripagencymanagement.template.liquidations.application.queries.handlers.GetLiquidationByIdQueryHandler;
import com.tripagencymanagement.template.liquidations.application.queries.handlers.GetLiquidationsByCustomerQueryHandler;
import com.tripagencymanagement.template.liquidations.application.queries.handlers.GetLiquidationsByStatusQueryHandler;
import com.tripagencymanagement.template.liquidations.application.queries.handlers.LiquidationPaginatedQueryHandler;
import com.tripagencymanagement.template.liquidations.application.services.QuotePdfGeneratorService;
import com.tripagencymanagement.template.liquidations.domain.entities.DAdditionalServices;
import com.tripagencymanagement.template.liquidations.domain.entities.DFlightBooking;
import com.tripagencymanagement.template.liquidations.domain.entities.DHotelBooking;
import com.tripagencymanagement.template.liquidations.domain.entities.DIncidency;
import com.tripagencymanagement.template.liquidations.domain.entities.DLiquidation;
import com.tripagencymanagement.template.liquidations.domain.entities.DPayment;
import com.tripagencymanagement.template.liquidations.domain.entities.DTour;
import com.tripagencymanagement.template.liquidations.domain.enums.DCurrency;
import com.tripagencymanagement.template.liquidations.domain.enums.DLiquidationStatus;
import com.tripagencymanagement.template.liquidations.domain.enums.DPaymentMethod;
import com.tripagencymanagement.template.liquidations.infrastructure.entities.AdditionalServices;
import com.tripagencymanagement.template.liquidations.infrastructure.entities.FlightBooking;
import com.tripagencymanagement.template.liquidations.infrastructure.entities.HotelBooking;
import com.tripagencymanagement.template.liquidations.infrastructure.entities.Incidency;
import com.tripagencymanagement.template.liquidations.infrastructure.entities.Liquidation;
import com.tripagencymanagement.template.liquidations.infrastructure.entities.Payment;
import com.tripagencymanagement.template.liquidations.infrastructure.entities.Tour;
import com.tripagencymanagement.template.liquidations.infrastructure.mappers.IAdditionalServicesMapper;
import com.tripagencymanagement.template.liquidations.infrastructure.mappers.IFlightBookingMapper;
import com.tripagencymanagement.template.liquidations.infrastructure.mappers.IHotelBookingMapper;
import com.tripagencymanagement.template.liquidations.infrastructure.mappers.IIncidencyMapper;
import com.tripagencymanagement.template.liquidations.infrastructure.mappers.ILiquidationMapper;
import com.tripagencymanagement.template.liquidations.infrastructure.mappers.IPaymentMapper;
import com.tripagencymanagement.template.liquidations.infrastructure.mappers.ITourMapper;
import com.tripagencymanagement.template.liquidations.presentation.dto.AddAdditionalServiceDto;
import com.tripagencymanagement.template.liquidations.presentation.dto.AddFlightServiceDto;
import com.tripagencymanagement.template.liquidations.presentation.dto.AddHotelServiceDto;
import com.tripagencymanagement.template.liquidations.presentation.dto.AddIncidencyDto;
import com.tripagencymanagement.template.liquidations.presentation.dto.AddPaymentDto;
import com.tripagencymanagement.template.liquidations.presentation.dto.AddTourServiceDto;
import com.tripagencymanagement.template.liquidations.presentation.dto.CreateLiquidationDto;
import com.tripagencymanagement.template.liquidations.presentation.dto.LiquidationWithDetailsDto;
import com.tripagencymanagement.template.liquidations.presentation.dto.PaginatedLiquidationRequestDto;
import com.tripagencymanagement.template.liquidations.presentation.dto.UpdateAdditionalServiceDto;
import com.tripagencymanagement.template.liquidations.presentation.dto.UpdateFlightBookingDto;
import com.tripagencymanagement.template.liquidations.presentation.dto.UpdateHotelBookingDto;
import com.tripagencymanagement.template.liquidations.presentation.dto.UpdateIncidencyDto;
import com.tripagencymanagement.template.liquidations.presentation.dto.UpdatePaymentDto;
import com.tripagencymanagement.template.liquidations.presentation.dto.UpdatePaymentStatusDto;
import com.tripagencymanagement.template.liquidations.presentation.dto.UpdateLiquidationStatusDto;
import com.tripagencymanagement.template.liquidations.presentation.dto.UpdateTourDto;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/liquidations")
@Tag(name = "Liquidaciones", description = "Endpoints de gestión de liquidaciones")
public class LiquidationController {

    private final CreateLiquidationCommandHandler createLiquidationCommandHandler;
    private final LiquidationPaginatedQueryHandler liquidationPaginatedQueryHandler;
    private final GetLiquidationByIdQueryHandler getLiquidationByIdQueryHandler;
    private final GetLiquidationsByCustomerQueryHandler getLiquidationsByCustomerQueryHandler;
    private final GetLiquidationsByStatusQueryHandler getLiquidationsByStatusQueryHandler;
    private final AddPaymentCommandHandler addPaymentCommandHandler;
    private final AddFlightServiceCommandHandler addFlightServiceCommandHandler;
    private final AddHotelServiceCommandHandler addHotelServiceCommandHandler;
    private final AddTourServiceCommandHandler addTourServiceCommandHandler;
    private final AddAdditionalServiceCommandHandler addAdditionalServiceCommandHandler;
    private final AddIncidencyCommandHandler addIncidencyCommandHandler;
    private final UpdateTourCommandHandler updateTourCommandHandler;
    private final UpdateHotelBookingCommandHandler updateHotelBookingCommandHandler;
    private final UpdateFlightBookingCommandHandler updateFlightBookingCommandHandler;
    private final UpdateAdditionalServiceCommandHandler updateAdditionalServiceCommandHandler;
    private final UpdatePaymentCommandHandler updatePaymentCommandHandler;
    private final UpdateIncidencyCommandHandler updateIncidencyCommandHandler;
    private final UpdateLiquidationStatusCommandHandler updateLiquidationStatusCommandHandler;
    private final UpdatePaymentStatusCommandHandler updatePaymentStatusCommandHandler;
    private final DeactivateLiquidationCommandHandler deactivateLiquidationCommandHandler;
    private final DeactivateTourCommandHandler deactivateTourCommandHandler;
    private final DeactivateHotelBookingCommandHandler deactivateHotelBookingCommandHandler;
    private final DeactivateFlightBookingCommandHandler deactivateFlightBookingCommandHandler;
    private final DeactivateAdditionalServiceCommandHandler deactivateAdditionalServiceCommandHandler;
    private final DeactivatePaymentCommandHandler deactivatePaymentCommandHandler;
    private final DeactivateIncidencyCommandHandler deactivateIncidencyCommandHandler;
    
    // Services
    private final QuotePdfGeneratorService quotePdfGeneratorService;
    
    // Mappers
    private final ILiquidationMapper liquidationMapper;
    private final ITourMapper tourMapper;
    private final IHotelBookingMapper hotelBookingMapper;
    private final IFlightBookingMapper flightBookingMapper;
    private final IAdditionalServicesMapper additionalServicesMapper;
    private final IPaymentMapper paymentMapper;
    private final IIncidencyMapper incidencyMapper;

    public LiquidationController(
            CreateLiquidationCommandHandler createLiquidationCommandHandler,
            LiquidationPaginatedQueryHandler liquidationPaginatedQueryHandler,
            GetLiquidationByIdQueryHandler getLiquidationByIdQueryHandler,
            GetLiquidationsByCustomerQueryHandler getLiquidationsByCustomerQueryHandler,
            GetLiquidationsByStatusQueryHandler getLiquidationsByStatusQueryHandler,
            AddPaymentCommandHandler addPaymentCommandHandler,
            AddFlightServiceCommandHandler addFlightServiceCommandHandler,
            AddHotelServiceCommandHandler addHotelServiceCommandHandler,
            AddTourServiceCommandHandler addTourServiceCommandHandler,
            AddAdditionalServiceCommandHandler addAdditionalServiceCommandHandler,
            AddIncidencyCommandHandler addIncidencyCommandHandler,
            UpdateTourCommandHandler updateTourCommandHandler,
            UpdateHotelBookingCommandHandler updateHotelBookingCommandHandler,
            UpdateFlightBookingCommandHandler updateFlightBookingCommandHandler,
            UpdateAdditionalServiceCommandHandler updateAdditionalServiceCommandHandler,
            UpdatePaymentCommandHandler updatePaymentCommandHandler,
            UpdateIncidencyCommandHandler updateIncidencyCommandHandler,
            UpdateLiquidationStatusCommandHandler updateLiquidationStatusCommandHandler,
            UpdatePaymentStatusCommandHandler updatePaymentStatusCommandHandler,
            DeactivateLiquidationCommandHandler deactivateLiquidationCommandHandler,
            DeactivateTourCommandHandler deactivateTourCommandHandler,
            DeactivateHotelBookingCommandHandler deactivateHotelBookingCommandHandler,
            DeactivateFlightBookingCommandHandler deactivateFlightBookingCommandHandler,
            DeactivateAdditionalServiceCommandHandler deactivateAdditionalServiceCommandHandler,
            DeactivatePaymentCommandHandler deactivatePaymentCommandHandler,
            DeactivateIncidencyCommandHandler deactivateIncidencyCommandHandler,
            ILiquidationMapper liquidationMapper,
            ITourMapper tourMapper,
            IHotelBookingMapper hotelBookingMapper,
            IFlightBookingMapper flightBookingMapper,
            IAdditionalServicesMapper additionalServicesMapper,
            IPaymentMapper paymentMapper,
            IIncidencyMapper incidencyMapper,
            QuotePdfGeneratorService quotePdfGeneratorService) {
        this.createLiquidationCommandHandler = createLiquidationCommandHandler;
        this.liquidationPaginatedQueryHandler = liquidationPaginatedQueryHandler;
        this.getLiquidationByIdQueryHandler = getLiquidationByIdQueryHandler;
        this.getLiquidationsByCustomerQueryHandler = getLiquidationsByCustomerQueryHandler;
        this.getLiquidationsByStatusQueryHandler = getLiquidationsByStatusQueryHandler;
        this.addPaymentCommandHandler = addPaymentCommandHandler;
        this.addFlightServiceCommandHandler = addFlightServiceCommandHandler;
        this.addHotelServiceCommandHandler = addHotelServiceCommandHandler;
        this.addTourServiceCommandHandler = addTourServiceCommandHandler;
        this.addAdditionalServiceCommandHandler = addAdditionalServiceCommandHandler;
        this.addIncidencyCommandHandler = addIncidencyCommandHandler;
        this.updateTourCommandHandler = updateTourCommandHandler;
        this.updateHotelBookingCommandHandler = updateHotelBookingCommandHandler;
        this.updateFlightBookingCommandHandler = updateFlightBookingCommandHandler;
        this.updateAdditionalServiceCommandHandler = updateAdditionalServiceCommandHandler;
        this.updatePaymentCommandHandler = updatePaymentCommandHandler;
        this.updateIncidencyCommandHandler = updateIncidencyCommandHandler;
        this.updateLiquidationStatusCommandHandler = updateLiquidationStatusCommandHandler;
        this.updatePaymentStatusCommandHandler = updatePaymentStatusCommandHandler;
        this.deactivateLiquidationCommandHandler = deactivateLiquidationCommandHandler;
        this.deactivateTourCommandHandler = deactivateTourCommandHandler;
        this.deactivateHotelBookingCommandHandler = deactivateHotelBookingCommandHandler;
        this.deactivateFlightBookingCommandHandler = deactivateFlightBookingCommandHandler;
        this.deactivateAdditionalServiceCommandHandler = deactivateAdditionalServiceCommandHandler;
        this.deactivatePaymentCommandHandler = deactivatePaymentCommandHandler;
        this.deactivateIncidencyCommandHandler = deactivateIncidencyCommandHandler;
        this.liquidationMapper = liquidationMapper;
        this.tourMapper = tourMapper;
        this.hotelBookingMapper = hotelBookingMapper;
        this.flightBookingMapper = flightBookingMapper;
        this.additionalServicesMapper = additionalServicesMapper;
        this.paymentMapper = paymentMapper;
        this.incidencyMapper = incidencyMapper;
        this.quotePdfGeneratorService = quotePdfGeneratorService;
    }

    @PostMapping
    @Operation(summary = "Crear una nueva liquidación")
    public ResponseEntity<DLiquidation> createLiquidation(@Valid @RequestBody CreateLiquidationDto dto) {
        CreateLiquidationCommand command = new CreateLiquidationCommand(
                dto.getCurrencyRate(),
                dto.getPaymentDeadline(),
                dto.getCompanion(),
                dto.getCustomerId(),
                dto.getStaffId());

        DLiquidation liquidation = createLiquidationCommandHandler.execute(command);
        return ResponseEntity.status(HttpStatus.CREATED).body(liquidation);
    }

    @GetMapping("/paginated")
    @Operation(summary = "Obtener liquidaciones paginadas")
    public Page<LiquidationWithDetailsDto> getPaginatedLiquidations(@ModelAttribute PaginatedLiquidationRequestDto requestDto) {
        requestDto.normalizePageNumber();
        LiquidationPaginatedQuery query = new LiquidationPaginatedQuery(requestDto);
        return liquidationPaginatedQueryHandler.execute(query);
    }

    @GetMapping("/{liquidationId}")
    @Operation(summary = "Obtener liquidación por ID")
    public ResponseEntity<LiquidationWithDetailsDto> getLiquidationById(@PathVariable Long liquidationId) {
        GetLiquidationByIdQuery query = new GetLiquidationByIdQuery(liquidationId);
        return getLiquidationByIdQueryHandler.execute(query)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/{liquidationId}/quote-pdf")
    @Operation(summary = "Descargar cotización en PDF (solo para estado IN_QUOTE)")
    public ResponseEntity<byte[]> downloadQuotePdf(@PathVariable Long liquidationId) {
        GetLiquidationByIdQuery query = new GetLiquidationByIdQuery(liquidationId);
        return getLiquidationByIdQueryHandler.execute(query)
                .map(liquidation -> {
                    // Validate that liquidation is in IN_QUOTE status
                    if (liquidation.getStatus() != DLiquidationStatus.IN_QUOTE) {
                        return ResponseEntity.badRequest().<byte[]>build();
                    }
                    
                    byte[] pdfBytes = quotePdfGeneratorService.generateQuotePdf(liquidation);
                    
                    String filename = String.format("cotizacion_%06d.pdf", liquidation.getId());
                    
                    HttpHeaders headers = new HttpHeaders();
                    headers.setContentType(MediaType.APPLICATION_PDF);
                    headers.setContentDispositionFormData("attachment", filename);
                    headers.setContentLength(pdfBytes.length);
                    
                    return ResponseEntity.ok()
                            .headers(headers)
                            .body(pdfBytes);
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/customer/{customerId}")
    @Operation(summary = "Obtener liquidaciones paginadas por ID de cliente")
    public Page<LiquidationWithDetailsDto> getLiquidationsByCustomer(
            @PathVariable Long customerId,
            @ModelAttribute PaginatedLiquidationRequestDto requestDto) {
        requestDto.normalizePageNumber();
        GetLiquidationsByCustomerQuery query = new GetLiquidationsByCustomerQuery(customerId, requestDto);
        return getLiquidationsByCustomerQueryHandler.execute(query);
    }

    @GetMapping("/status/{status}")
    @Operation(summary = "Obtener liquidaciones paginadas por estado")
    public Page<LiquidationWithDetailsDto> getLiquidationsByStatus(
            @PathVariable DLiquidationStatus status,
            @ModelAttribute PaginatedLiquidationRequestDto requestDto) {
        requestDto.normalizePageNumber();
        GetLiquidationsByStatusQuery query = new GetLiquidationsByStatusQuery(status, requestDto);
        return getLiquidationsByStatusQueryHandler.execute(query);
    }

    @PostMapping("/{liquidationId}/payments")
    @Operation(summary = "Agregar pago a la liquidación")
    public ResponseEntity<DLiquidation> addPayment(
            @PathVariable Long liquidationId,
            @Valid @RequestBody AddPaymentDto dto) {
        AddPaymentCommand command = new AddPaymentCommand(
                liquidationId,
                DPaymentMethod.valueOf(dto.getPaymentMethod()),
                dto.getAmount(),
                dto.getCurrency() != null ? DCurrency.valueOf(dto.getCurrency()) : DCurrency.PEN,
                dto.getEvidenceUrl());

        DLiquidation liquidation = addPaymentCommandHandler.execute(command);
        return ResponseEntity.ok(liquidation);
    }

    @PostMapping("/{liquidationId}/flight-services")
    @Operation(summary = "Agregar servicio de vuelo a la liquidación")
    public ResponseEntity<DLiquidation> addFlightService(
            @PathVariable Long liquidationId,
            @Valid @RequestBody AddFlightServiceDto dto) {
        AddFlightServiceCommand command = new AddFlightServiceCommand(liquidationId, dto);
        DLiquidation liquidation = addFlightServiceCommandHandler.execute(command);
        return ResponseEntity.ok(liquidation);
    }

    @PostMapping("/{liquidationId}/hotel-services")
    @Operation(summary = "Agregar servicio de hotel a la liquidación")
    public ResponseEntity<DLiquidation> addHotelService(
            @PathVariable Long liquidationId,
            @Valid @RequestBody AddHotelServiceDto dto) {
        AddHotelServiceCommand command = new AddHotelServiceCommand(liquidationId, dto);
        DLiquidation liquidation = addHotelServiceCommandHandler.execute(command);
        return ResponseEntity.ok(liquidation);
    }

    @PostMapping("/{liquidationId}/tour-services")
    @Operation(summary = "Agregar servicio de tour a la liquidación")
    public ResponseEntity<DLiquidation> addTourService(
            @PathVariable Long liquidationId,
            @Valid @RequestBody AddTourServiceDto dto) {
        AddTourServiceCommand command = new AddTourServiceCommand(liquidationId, dto);
        DLiquidation liquidation = addTourServiceCommandHandler.execute(command);
        return ResponseEntity.ok(liquidation);
    }

    @PostMapping("/{liquidationId}/additional-services")
    @Operation(summary = "Agregar servicio adicional a la liquidación")
    public ResponseEntity<DLiquidation> addAdditionalService(
            @PathVariable Long liquidationId,
            @Valid @RequestBody AddAdditionalServiceDto dto) {
        AddAdditionalServiceCommand command = new AddAdditionalServiceCommand(liquidationId, dto);
        DLiquidation liquidation = addAdditionalServiceCommandHandler.execute(command);
        return ResponseEntity.ok(liquidation);
    }

    @PostMapping("/{liquidationId}/incidencies")
    @Operation(summary = "Agregar incidencia a la liquidación")
    public ResponseEntity<DLiquidation> addIncidency(
            @PathVariable Long liquidationId,
            @Valid @RequestBody AddIncidencyDto dto) {
        AddIncidencyCommand command = new AddIncidencyCommand(liquidationId, dto);
        DLiquidation liquidation = addIncidencyCommandHandler.execute(command);
        return ResponseEntity.ok(liquidation);
    }

    // ==================== UPDATE ENDPOINTS ====================

    @PutMapping("/{liquidationId}/tour-services/{tourServiceId}/tours/{tourId}")
    @Operation(summary = "Actualizar un tour específico")
    public ResponseEntity<DTour> updateTour(
            @PathVariable Long liquidationId,
            @PathVariable Long tourServiceId,
            @PathVariable Long tourId,
            @Valid @RequestBody UpdateTourDto dto) {
        UpdateTourCommand command = new UpdateTourCommand(liquidationId, tourServiceId, tourId, dto);
        Tour updatedTour = updateTourCommandHandler.execute(command);
        return ResponseEntity.ok(tourMapper.toDomain(updatedTour));
    }

    @PutMapping("/{liquidationId}/hotel-services/{hotelServiceId}/bookings/{hotelBookingId}")
    @Operation(summary = "Actualizar una reserva de hotel específica")
    public ResponseEntity<DHotelBooking> updateHotelBooking(
            @PathVariable Long liquidationId,
            @PathVariable Long hotelServiceId,
            @PathVariable Long hotelBookingId,
            @Valid @RequestBody UpdateHotelBookingDto dto) {
        UpdateHotelBookingCommand command = new UpdateHotelBookingCommand(liquidationId, hotelServiceId, hotelBookingId, dto);
        HotelBooking updatedHotelBooking = updateHotelBookingCommandHandler.execute(command);
        return ResponseEntity.ok(hotelBookingMapper.toDomain(updatedHotelBooking));
    }

    @PutMapping("/{liquidationId}/flight-services/{flightServiceId}/bookings/{flightBookingId}")
    @Operation(summary = "Actualizar una reserva de vuelo específica")
    public ResponseEntity<DFlightBooking> updateFlightBooking(
            @PathVariable Long liquidationId,
            @PathVariable Long flightServiceId,
            @PathVariable Long flightBookingId,
            @Valid @RequestBody UpdateFlightBookingDto dto) {
        UpdateFlightBookingCommand command = new UpdateFlightBookingCommand(liquidationId, flightServiceId, flightBookingId, dto);
        FlightBooking updatedFlightBooking = updateFlightBookingCommandHandler.execute(command);
        return ResponseEntity.ok(flightBookingMapper.toDomain(updatedFlightBooking));
    }

    @PutMapping("/{liquidationId}/additional-services/{additionalServiceId}")
    @Operation(summary = "Actualizar un servicio adicional")
    public ResponseEntity<DAdditionalServices> updateAdditionalService(
            @PathVariable Long liquidationId,
            @PathVariable Long additionalServiceId,
            @Valid @RequestBody UpdateAdditionalServiceDto dto) {
        UpdateAdditionalServiceCommand command = new UpdateAdditionalServiceCommand(liquidationId, additionalServiceId, dto);
        AdditionalServices updatedAdditionalService = updateAdditionalServiceCommandHandler.execute(command);
        return ResponseEntity.ok(additionalServicesMapper.toDomain(updatedAdditionalService));
    }

    @PutMapping("/{liquidationId}/payments/{paymentId}")
    @Operation(summary = "Actualizar un pago")
    public ResponseEntity<DPayment> updatePayment(
            @PathVariable Long liquidationId,
            @PathVariable Long paymentId,
            @Valid @RequestBody UpdatePaymentDto dto) {
        UpdatePaymentCommand command = new UpdatePaymentCommand(liquidationId, paymentId, dto);
        Payment updatedPayment = updatePaymentCommandHandler.execute(command);
        return ResponseEntity.ok(paymentMapper.toDomain(updatedPayment));
    }

    @PutMapping("/{liquidationId}/incidencies/{incidencyId}")
    @Operation(summary = "Actualizar una incidencia")
    public ResponseEntity<DIncidency> updateIncidency(
            @PathVariable Long liquidationId,
            @PathVariable Long incidencyId,
            @Valid @RequestBody UpdateIncidencyDto dto) {
        UpdateIncidencyCommand command = new UpdateIncidencyCommand(liquidationId, incidencyId, dto);
        Incidency updatedIncidency = updateIncidencyCommandHandler.execute(command);
        return ResponseEntity.ok(incidencyMapper.toDomain(updatedIncidency));
    }

    // ============== DELETE (Soft Delete) Endpoints ==============

    @DeleteMapping("/{liquidationId}")
    @Operation(summary = "Desactivar (soft delete) una liquidación")
    public ResponseEntity<DLiquidation> deactivateLiquidation(@PathVariable Long liquidationId) {
        DeactivateLiquidationCommand command = new DeactivateLiquidationCommand(liquidationId);
        Liquidation result = deactivateLiquidationCommandHandler.execute(command);
        return ResponseEntity.ok(liquidationMapper.toDomain(result));
    }

    @DeleteMapping("/{liquidationId}/tour-services/{tourServiceId}/tours/{tourId}")
    @Operation(summary = "Desactivar (soft delete) un tour")
    public ResponseEntity<DTour> deactivateTour(
            @PathVariable Long liquidationId,
            @PathVariable Long tourServiceId,
            @PathVariable Long tourId) {
        DeactivateTourCommand command = new DeactivateTourCommand(liquidationId, tourServiceId, tourId);
        Tour result = deactivateTourCommandHandler.execute(command);
        return ResponseEntity.ok(tourMapper.toDomain(result));
    }

    @DeleteMapping("/{liquidationId}/hotel-services/{hotelServiceId}/bookings/{hotelBookingId}")
    @Operation(summary = "Desactivar (soft delete) una reserva de hotel")
    public ResponseEntity<DHotelBooking> deactivateHotelBooking(
            @PathVariable Long liquidationId,
            @PathVariable Long hotelServiceId,
            @PathVariable Long hotelBookingId) {
        DeactivateHotelBookingCommand command = new DeactivateHotelBookingCommand(liquidationId, hotelServiceId, hotelBookingId);
        HotelBooking result = deactivateHotelBookingCommandHandler.execute(command);
        return ResponseEntity.ok(hotelBookingMapper.toDomain(result));
    }

    @DeleteMapping("/{liquidationId}/flight-services/{flightServiceId}/bookings/{flightBookingId}")
    @Operation(summary = "Desactivar (soft delete) una reserva de vuelo")
    public ResponseEntity<DFlightBooking> deactivateFlightBooking(
            @PathVariable Long liquidationId,
            @PathVariable Long flightServiceId,
            @PathVariable Long flightBookingId) {
        DeactivateFlightBookingCommand command = new DeactivateFlightBookingCommand(liquidationId, flightServiceId, flightBookingId);
        FlightBooking result = deactivateFlightBookingCommandHandler.execute(command);
        return ResponseEntity.ok(flightBookingMapper.toDomain(result));
    }

    @DeleteMapping("/{liquidationId}/additional-services/{additionalServiceId}")
    @Operation(summary = "Desactivar (soft delete) un servicio adicional")
    public ResponseEntity<DAdditionalServices> deactivateAdditionalService(
            @PathVariable Long liquidationId,
            @PathVariable Long additionalServiceId) {
        DeactivateAdditionalServiceCommand command = new DeactivateAdditionalServiceCommand(liquidationId, additionalServiceId);
        AdditionalServices result = deactivateAdditionalServiceCommandHandler.execute(command);
        return ResponseEntity.ok(additionalServicesMapper.toDomain(result));
    }

    @DeleteMapping("/{liquidationId}/payments/{paymentId}")
    @Operation(summary = "Desactivar (soft delete) un pago")
    public ResponseEntity<DPayment> deactivatePayment(
            @PathVariable Long liquidationId,
            @PathVariable Long paymentId) {
        DeactivatePaymentCommand command = new DeactivatePaymentCommand(liquidationId, paymentId);
        Payment result = deactivatePaymentCommandHandler.execute(command);
        return ResponseEntity.ok(paymentMapper.toDomain(result));
    }

    @DeleteMapping("/{liquidationId}/incidencies/{incidencyId}")
    @Operation(summary = "Desactivar (soft delete) una incidencia")
    public ResponseEntity<DIncidency> deactivateIncidency(
            @PathVariable Long liquidationId,
            @PathVariable Long incidencyId) {
        DeactivateIncidencyCommand command = new DeactivateIncidencyCommand(liquidationId, incidencyId);
        Incidency result = deactivateIncidencyCommandHandler.execute(command);
        return ResponseEntity.ok(incidencyMapper.toDomain(result));
    }

    // ==================== STATUS TRANSITION ENDPOINTS ====================

    @PutMapping("/{liquidationId}/status")
    @Operation(summary = "Actualizar estado de la liquidación")
    public ResponseEntity<DLiquidation> updateLiquidationStatus(
            @PathVariable Long liquidationId,
            @Valid @RequestBody UpdateLiquidationStatusDto dto) {
        UpdateLiquidationStatusCommand command = new UpdateLiquidationStatusCommand(
                liquidationId,
                DLiquidationStatus.valueOf(dto.getTargetStatus()));
        DLiquidation result = updateLiquidationStatusCommandHandler.execute(command);
        return ResponseEntity.ok(result);
    }

    @PutMapping("/{liquidationId}/payment-status")
    @Operation(summary = "Actualizar estado de pago de la liquidación")
    public ResponseEntity<DLiquidation> updatePaymentStatus(
            @PathVariable Long liquidationId,
            @Valid @RequestBody UpdatePaymentStatusDto dto) {
        UpdatePaymentStatusCommand command = new UpdatePaymentStatusCommand(
                liquidationId,
                com.tripagencymanagement.template.liquidations.domain.enums.DPaymentStatus.valueOf(dto.getTargetStatus()));
        DLiquidation result = updatePaymentStatusCommandHandler.execute(command);
        return ResponseEntity.ok(result);
    }
}
