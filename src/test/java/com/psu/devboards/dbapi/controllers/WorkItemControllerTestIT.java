package com.psu.devboards.dbapi.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.psu.devboards.dbapi.models.WorkItemType;
import com.psu.devboards.dbapi.models.entities.Organization;
import com.psu.devboards.dbapi.models.entities.OrganizationUser;
import com.psu.devboards.dbapi.models.entities.Role;
import com.psu.devboards.dbapi.models.entities.User;
import com.psu.devboards.dbapi.models.entities.WorkItem;
import com.psu.devboards.dbapi.models.requests.WorkItemRequest;
import com.psu.devboards.dbapi.repositories.OrganizationRepository;
import com.psu.devboards.dbapi.repositories.RoleRepository;
import com.psu.devboards.dbapi.repositories.UserRepository;
import com.psu.devboards.dbapi.repositories.WorkItemRepository;
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
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.HashSet;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
class WorkItemControllerTestIT {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    UserRepository userRepository;

    @Autowired
    OrganizationRepository organizationRepository;

    @Autowired
    RoleRepository roleRepository;

    @Autowired
    WorkItemRepository workItemRepository;

    ObjectMapper objectMapper;
    User user;
    Role role;
    Organization organization;
    WorkItem workItem;

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

        workItem = WorkItem.builder()
                .description("Test")
                .name("Test")
                .priority(1)
                .type(WorkItemType.FEATURE)
                .organization(organization)
                .build();
        workItem = workItemRepository.save(workItem);
    }

    @Test
    @WithMockUser(username = "testUser")
    void shouldListWorkItems() throws Exception {
        mockMvc.perform(get("/organizations/" + organization.getId() + "/work-items"))
                .andExpect(status().isOk());
    }

    @Test
    void listShouldReturnUnauthorizedWhenNoUser() throws Exception {
        mockMvc.perform(get("/organizations/" + organization.getId() + "/work-items"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithMockUser(username = "testUser")
    void shouldGetAWorkItemById() throws Exception {
        mockMvc.perform(get("/organizations/" + organization.getId() + "/work-items/" + workItem.getId()))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(workItem)));
    }

    @Test
    @Transactional
    @WithMockUser(username = "testUser")
    void postShouldCreateAWorkItem() throws Exception {
        WorkItemRequest workItemRequest = WorkItemRequest.builder()
                .description("Created")
                .name("Created")
                .priority(1)
                .type(WorkItemType.FEATURE)
                .build();

        WorkItem expectedItem = WorkItem.builder()
                .id(2)
                .description(workItemRequest.getDescription())
                .name(workItemRequest.getName())
                .priority(workItemRequest.getPriority())
                .type(workItemRequest.getType())
                .organization(organization)
                .build();

        mockMvc.perform(post("/organizations/" + organization.getId() + "/work-items")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(workItemRequest))
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(expectedItem)));


        WorkItem persistentItem = workItemRepository.getById(expectedItem.getId());
        assertEquals(expectedItem, persistentItem);
    }

    @Test
    @Transactional
    @WithMockUser(username = "testUser")
    void shouldUpdateAWorkItem() throws Exception {
        WorkItemRequest workItemRequest = WorkItemRequest.builder()
                .description("Updated")
                .name("Updated")
                .priority(1)
                .type(WorkItemType.FEATURE)
                .build();

        mockMvc.perform(patch("/organizations/" + organization.getId() + "/work-items/" + workItem.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(workItemRequest)))
                .andExpect(status().isOk());

        WorkItem persistentItem = workItemRepository.getById(workItem.getId());
        assertEquals(persistentItem.getDescription(), workItemRequest.getDescription());
    }

    @Test
    @WithMockUser(username = "testUser")
    void shouldDeleteAWorkItem() throws Exception {
        mockMvc.perform(delete("/organizations/" + organization.getId() + "/work-items/" + workItem.getId()))
                .andExpect(status().isOk());
    }
}
