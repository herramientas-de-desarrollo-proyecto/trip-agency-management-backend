package com.tripagencymanagement.template.auth.infrastructure.security;

import java.io.IOException;

import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.tripagencymanagement.template.auth.application.services.CustomUserDetailsService;
import com.tripagencymanagement.template.auth.application.services.JwtService;
import com.tripagencymanagement.template.auth.domain.repositories.ISessionRepository;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * JWT Authentication Filter that intercepts requests to validate JWT tokens.
 * Extracts the token from the Authorization header and validates it.
 */
@Component
@RequiredArgsConstructor
@Slf4j
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtService jwtService;
    private final CustomUserDetailsService userDetailsService;
    private final ISessionRepository sessionRepository;

    @Override
    protected void doFilterInternal(
            @NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull FilterChain filterChain
    ) throws ServletException, IOException {
        
        final String authHeader = request.getHeader("Authorization");
        String jwt;
        final String userEmail;

        // DEBUG: Log request details
        log.info("🔐 [JwtFilter] Request: {} {}", request.getMethod(), request.getRequestURI());
        log.info("🔐 [JwtFilter] Authorization header present: {}", authHeader != null);
        if (authHeader != null) {
            log.info("🔐 [JwtFilter] Auth header starts with Bearer: {}", authHeader.startsWith("Bearer "));
            log.info("🔐 [JwtFilter] Auth header length: {}", authHeader.length());
        }

        // Check if Authorization header exists and starts with Bearer
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            log.warn("🔐 [JwtFilter] No valid Authorization header, proceeding without authentication");
            filterChain.doFilter(request, response);
            return;
        }

        // Remove "Bearer " prefix and clean any whitespace
        jwt = authHeader.substring(7).trim().replaceAll("\\s+", "");
        
        if (jwt.isEmpty()) {
            log.warn("🔐 [JwtFilter] Empty token after cleanup, proceeding without authentication");
            filterChain.doFilter(request, response);
            return;
        }

        try {
            // Validate token format and signature first
            if (!jwtService.validateToken(jwt)) {
                log.debug("Invalid JWT token");
                filterChain.doFilter(request, response);
                return;
            }

            // Check if it's an access token
            if (!jwtService.isAccessToken(jwt)) {
                log.debug("Token is not an access token");
                filterChain.doFilter(request, response);
                return;
            }

            userEmail = jwtService.extractUsername(jwt);

            // Check if user is already authenticated
            if (userEmail != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                UserDetails userDetails = userDetailsService.loadUserByUsername(userEmail);

                // Validate token and check session in database
                if (jwtService.isTokenValid(jwt, userDetails) && isSessionValid(jwt)) {
                    UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                            userDetails,
                            null,
                            userDetails.getAuthorities()
                    );
                    authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    SecurityContextHolder.getContext().setAuthentication(authToken);
                    log.debug("User authenticated successfully: {}", userEmail);
                }
            }
        } catch (Exception e) {
            log.error("Error processing JWT token: {}", e.getMessage());
        }

        filterChain.doFilter(request, response);
    }

    /**
     * Checks if the session for this token is still valid in the database
     */
    private boolean isSessionValid(String token) {
        return sessionRepository.findByToken(token)
                .map(session -> !session.getIsRevoked() && session.getIsActive())
                .orElse(false);
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        String path = request.getServletPath();
        String method = request.getMethod();
        
        // Skip filter for public endpoints (paths are relative to context-path)
        return path.startsWith("/auth/login") || 
               path.startsWith("/auth/register") ||
               path.startsWith("/auth/refresh") ||
               path.startsWith("/swagger-ui") ||
               path.startsWith("/scalar") ||
               path.startsWith("/api-docs") ||
               path.startsWith("/v3/api-docs") ||
               (path.equals("/users") && "POST".equalsIgnoreCase(method)); // Allow POST /users for registration
    }
}
