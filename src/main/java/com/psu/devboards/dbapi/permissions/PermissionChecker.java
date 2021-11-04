package com.psu.devboards.dbapi.permissions;

import com.psu.devboards.dbapi.models.entities.Organization;
import com.psu.devboards.dbapi.models.entities.User;
import com.psu.devboards.dbapi.repositories.OrganizationUserRepository;
import com.psu.devboards.dbapi.repositories.UserRepository;
import com.psu.devboards.dbapi.services.OrganizationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;

import java.io.Serializable;
import java.security.Principal;

public abstract class PermissionChecker<T> {

    protected String domainClassName;

    @Autowired
    protected UserRepository userRepository;

    @Autowired
    private OrganizationUserRepository organizationUserRepository;

    @Autowired
    private OrganizationService organizationService;

    public boolean hasPermission(User user, T domainObject, String permission) {
        throw new UnsupportedOperationException("hasPermission by domain object not supported for " + domainClassName);
    }

    public boolean hasPermission(User user, Serializable targetId, String permission) {
        switch (permission) {
            case "view":
                return hasViewPermission(user, targetId);
            case "edit":
                return hasEditPermission(user, targetId);
            case "delete":
                return hasDeletePermission(user, targetId);
            default:
                return false;
        }
    }

    protected boolean hasRolePermission(Integer organizationId, User user, String permission) {
        Organization organization = organizationService.getById(organizationId);

        return organizationUserRepository.findByUserAndOrganization(user, organization)
                .flatMap(organizationUser -> organizationUser.getRole().getPermissions().stream()
                        .filter(perm -> perm.getKey().equals(domainClassName + ":" + permission))
                        .findAny()).isPresent();
    }

    public boolean hasCreatePermission(Integer organizationId) {
        Principal principal = SecurityContextHolder.getContext().getAuthentication();
        User user = userRepository.getByUsername(principal.getName());

        return hasRolePermission(organizationId, user, "create");
    }

    public boolean hasListPermission(Integer organizationId) {
        Principal principal = SecurityContextHolder.getContext().getAuthentication();
        User user = userRepository.getByUsername(principal.getName());

        return hasRolePermission(organizationId, user, "list");
    }

    protected boolean hasViewPermission(User user, Serializable targetId) {
        return hasRolePermission(getOrganizationId(targetId), user, "view");
    }

    protected boolean hasEditPermission(User user, Serializable targetId) {
        return hasRolePermission(getOrganizationId(targetId), user, "edit");
    }

    protected boolean hasDeletePermission(User user, Serializable targetId) {
        return hasRolePermission(getOrganizationId(targetId), user, "delete");
    }

    protected abstract Integer getOrganizationId(Serializable targetId);
}
