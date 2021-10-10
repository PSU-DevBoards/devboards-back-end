package com.psu.devboards.dbapi.repositories;

import com.psu.devboards.dbapi.models.entities.Role;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleRepository extends JpaRepository<Role, Integer> {
}
