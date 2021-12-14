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
 * Filter that adds the JWT authenticated user to the database if they do not currently exist. Happens before a request
 * is executed by any controller and after authentication.
 *
 * @see OncePerRequestFilter
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

    /**
     * Performs the filter operation and adds the user to the database if they do not exist.
     *
     * @param request     The incoming request.
     * @param response    The response object.
     * @param filterChain The chain of filters.
     */
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {

        Principal principal = request.getUserPrincipal();

        if (principal != null) {
            // Attempt to find the user in the database.
            User user = userService.getByUserName(principal.getName());
            if (user == null) {
                Auth0User auth0User = null;

                try {
                    // Attempt to get the user's information from the auth0 management service. This is necessary
                    // because the JWT does not contain the user email.
                    auth0User = auth0Service.findUserById(principal.getName());
                } catch (Exception ex) {
                    log.warn("Unable to get email from user {}, error message {}", principal.getName(),
                            ex.getMessage());
                }

                if (auth0User != null) {
                    // Check for an existing user by email, this would be if they were invited. If we do not find one
                    // we create a completely new user.
                    user = userService.findByEmail(auth0User.getEmail())
                            .orElse(new User(auth0User.getUserId(), auth0User.getEmail()));
                } else {
                    // We do not want to hard fail without the email, so create a user with no email anyways.
                    user = new User(principal.getName());
                }

                userService.saveUser(user);
            }
        }

        filterChain.doFilter(request, response);
    }
}
