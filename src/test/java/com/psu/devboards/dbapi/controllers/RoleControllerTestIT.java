package com.psu.devboards.dbapi.controllers;

import com.psu.devboards.dbapi.models.entities.Permission;
import com.psu.devboards.dbapi.models.entities.Role;
import com.psu.devboards.dbapi.models.entities.User;
import com.psu.devboards.dbapi.repositories.PermissionRepository;
import com.psu.devboards.dbapi.repositories.RoleRepository;
import com.psu.devboards.dbapi.repositories.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.io.UnsupportedEncodingException;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
class RoleControllerTestIT {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    UserRepository userRepository;

    @Autowired
    RoleRepository roleRepository;

    @Autowired
    PermissionRepository permissionRepository;

    User user;
    Role role;
    Permission permission;

    @BeforeEach
    void setUp() {
        user = userRepository.save(new User("testUser"));

        role = roleRepository.save(new Role("testRole"));
        permission = permissionRepository.save(new Permission("testPermission"));

        role.setPermissions(Collections.singleton(permission));
        role = roleRepository.save(role);
    }

    @Test
    @WithMockUser(username = "testUser")
    void getRolesShouldReturnRoles() throws Exception {
        MvcResult response = mockMvc.perform(get("/roles"))
                .andExpect(status().isOk()).andReturn();

        assertEquals("[{\"id\":1,\"name\":\"Developer\"},{\"id\":2,\"name\":\"Scrum Master\"},{\"id\":3," +
                "\"name\":\"testRole\"}]", response.getResponse().getContentAsString());
    }

    @Test
    @WithMockUser(username = "testUser")
    void getRoleShouldReturnRole() throws Exception {
        MvcResult response = mockMvc.perform(get("/roles/" + role.getId()))
                .andExpect(status().isOk()).andReturn();

        assertEquals("{\"id\":3,\"name\":\"testRole\"}", response.getResponse().getContentAsString());
    }

    @Test
    @WithMockUser(username = "testUser")
    void getRolePermissionsShouldReturnRolePermissions() throws Exception {
        MvcResult response = mockMvc.perform(get("/roles/" + role.getId() + "/permissions"))
                .andExpect(status().isOk()).andReturn();

        assertEquals("[{\"id\":11,\"key\":\"testPermission\"}]", response.getResponse().getContentAsString());
    }
}