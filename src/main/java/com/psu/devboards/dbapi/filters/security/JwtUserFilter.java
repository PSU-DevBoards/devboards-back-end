package com.psu.devboards.dbapi.filters.security;

import com.psu.devboards.dbapi.models.entities.User;
import com.psu.devboards.dbapi.models.responses.auth0.Auth0User;
import com.psu.devboards.dbapi.services.Auth0Service;
import com.psu.devboards.dbapi.services.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.security.Principal;

/**
 * Filter that adds the JWT authenticated user to the database if they do not currently exist.
 */
@Slf4j
@Component
public class JwtUserFilter extends OncePerRequestFilter {

    private final UserService userService;
    private final Auth0Service auth0Service;

    public JwtUserFilter(UserService userService, Auth0Service auth0Service) {
        this.userService = userService;
        this.auth0Service = auth0Service;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {

        Principal principal = request.getUserPrincipal();

        if (principal != null) {
            User user = userService.getByUserName(principal.getName());
            if (user == null) {
                Auth0User auth0User = null;

                try {
                    auth0User = auth0Service.findUserById(principal.getName());
                } catch (Exception ex) {
                    log.warn("Unable to get email from user {}, error message {}", principal.getName(),
                            ex.getMessage());
                }

                // Check for an existing user by email, this would be if they were invited. If we do not find one
                // we create a completely new user.
                if (auth0User != null) {
                    user = userService.findByEmail(auth0User.getEmail())
                            .orElse(new User(auth0User.getUserId(), auth0User.getEmail()));
                } else {
                    user = new User(principal.getName());
                }

                userService.saveUser(user);
            }
        }

        filterChain.doFilter(request, response);
    }
}
