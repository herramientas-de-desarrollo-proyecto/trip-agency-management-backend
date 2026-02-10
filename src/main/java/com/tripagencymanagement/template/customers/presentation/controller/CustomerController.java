package com.tripagency.ptc.ptcagencydemo.customers.presentation.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.tripagency.ptc.ptcagencydemo.customers.application.commands.CreateCustomerCommand;
import com.tripagency.ptc.ptcagencydemo.customers.application.commands.DeactivateCustomerCommand;
import com.tripagency.ptc.ptcagencydemo.customers.application.commands.UpdateCustomerCommand;
import com.tripagency.ptc.ptcagencydemo.customers.application.commands.handlers.CreateCustomerCommandHandler;
import com.tripagency.ptc.ptcagencydemo.customers.application.commands.handlers.DeactivateCustomerCommandHandler;
import com.tripagency.ptc.ptcagencydemo.customers.application.commands.handlers.UpdateCustomerCommandHandler;
import com.tripagency.ptc.ptcagencydemo.customers.application.queries.CustomerPaginatedQuery;
import com.tripagency.ptc.ptcagencydemo.customers.application.queries.GetAllCustomersQuery;
import com.tripagency.ptc.ptcagencydemo.customers.application.queries.handlers.CustomerPaginatedQueryHandler;
import com.tripagency.ptc.ptcagencydemo.customers.application.queries.handlers.GetAllCustomersQueryHandler;
import com.tripagency.ptc.ptcagencydemo.customers.domain.entities.DCustomer;
import com.tripagency.ptc.ptcagencydemo.customers.presentation.dto.CreateCustomerDto;
import com.tripagency.ptc.ptcagencydemo.customers.presentation.dto.PaginatedCustomerRequestDto;
import com.tripagency.ptc.ptcagencydemo.customers.presentation.dto.UpdateCustomerDto;
import com.tripagency.ptc.ptcagencydemo.general.entities.domainEntities.GeneralException;
import com.tripagency.ptc.ptcagencydemo.general.presentation.controllers.BaseV1Controller;
import com.tripagency.ptc.ptcagencydemo.general.presentation.exception.ErrorBody;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;


@RestController
@RequestMapping("/clientes")
@Tag(name = "Clientes", description = "Endpoints para el manejo de clientes")
public class CustomerController extends BaseV1Controller {

    private final CustomerPaginatedQueryHandler customerPaginatedQueryHandler;
    private final CreateCustomerCommandHandler createCustomerCommandHandler;
    private final UpdateCustomerCommandHandler updateCustomerCommandHandler;
    private final DeactivateCustomerCommandHandler deactivateCustomerCommandHandler;
    private final GetAllCustomersQueryHandler getAllCustomersQueryHandler;

    @Autowired
    public CustomerController(CustomerPaginatedQueryHandler customerPaginatedQueryHandler, 
                             CreateCustomerCommandHandler createCustomerCommandHandler, 
                             UpdateCustomerCommandHandler updateCustomerCommandHandler,
                             DeactivateCustomerCommandHandler deactivateCustomerCommandHandler,
                             GetAllCustomersQueryHandler getAllCustomersQueryHandler) {
        this.customerPaginatedQueryHandler = customerPaginatedQueryHandler;
        this.createCustomerCommandHandler = createCustomerCommandHandler;
        this.updateCustomerCommandHandler = updateCustomerCommandHandler;
        this.deactivateCustomerCommandHandler = deactivateCustomerCommandHandler;
        this.getAllCustomersQueryHandler = getAllCustomersQueryHandler;
    }

    @GetMapping("")
    @Operation(
        summary = "Obtener todos los clientes",
        description = "Obtiene una lista completa de todos los clientes sin paginación.",
        responses = {
            @ApiResponse(responseCode = "200", description = "Lista de clientes obtenida exitosamente"),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorBody.class)))
        }
    )
    public List<DCustomer> getAllCustomers() {
        GetAllCustomersQuery query = new GetAllCustomersQuery();
        return getAllCustomersQueryHandler.execute(query);
    }

    @GetMapping("/paginados")
    @Operation(summary = "Obtener clientes paginados", description = "Obtiene una lista paginada de clientes según la configuración proporcionada.", responses = {
        @ApiResponse(responseCode = "200", description = "Lista paginada de clientes obtenida exitosamente"),
        @ApiResponse(responseCode = "400", description = "Solicitud inválida", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorBody.class))),
    })
    public Page<DCustomer> getPaginatedCustomers(@ModelAttribute PaginatedCustomerRequestDto requestDto) throws GeneralException {
        requestDto.normalizePageNumber();
        CustomerPaginatedQuery query = new CustomerPaginatedQuery(requestDto);
        return customerPaginatedQueryHandler.execute(query);
    }

    @PostMapping("")
    @Operation(
        summary = "Crear un nuevo cliente",
        description = "Crea un nuevo cliente con la información proporcionada.",
        responses = {
            @ApiResponse(responseCode = "200", description = "Cliente creado exitosamente", content = @Content(mediaType = "application/json", schema = @Schema(implementation = DCustomer.class))),
            @ApiResponse(responseCode = "400", description = "Solicitud inválida", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorBody.class)))
        }
    )
    public DCustomer saveCustomers(@RequestBody CreateCustomerDto entity) {
        DCustomer createdCustomer = createCustomerCommandHandler.execute(new CreateCustomerCommand(entity));
        return createdCustomer;
    }

    @PutMapping("/{id}")
    @Operation(
        summary = "Actualizar cliente",
        description = "Actualiza la información de un cliente existente.",
        responses = {
            @ApiResponse(responseCode = "200", description = "Cliente actualizado exitosamente", content = @Content(mediaType = "application/json", schema = @Schema(implementation = DCustomer.class))),
            @ApiResponse(responseCode = "400", description = "Solicitud inválida", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorBody.class))),
            @ApiResponse(responseCode = "404", description = "Cliente no encontrado", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorBody.class)))
        }
    )
    public DCustomer updateCustomer(@PathVariable Long id, @Valid @RequestBody UpdateCustomerDto dto) {
        return updateCustomerCommandHandler.execute(new UpdateCustomerCommand(id, dto));
    }

    @DeleteMapping("/{id}")
    @Operation(
        summary = "Desactivar cliente",
        description = "Desactiva (soft delete) un cliente existente.",
        responses = {
            @ApiResponse(responseCode = "200", description = "Cliente desactivado exitosamente", content = @Content(mediaType = "application/json", schema = @Schema(implementation = DCustomer.class))),
            @ApiResponse(responseCode = "404", description = "Cliente no encontrado", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorBody.class)))
        }
    )
    public DCustomer deactivateCustomer(@PathVariable Long id) {
        return deactivateCustomerCommandHandler.execute(new DeactivateCustomerCommand(id));
    }


}
