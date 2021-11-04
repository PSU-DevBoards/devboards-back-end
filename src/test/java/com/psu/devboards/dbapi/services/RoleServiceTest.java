package com.psu.devboards.dbapi.services;

import com.psu.devboards.dbapi.models.entities.Role;
import com.psu.devboards.dbapi.repositories.RoleRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class RoleServiceTest {

    @Mock
    RoleRepository roleRepository;

    @InjectMocks
    RoleService roleService;

    @Test
    void getByNameThrowsResponseStatusExceptionIfNotFound() {
        when(roleRepository.findByName("test")).thenReturn(Optional.empty());

        assertThrows(ResponseStatusException.class, () -> roleService.getByName("test"));
    }

    @Test
    void updateEntityFromRequestThrowsUnsupported() {
        when(roleRepository.findById(1)).thenReturn(Optional.of(new Role()));

        Object request = new Object();
        assertThrows(UnsupportedOperationException.class, () -> roleService.updateById(1, request));
    }

    @Test
    void createEntityFromRequestThrowsUnsupported() {
        Object request = new Object();
        assertThrows(UnsupportedOperationException.class, () -> roleService.create(request));
    }
}