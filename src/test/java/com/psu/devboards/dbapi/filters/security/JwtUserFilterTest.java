package com.psu.devboards.dbapi.filters.security;

import com.psu.devboards.dbapi.models.entities.User;
import com.psu.devboards.dbapi.services.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.security.Principal;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class JwtUserFilterTest {

    @Mock
    UserService userService;

    @InjectMocks
    JwtUserFilter jwtUserFilter;

    HttpServletRequest request;
    HttpServletResponse response;
    FilterChain filterChain;
    Principal principal;
    User user;

    @BeforeEach
    void setUp() {
        request = mock(HttpServletRequest.class);
        response = mock(HttpServletResponse.class);
        filterChain = mock(FilterChain.class);
        principal = mock(Principal.class);
        user = new User("testUser");
    }

    @Test
    void shouldContinueWithoutErrorWhenNoPrincipal() throws ServletException, IOException {
        when(request.getUserPrincipal()).thenReturn(null);

        jwtUserFilter.doFilterInternal(request, response, filterChain);

        verify(filterChain).doFilter(request, response);
    }

    @Test
    void shouldAddUserToDatabaseIfDoesNotExistYet() throws ServletException, IOException {
        when(request.getUserPrincipal()).thenReturn(principal);
        when(principal.getName()).thenReturn("testUser");
        when(userService.findByUsername("testUser")).thenReturn(null);
        when(userService.saveUser(any())).thenReturn(null);

        jwtUserFilter.doFilterInternal(request, response, filterChain);

        verify(userService, times(1)).saveUser(user);
    }

    @Test
    void shouldNotAddUserToDatabaseIfAlreadyExists() throws ServletException, IOException {
        when(request.getUserPrincipal()).thenReturn(principal);
        when(principal.getName()).thenReturn("testUser");
        when(userService.findByUsername("testUser")).thenReturn(user);

        jwtUserFilter.doFilterInternal(request, response, filterChain);

        verify(userService, never()).saveUser(any());
    }
}