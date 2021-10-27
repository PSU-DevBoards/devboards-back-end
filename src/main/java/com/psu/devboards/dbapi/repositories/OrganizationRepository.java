package com.psu.devboards.dbapi.repositories;

import com.psu.devboards.dbapi.models.entities.Organization;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface OrganizationRepository extends JpaRepository<Organization, Long> {
    Optional<Organization> findById(Integer id);
    Optional<Organization> findByName(String name);
}
