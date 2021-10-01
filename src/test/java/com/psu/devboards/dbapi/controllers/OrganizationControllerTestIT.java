package com.psu.devboards.dbapi.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.psu.devboards.dbapi.models.entities.Organization;
import com.psu.devboards.dbapi.models.requests.OrganizationRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class OrganizationControllerTestIT {

    @Autowired
    MockMvc mockMvc;

    @MockBean
    @SuppressWarnings("unused")
    private JwtDecoder jwtDecoder;

    ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        objectMapper = new ObjectMapper();
    }

    @Test
    @WithMockUser(username = "testUser")
    void shouldCreateOrganization() throws Exception {
        OrganizationRequest organizationRequest = new OrganizationRequest("testOrganization");

        MvcResult response = mockMvc.perform(post("/organizations")
                        .content(objectMapper.writeValueAsString(organizationRequest))
                        .accept(MediaType.APPLICATION_JSON).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk()).andReturn();
        Organization createOrganization = objectMapper
                .readValue(response.getResponse().getContentAsString(), Organization.class);

        mockMvc.perform(get("/organizations/" + createOrganization.getId())).andExpect(status().isOk());
    }
}