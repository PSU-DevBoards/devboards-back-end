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

/**
 * Class used handle permission checking on a specified domain object.
 */
public abstract class PermissionChecker {

    protected String domainClassName;

    @Autowired
    protected UserRepository userRepository;

    @Autowired
    private OrganizationUserRepository organizationUserRepository;

    @Autowired
    private OrganizationService organizationService;

    /**
     * Checks for permission based on id.
     *
     * @param user       The user requesting permission.
     * @param targetId   The id of the object to check for permission on.
     * @param permission The permission to check for.
     * @return If permission is granted or not.
     */
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

    /**
     * Checks to see if the specified user has the desired role permission. For example, Organization:view.
     *
     * @param organizationId The id of the organization to check in.
     * @param user           The user who is requesting permission.
     * @param permission     The permission being requested.
     * @return If the user has the role or not.
     */
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

    /**
     * Gets the organization id of the target domain entity. Important because all permissions are tied back to an
     * organization.
     *
     * @param targetId The id of the target domain entity.
     * @return The organization id.
     */
    protected abstract Integer getOrganizationId(Serializable targetId);
}
