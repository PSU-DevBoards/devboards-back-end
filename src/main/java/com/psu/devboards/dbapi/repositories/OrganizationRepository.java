package com.psu.devboards.dbapi.repositories;

import com.psu.devboards.dbapi.models.entities.Organization;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrganizationRepository extends JpaRepository<Organization, Integer> {
}
