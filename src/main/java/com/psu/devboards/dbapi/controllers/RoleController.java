package com.psu.devboards.dbapi.controllers;

import com.psu.devboards.dbapi.models.entities.Permission;
import com.psu.devboards.dbapi.models.entities.Role;
import com.psu.devboards.dbapi.services.RoleService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Set;

/**
 * REST Controller for all {@link Role} related resources.
 */
@RestController
@RequestMapping("roles")
public class RoleController {

    private final RoleService roleService;

    public RoleController(RoleService roleService) {
        this.roleService = roleService;
    }

    /**
     * Gets a list of all roles in the system.
     *
     * @return The retrieved roles.
     */
    @GetMapping
    public List<Role> getRoles() {
        return roleService.list();
    }

    /**
     * Gets a single role by its id.
     *
     * @param roleId The id of the role to get.
     * @return The retrieved role.
     */
    @GetMapping("/{roleId}")
    public Role getRole(@PathVariable Integer roleId) {
        return roleService.getById(roleId);
    }

    /**
     * Gets a roles permissions by its id.
     *
     * @param roleId The id of the role to retrieve permissions for.
     * @return A list of the roles permissions.
     */
    @GetMapping("/{roleId}/permissions")
    public Set<Permission> getRolePermissions(@PathVariable Integer roleId) {
        return roleService.getById(roleId).getPermissions();
    }
}
