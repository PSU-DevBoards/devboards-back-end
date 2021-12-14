package com.psu.devboards.dbapi.permissions;

import com.psu.devboards.dbapi.models.entities.OrganizationUser;
import com.psu.devboards.dbapi.models.entities.OrganizationUserKey;
import com.psu.devboards.dbapi.models.entities.User;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.security.Principal;

/**
 * Permission checker for OrganizationUser resources.
 */
@Component
public class OrganizationUserPermissionChecker extends PermissionChecker {

    public OrganizationUserPermissionChecker() {
        this.domainClassName = OrganizationUser.class.getSimpleName();
    }

    public boolean hasPermission(Integer organizationId, Integer userId, String permission) {
        Principal principal = SecurityContextHolder.getContext().getAuthentication();
        User user = userRepository.getByUsername(principal.getName());

        return this.hasPermission(user, new OrganizationUserKey(organizationId, userId), permission);
    }

    @Override
    protected Integer getOrganizationId(Serializable targetId) {
        return ((OrganizationUserKey) targetId).getOrganizationId();
    }
}
