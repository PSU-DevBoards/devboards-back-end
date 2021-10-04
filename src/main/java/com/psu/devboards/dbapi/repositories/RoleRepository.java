package com.psu.devboards.dbapi.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import com.psu.devboards.dbapi.models.entities.Role;
import java.util.List;

public interface RoleRepository extends JpaRepository<Role,Long> {

    @Override
    List<Role> findAll();
}
