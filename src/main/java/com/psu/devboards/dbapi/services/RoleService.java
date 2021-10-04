package com.psu.devboards.dbapi.services;

import com.psu.devboards.dbapi.repositories.RoleRepository;
import com.psu.devboards.dbapi.models.entities.Role;

import java.util.List;
public class RoleService {
    private final RoleRepository roleRepository;

    public RoleService(RoleRepository roleRepository){
        this.roleRepository = roleRepository;
    }

    public List<Role> getAllRoles(){
        return roleRepository.findAll();
    }
}
