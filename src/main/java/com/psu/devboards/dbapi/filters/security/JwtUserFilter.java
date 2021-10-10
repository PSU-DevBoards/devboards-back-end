package com.psu.devboards.dbapi.filters.security;

import com.psu.devboards.dbapi.models.entities.User;
import com.psu.devboards.dbapi.services.UserService;
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
@Component
public class JwtUserFilter extends OncePerRequestFilter {

    private final UserService userService;

    public JwtUserFilter(UserService userService) {
        this.userService = userService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {

        Principal principal = request.getUserPrincipal();

        if (principal != null) {
            User user = userService.getByUserName(principal.getName());
            if (user == null) {
                user = new User(principal.getName());
                userService.saveUser(user);
            }
        }

        filterChain.doFilter(request, response);
    }
}
