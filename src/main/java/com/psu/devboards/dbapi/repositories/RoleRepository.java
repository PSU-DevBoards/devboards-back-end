package com.psu.devboards.dbapi.repositories;

import com.psu.devboards.dbapi.models.entities.Role;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RoleRepository extends JpaRepository<Role, Integer> {
    Role getByName(String name);

    Optional<Role> findByName(String name);
}
