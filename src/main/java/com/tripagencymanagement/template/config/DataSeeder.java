package com.tripagency.ptc.ptcagencydemo.config;

import java.time.LocalDateTime;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.tripagency.ptc.ptcagencydemo.users.infrastructure.entities.Staff;
import com.tripagency.ptc.ptcagencydemo.users.infrastructure.entities.User;
import com.tripagency.ptc.ptcagencydemo.users.infrastructure.enums.Currency;
import com.tripagency.ptc.ptcagencydemo.users.infrastructure.enums.Roles;
import com.tripagency.ptc.ptcagencydemo.users.infrastructure.repositories.interfaces.IUserJpaRepository;

@Configuration
public class DataSeeder {

    private static final Logger logger = LoggerFactory.getLogger(DataSeeder.class);

    private static final String SUPERADMIN_EMAIL = "superadmin@ptc.com";
    private static final String SUPERADMIN_USERNAME = "superadmin";
    private static final String SUPERADMIN_PASSWORD = "SuperAdmin123!";

    @Bean
    CommandLineRunner initSuperAdmin(IUserJpaRepository userRepository, PasswordEncoder passwordEncoder) {
        return args -> {
            if (userRepository.existsByEmail(SUPERADMIN_EMAIL)) {
                logger.info("Superadmin user already exists, skipping seed.");
                return;
            }

            logger.info("Creating superadmin user...");

            User superadminUser = User.builder()
                    .userName(SUPERADMIN_USERNAME)
                    .email(SUPERADMIN_EMAIL)
                    .passwordHash(passwordEncoder.encode(SUPERADMIN_PASSWORD))
                    .isActive(true)
                    .build();

            Staff superadminStaff = Staff.builder()
                    .phoneNumber("999999999")
                    .salary(0.0f)
                    .currency(Currency.PEN)
                    .hireDate(LocalDateTime.now())
                    .role(Roles.SUPERADMIN)
                    .user(superadminUser)
                    .isActive(true)
                    .build();

            superadminUser.setStaff(superadminStaff);

            userRepository.save(superadminUser);

            logger.info("Superadmin user created successfully!");
            logger.info("Email: {}", SUPERADMIN_EMAIL);
            logger.info("Password: {}", SUPERADMIN_PASSWORD);
            logger.warn("IMPORTANT: Please change the superadmin password after first login!");
        };
    }
}