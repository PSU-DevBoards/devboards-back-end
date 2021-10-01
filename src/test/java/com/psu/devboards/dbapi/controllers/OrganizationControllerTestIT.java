package com.psu.devboards.dbapi.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.psu.devboards.dbapi.models.entities.Organization;
import com.psu.devboards.dbapi.models.entities.User;
import com.psu.devboards.dbapi.models.requests.OrganizationRequest;
import com.psu.devboards.dbapi.repositories.OrganizationRepository;
import com.psu.devboards.dbapi.repositories.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.web.server.ResponseStatusException;

import java.util.Collections;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
class OrganizationControllerTestIT {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    UserRepository userRepository;

    @Autowired
    OrganizationRepository organizationRepository;

    @MockBean
    @SuppressWarnings("unused")
    private JwtDecoder jwtDecoder;

    ObjectMapper objectMapper;
    User user;
    Organization organization;

    @BeforeEach
    void setUp() {
        objectMapper = new ObjectMapper();

        user = userRepository.save(new User("testUser"));
        organization = organizationRepository
                .save(new Organization("testOrganization", user, Collections.singletonList(user)));
    }

    @Test
    @WithMockUser(username = "testUser")
    void shouldCreateOrganization() throws Exception {
        OrganizationRequest organizationRequest = new OrganizationRequest("newOrganization");

        MvcResult response = mockMvc.perform(post("/organizations")
                        .content(objectMapper.writeValueAsString(organizationRequest))
                        .accept(MediaType.APPLICATION_JSON).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk()).andReturn();
        Organization createOrganization = objectMapper
                .readValue(response.getResponse().getContentAsString(), Organization.class);

        mockMvc.perform(get("/organizations/" + createOrganization.getId())).andExpect(status().isOk());
    }

    @Test
    @WithMockUser(username = "testUser")
    void shouldUpdateOrganization() throws Exception {
        OrganizationRequest organizationRequest = new OrganizationRequest("updatedOrganization");

        mockMvc.perform(patch("/organizations/" + organization.getId())
                        .content(objectMapper.writeValueAsString(organizationRequest))
                        .accept(MediaType.APPLICATION_JSON).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        MvcResult response = mockMvc.perform(get("/organizations/" + organization.getId()))
                .andExpect(status().isOk()).andReturn();
        Organization updatedOrganization = objectMapper
                .readValue(response.getResponse().getContentAsString(), Organization.class);

        assertEquals("updatedOrganization", updatedOrganization.getName());
    }

    @Test
    @WithMockUser(username = "testUser")
    void shouldDeleteOrganization() throws Exception {
        mockMvc.perform(delete("/organizations/" + organization.getId())).andExpect(status().isOk());

        mockMvc.perform(get("/organizations/" + organization.getId())).andExpect(status().isNotFound());
    }

    @Test
    @WithMockUser(username = "testUser")
    void shouldGetOrganization() throws Exception {
        MvcResult response = mockMvc.perform(get("/organizations/" + organization.getId()))
                .andExpect(status().isOk()).andReturn();
        Organization retrievedOrganization = objectMapper
                .readValue(response.getResponse().getContentAsString(), Organization.class);

        assertEquals(organization.getId(), retrievedOrganization.getId());
    }
}