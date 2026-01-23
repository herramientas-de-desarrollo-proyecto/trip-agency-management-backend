package com.tripagencymanagement.template.auth.infrastructure.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.info.License;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import io.swagger.v3.oas.annotations.servers.Server;

/**
 * OpenAPI configuration for JWT authentication documentation.
 */
@OpenAPIDefinition(
    info = @Info(
        title = "PTC Agency Demo API",
        version = "1.0.0",
        description = "API REST para la gestión de la agencia de viajes PTC. " +
                      "Incluye autenticación JWT, gestión de usuarios, clientes y liquidaciones.",
        contact = @Contact(
            name = "PTC Agency Team",
            email = "support@ptcagency.com"
        ),
        license = @License(
            name = "MIT License",
            url = "https://opensource.org/licenses/MIT"
        )
    ),
    servers = {
        @Server(url = "/ptc/api", description = "API Server")
    }
)
@SecurityScheme(
    name = "bearerAuth",
    type = SecuritySchemeType.HTTP,
    scheme = "bearer",
    bearerFormat = "JWT",
    description = "Token de autenticación JWT. Obtén el token del endpoint /v1/auth/login"
)
public class OpenApiConfig {
}
