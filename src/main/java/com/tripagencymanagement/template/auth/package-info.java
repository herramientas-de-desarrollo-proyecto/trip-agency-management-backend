/**
 * Authentication module following Domain-Driven Design (DDD) with CQRS patterns.
 * 
 * This module provides JWT-based authentication with session management.
 * 
 * <h2>Package Structure:</h2>
 * <ul>
 *   <li><b>domain/</b> - Domain entities, repositories interfaces, and business logic</li>
 *   <li><b>application/</b> - Commands, queries, handlers, and services</li>
 *   <li><b>infrastructure/</b> - JPA entities, repository implementations, mappers</li>
 *   <li><b>presentation/</b> - REST controllers and DTOs</li>
 * </ul>
 * 
 * @author PTC Agency Demo
 * @since 1.0.0
 */
@ApplicationModule(
    type = ApplicationModule.Type.OPEN,
    displayName = "Authentication Module",
    allowedDependencies = {"users", "general", "config"}
)
package com.tripagencymanagement.template.auth;

import org.springframework.modulith.ApplicationModule;
