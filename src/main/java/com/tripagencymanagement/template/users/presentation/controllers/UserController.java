package com.tripagencymanagement.template.users.presentation.controllers;

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

import com.tripagencymanagement.template.general.entities.domainEntities.GeneralException;
import com.tripagencymanagement.template.general.presentation.controllers.BaseV1Controller;
import com.tripagencymanagement.template.general.presentation.exception.ErrorBody;
import com.tripagencymanagement.template.users.application.commands.CreateUserCommand;
import com.tripagencymanagement.template.users.application.commands.DeactivateUserCommand;
import com.tripagencymanagement.template.users.application.commands.UpdateUserCommand;
import com.tripagencymanagement.template.users.application.commands.handlers.CreateUserCommandHandler;
import com.tripagencymanagement.template.users.application.commands.handlers.DeactivateUserCommandHandler;
import com.tripagencymanagement.template.users.application.commands.handlers.UpdateUserCommandHandler;
import com.tripagencymanagement.template.users.application.queries.GetUserByIdQuery;
import com.tripagencymanagement.template.users.application.queries.UserPaginatedQuery;
import com.tripagencymanagement.template.users.application.queries.handlers.GetUserByIdQueryHandler;
import com.tripagencymanagement.template.users.application.queries.handlers.UserPaginatedQueryHandler;
import com.tripagencymanagement.template.users.domain.entities.DUser;
import com.tripagencymanagement.template.users.presentation.dto.CreateUserDto;
import com.tripagencymanagement.template.users.presentation.dto.PaginatedUserRequestDto;
import com.tripagencymanagement.template.users.presentation.dto.UpdateUserDto;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/users")
@Tag(name = "Users", description = "Endpoints para el manejo de usuarios")
public class UserController extends BaseV1Controller {

    private final CreateUserCommandHandler createUserCommandHandler;
    private final UpdateUserCommandHandler updateUserCommandHandler;
    private final DeactivateUserCommandHandler deactivateUserCommandHandler;
    private final GetUserByIdQueryHandler getUserByIdQueryHandler;
    private final UserPaginatedQueryHandler userPaginatedQueryHandler;

    public UserController(CreateUserCommandHandler createUserCommandHandler,
                         UpdateUserCommandHandler updateUserCommandHandler,
                         DeactivateUserCommandHandler deactivateUserCommandHandler,
                         GetUserByIdQueryHandler getUserByIdQueryHandler,
                         UserPaginatedQueryHandler userPaginatedQueryHandler) {
        this.createUserCommandHandler = createUserCommandHandler;
        this.updateUserCommandHandler = updateUserCommandHandler;
        this.deactivateUserCommandHandler = deactivateUserCommandHandler;
        this.getUserByIdQueryHandler = getUserByIdQueryHandler;
        this.userPaginatedQueryHandler = userPaginatedQueryHandler;
    }

    @PostMapping("")
    @Operation(
        summary = "Crear un nuevo usuario",
        description = "Crea un nuevo usuario con la información proporcionada.",
        responses = {
            @ApiResponse(responseCode = "200", description = "Usuario creado exitosamente", content = @Content(mediaType = "application/json", schema = @Schema(implementation = DUser.class))),
            @ApiResponse(responseCode = "400", description = "Solicitud inválida", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorBody.class)))
        }
    )
    public DUser createUser(@Valid @RequestBody CreateUserDto dto) {
        return createUserCommandHandler.execute(new CreateUserCommand(dto));
    }

    @GetMapping("/{id}")
    @Operation(
        summary = "Obtener usuario por ID",
        description = "Obtiene un usuario específico por su ID.",
        responses = {
            @ApiResponse(responseCode = "200", description = "Usuario encontrado exitosamente"),
            @ApiResponse(responseCode = "404", description = "Usuario no encontrado", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorBody.class)))
        }
    )
    public DUser getUserById(@PathVariable Long id) {
        return getUserByIdQueryHandler.execute(new GetUserByIdQuery(id));
    }

    @GetMapping("/paginados")
    @Operation(
        summary = "Obtener usuarios paginados",
        description = "Obtiene una lista paginada de usuarios según la configuración proporcionada.",
        responses = {
            @ApiResponse(responseCode = "200", description = "Lista paginada de usuarios obtenida exitosamente"),
            @ApiResponse(responseCode = "400", description = "Solicitud inválida", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorBody.class)))
        }
    )
    public Page<DUser> getPaginatedUsers(@ModelAttribute PaginatedUserRequestDto requestDto) throws GeneralException {
        requestDto.normalizePageNumber();
        UserPaginatedQuery query = new UserPaginatedQuery(requestDto);
        return userPaginatedQueryHandler.execute(query);
    }

    @PutMapping("/{id}")
    @Operation(
        summary = "Actualizar usuario",
        description = "Actualiza la información de un usuario existente.",
        responses = {
            @ApiResponse(responseCode = "200", description = "Usuario actualizado exitosamente", content = @Content(mediaType = "application/json", schema = @Schema(implementation = DUser.class))),
            @ApiResponse(responseCode = "400", description = "Solicitud inválida", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorBody.class))),
            @ApiResponse(responseCode = "404", description = "Usuario no encontrado", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorBody.class)))
        }
    )
    public DUser updateUser(@PathVariable Long id, @Valid @RequestBody UpdateUserDto dto) {
        return updateUserCommandHandler.execute(new UpdateUserCommand(id, dto));
    }

    @DeleteMapping("/{id}")
    @Operation(
        summary = "Desactivar usuario",
        description = "Desactiva (soft delete) un usuario existente.",
        responses = {
            @ApiResponse(responseCode = "200", description = "Usuario desactivado exitosamente", content = @Content(mediaType = "application/json", schema = @Schema(implementation = DUser.class))),
            @ApiResponse(responseCode = "404", description = "Usuario no encontrado", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorBody.class)))
        }
    )
    public DUser deactivateUser(@PathVariable Long id) {
        return deactivateUserCommandHandler.execute(new DeactivateUserCommand(id));
    }
}
