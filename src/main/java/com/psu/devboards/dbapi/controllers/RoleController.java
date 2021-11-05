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

@RestController
@RequestMapping("roles")
public class RoleController {

    private final RoleService roleService;

    public RoleController(RoleService roleService) {
        this.roleService = roleService;
    }

    @GetMapping
    public List<Role> getRoles() {
        return roleService.list();
    }

    @GetMapping("/{roleId}")
    public Role getRole(@PathVariable Integer roleId) {
        return roleService.getById(roleId);
    }

    @GetMapping("/{roleId}/permissions")
    public Set<Permission> getRolePermissions(@PathVariable Integer roleId) {
        return roleService.getById(roleId).getPermissions();
    }
}
