package com.psu.devboards.dbapi.services;

import com.psu.devboards.dbapi.models.entities.Organization;
import com.psu.devboards.dbapi.models.entities.User;
import com.psu.devboards.dbapi.repositories.OrganizationRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class OrganizationServiceTest {

    @Mock
    OrganizationRepository organizationRepository;

    @InjectMocks
    OrganizationService organizationService;

    User user;

    @BeforeEach
    void setUp() {
        user = new User("testUser");
    }

    @Test
    void shouldCreateOrganization() {
        String name = "newOrganization";
        Organization organization = new Organization(name, user);

        organizationService.createOrganization(user, name);

        verify(organizationRepository, times(1)).save(organization);
    }
}