package com.psu.devboards.dbapi.repositories;

import com.psu.devboards.dbapi.models.entities.Permission;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PermissionRepository extends JpaRepository<Permission, Integer> {
}
