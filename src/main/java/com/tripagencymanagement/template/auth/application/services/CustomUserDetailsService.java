package com.tripagencymanagement.template.auth.application.services;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.tripagencymanagement.template.auth.domain.entities.DAuthenticatedUser;
import com.tripagencymanagement.template.users.domain.entities.DUser;
import com.tripagencymanagement.template.users.domain.repositories.IUserRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * Service that implements Spring Security's UserDetailsService.
 * Loads user details from the database for authentication.
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class CustomUserDetailsService implements UserDetailsService {

    private final IUserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        log.debug("Loading user by email: {}", email);
        
        DUser user = userRepository.findByEmailIncludingInactive(email);
        
        if (user == null) {
            log.warn("User not found with email: {}", email);
            throw new UsernameNotFoundException("Usuario no encontrado con email: " + email);
        }

        if (user.getIsActive() == null || !user.getIsActive()) {
            log.warn("User account is disabled: {}", email);
            throw new UsernameNotFoundException("La cuenta de usuario está desactivada: " + email);
        }

        log.debug("User loaded successfully: {}", email);
        return new DAuthenticatedUser(user);
    }

    /**
     * Loads user by ID
     * @param userId the user ID
     * @return UserDetails for the user
     */
    public UserDetails loadUserById(Long userId) {
        DUser user = userRepository.findById(userId);
        
        if (user == null) {
            throw new UsernameNotFoundException("Usuario no encontrado con ID: " + userId);
        }

        if (user.getIsActive() == null || !user.getIsActive()) {
            throw new UsernameNotFoundException("La cuenta de usuario está desactivada");
        }

        return new DAuthenticatedUser(user);
    }
}
