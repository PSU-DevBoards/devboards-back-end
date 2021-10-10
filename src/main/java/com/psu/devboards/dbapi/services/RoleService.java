package com.psu.devboards.dbapi.services;

import com.psu.devboards.dbapi.models.entities.Role;
import com.psu.devboards.dbapi.repositories.RoleRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

/**
 * Singleton service for interacting with roles. Currently, does not perform any authorization checks because all users
 * should be able to view roles and permissions.
 */
@Service
public class RoleService {

    private final RoleRepository roleRepository;

    public RoleService(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    /**
     * Returns a list of all roles currently persisted.
     * @return The list of roles.
     */
    public List<Role> listRoles() {
        return roleRepository.findAll();
    }

    /**
     * Finds a role by its id and throws a response status exception if it does not find the role.
     * @param id The role id to locate.
     * @return The located role.
     */
    public Role findRoleById(Integer id) {
        return roleRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Role not found."));
    }
}
