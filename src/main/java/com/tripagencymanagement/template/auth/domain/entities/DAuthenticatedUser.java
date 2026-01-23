package com.tripagencymanagement.template.auth.domain.entities;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.tripagencymanagement.template.users.domain.entities.DUser;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * Domain entity representing an authenticated user.
 * Implements Spring Security's UserDetails for authentication integration.
 */
@Data
@AllArgsConstructor
public class DAuthenticatedUser implements UserDetails {
    
    private final DUser user;
    private final List<String> roles;

    public DAuthenticatedUser(DUser user) {
        this.user = user;
        this.roles = List.of("ROLE_USER"); // Default role
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return roles.stream()
                .map(SimpleGrantedAuthority::new)
                .toList();
    }

    @Override
    public String getPassword() {
        return user.getPasswordHash();
    }

    @Override
    public String getUsername() {
        return user.getEmail();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return user.getIsActive() != null && user.getIsActive();
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return user.getIsActive() != null && user.getIsActive();
    }

    public Long getUserId() {
        return user.getId();
    }

    public String getEmail() {
        return user.getEmail();
    }

    public Optional<String> getUserName() {
        return user.getUserName();
    }
}
