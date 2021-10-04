package com.psu.devboards.dbapi.controllers;

import com.psu.devboards.dbapi.services.RoleService;
import com.psu.devboards.dbapi.models.entities.Role;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("role")
public class RoleController {
    private final RoleService roleService;

    public RoleController(RoleService roleService){
        this.roleService = roleService;
    }

    @GetMapping
    public List<Role> listRoles(Principal principal){
        return roleService.getAllRoles();
    }
}
