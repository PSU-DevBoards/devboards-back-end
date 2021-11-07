package com.psu.devboards.dbapi.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.psu.devboards.dbapi.models.entities.Organization;
import com.psu.devboards.dbapi.models.entities.OrganizationUser;
import com.psu.devboards.dbapi.models.entities.Role;
import com.psu.devboards.dbapi.models.entities.User;
import com.psu.devboards.dbapi.models.requests.OrganizationUserRequest;
import com.psu.devboards.dbapi.repositories.OrganizationRepository;
import com.psu.devboards.dbapi.repositories.RoleRepository;
import com.psu.devboards.dbapi.repositories.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.util.Collections;
import java.util.HashSet;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
class OrganizationUserControllerTestIT {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    UserRepository userRepository;

    @Autowired
    RoleRepository roleRepository;

    @Autowired
    OrganizationRepository organizationRepository;

    ObjectMapper objectMapper;
    User user;
    User user2;
    Organization organization;
    Role role;
    Role role2;

    @MockBean
    @SuppressWarnings("unused")
    private JwtDecoder jwtDecoder;

    @BeforeEach
    void setUp() {
        objectMapper = new ObjectMapper();

        user = userRepository.save(new User("testUser"));

        organization = new Organization("testOrganization", user);
        organization = organizationRepository.save(organization);

        role = roleRepository.getByName("Scrum Master");
        organization.setUsers(new HashSet<>(Collections.singletonList(new OrganizationUser(organization, user, role))));
        organization = organizationRepository.save(organization);

        user2 = userRepository.save(new User("testUser2"));
        role2 = roleRepository.save(new Role("role2"));
    }

    @Test
    @WithMockUser(username = "testUser")
    void shouldGetOrganizationUsers() throws Exception {
        MvcResult response = mockMvc.perform(get("/organizations/" + organization.getId() + "/users"))
                .andExpect(status().isOk()).andReturn();

        assertEquals("[{\"organization_id\":1,\"user_id\":1,\"role_id\":2}]",
                response.getResponse().getContentAsString());
    }

    @Test
    @WithMockUser(username = "testUser")
    void shouldAddOrganizationUser() throws Exception {
        OrganizationUserRequest userRequest = new OrganizationUserRequest(user2.getId(), role.getId());

        mockMvc.perform(post("/organizations/" + organization.getId() + "/users")
                        .content(objectMapper.writeValueAsString(userRequest))
                        .accept(MediaType.APPLICATION_JSON).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
        MvcResult response = mockMvc.perform(get("/organizations/" + organization.getId() + "/users"))
                .andExpect(status().isOk()).andReturn();

        assertEquals("[{\"organization_id\":1,\"user_id\":1,\"role_id\":2},{\"organization_id\":1,\"user_id\":2," +
                "\"role_id\":2}]", response.getResponse().getContentAsString());
    }

    @Test
    @WithMockUser(username = "testUser")
    void shouldRemoveOrganizationUser() throws Exception {
        User userToRemove = userRepository.save(new User("userToRemove"));
        OrganizationUser organizationUserToRemove = new OrganizationUser(organization, userToRemove, role);
        organization.getUsers().add(organizationUserToRemove);
        organizationRepository.save(organization);

        mockMvc.perform(delete("/organizations/" + organization.getId() + "/users/" + userToRemove.getId()))
                .andExpect(status().isOk());
        MvcResult response = mockMvc.perform(get("/organizations/" + organization.getId() + "/users"))
                .andExpect(status().isOk()).andReturn();

        assertEquals("[{\"organization_id\":1,\"user_id\":1,\"role_id\":2}]",
                response.getResponse().getContentAsString());
    }

    @Test
    @WithMockUser(username = "testUser")
    void shouldRespond404IfAddingNonExistentUser() throws Exception {
        OrganizationUserRequest userRequest = new OrganizationUserRequest(60, 1);

        mockMvc.perform(post("/organizations/" + organization.getId() + "/users")
                        .content(objectMapper.writeValueAsString(userRequest))
                        .accept(MediaType.APPLICATION_JSON).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    @WithMockUser(username = "testUser")
    void shouldRespond404IfDeletingNonExistentUser() throws Exception {
        mockMvc.perform(delete("/organizations/" + organization.getId() + "/users/60"))
                .andExpect(status().isNotFound());
    }

    @Test
    @WithMockUser(username = "testUser")
    void shouldUpdateOrganizationUser() throws Exception {
        organization.getUsers().add(new OrganizationUser(organization, user2, role));
        organizationRepository.save(organization);

        OrganizationUserRequest organizationUserRequest = OrganizationUserRequest.builder()
                .roleId(role2.getId())
                .build();

        mockMvc.perform(patch("/organizations/" + organization.getId() + "/users/" + user2.getId())
                        .content(objectMapper.writeValueAsString(organizationUserRequest))
                        .accept(MediaType.APPLICATION_JSON).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }
}
