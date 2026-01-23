package com.tripagencymanagement.template.config;

import org.springframework.boot.autoconfigure.flyway.FlywayMigrationStrategy;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DatabaseConfig {

    @Bean
    public FlywayMigrationStrategy migrationStrategy() {
        // Migraciones automaticas al iniciar la aplicacion
        return flyway -> {
            flyway.repair();
            flyway.migrate();
        };
    }
}
