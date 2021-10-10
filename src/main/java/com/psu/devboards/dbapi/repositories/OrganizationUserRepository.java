package com.psu.devboards.dbapi.repositories;

import com.psu.devboards.dbapi.models.entities.Organization;
import com.psu.devboards.dbapi.models.entities.OrganizationUser;
import com.psu.devboards.dbapi.models.entities.OrganizationUserKey;
import com.psu.devboards.dbapi.models.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface OrganizationUserRepository extends JpaRepository<OrganizationUser, OrganizationUserKey> {
    Optional<OrganizationUser> findByUserAndOrganization(User user, Organization organization);
}
