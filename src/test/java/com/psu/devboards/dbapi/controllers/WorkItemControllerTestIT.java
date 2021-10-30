package com.psu.devboards.dbapi.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.psu.devboards.dbapi.models.entities.Organization;
import com.psu.devboards.dbapi.models.entities.OrganizationUser;
import com.psu.devboards.dbapi.models.entities.User;
import com.psu.devboards.dbapi.models.entities.WorkItem;
import com.psu.devboards.dbapi.models.requests.WorkItemRequest;
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
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.util.Collections;
import java.util.HashSet;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class WorkItemControllerTestIT {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    WorkItemRepository workItemRepository;

    @MockBean
    @SuppressWarnings("unused")
    private JwtDecoder jwtDecoder;
    private ObjectMapper objectMapper;
    private WorkItem workItem;
    @BeforeEach
    void setUp() {
        objectMapper = new ObjectMapper();
        workItem = WorkItem.builder().name("Test").type("Test").description("Test").priority(0).build();
        workItem = workItemRepository.save(workItem);
    }
    @Test
    @WithMockUser(username = "testUser")
    void shouldReturnOkWhenAuthorized() throws Exception {
        mockMvc.perform(get("/work-items")).andExpect(status().isOk());
    }

    @Test
    void shouldReturnUnauthorizedWhenNoUser() throws Exception {
        mockMvc.perform(get("/work-items")).andExpect(status().isUnauthorized());
    }

    @Test
    @WithMockUser
    void shouldCreateWorkItem()throws Exception {
        WorkItemRequest workItemRequest = new WorkItemRequest("Test","test","Test",0);

        MvcResult response = mockMvc.perform(post("/work-items/")
                        .content(objectMapper.writeValueAsString(workItemRequest))
                        .accept(MediaType.APPLICATION_JSON).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk()).andReturn();
        WorkItem createWorkItem = objectMapper.readValue(response.getResponse().getContentAsString(), WorkItem.class);

        mockMvc.perform(get("/work-items/" + createWorkItem.getId())).andExpect(status().isOk());
    }

    @Test
    @WithMockUser(username = "testUser")
    void shouldGetWorkItemByID() throws Exception{
        MvcResult response = mockMvc.perform(get("/work-items/"+workItem.getId())).andExpect(status().isOk()).andReturn();

        WorkItem temp = objectMapper.readValue(response.getResponse().getContentAsString(),WorkItem.class);
        assertEquals(workItem,temp);
    }

    @Test
    @WithMockUser(username = "testUser")
    void shouldUpdateWorkItemByID() throws Exception{
        WorkItemRequest workItemRequest = new WorkItemRequest("Test 2","test","Test",0);

        mockMvc.perform(patch("/work-items/" + workItem.getId())
                        .content(objectMapper.writeValueAsString(workItemRequest))
                        .accept(MediaType.APPLICATION_JSON).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        MvcResult response = mockMvc.perform(get("/work-items/" + workItem.getId()))
                .andExpect(status().isOk()).andReturn();
        WorkItem updatedWorkItem = objectMapper.readValue(response.getResponse().getContentAsString(), WorkItem.class);

        assertEquals("Test 2",updatedWorkItem.getName());
    }

    @Test
    @WithMockUser(username = "testUser")
    void shouldDeleteWorkItems() throws Exception {
        mockMvc.perform(delete("/work-items/" + workItem.getId())).andExpect(status().isOk());

        mockMvc.perform(get("/work-items/" + workItem.getId())).andExpect(status().isNotFound());
    }
}
