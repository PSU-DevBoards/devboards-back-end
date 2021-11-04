package com.psu.devboards.dbapi.permissions;

import com.psu.devboards.dbapi.models.entities.Organization;
import com.psu.devboards.dbapi.models.entities.User;
import com.psu.devboards.dbapi.models.entities.WorkItem;
import com.psu.devboards.dbapi.services.UserService;
import org.springframework.security.access.PermissionEvaluator;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * Class that handles permission checks initiated from @PreAuthorize and @PostAuthorize
 */
@Component
public class PermissionEvaluatorImpl implements PermissionEvaluator {

    private final UserService userService;
    private final Map<String, PermissionChecker> permissionCheckers;

    public PermissionEvaluatorImpl(UserService userService, WorkItemPermissionChecker workItemPermissionChecker,
                                   OrganizationPermissionChecker organizationPermissionChecker) {
        this.userService = userService;

        this.permissionCheckers = new HashMap<>();
        this.permissionCheckers.put(WorkItem.class.getSimpleName(), workItemPermissionChecker);
        this.permissionCheckers.put(Organization.class.getSimpleName(), organizationPermissionChecker);
    }

    /**
     * Checks for permission based on a domain object. Currently, always returns false because it is not needed.
     *
     * @param authentication     The authentication context.
     * @param targetDomainObject The target domain object to check permissions based on.
     * @param permission         The permission to check for.
     * @return If permission is granted or not.
     */
    @Override
    public boolean hasPermission(Authentication authentication, Object targetDomainObject, Object permission) {
        return false;
    }

    /**
     * Checks for permission on a specified domain object by id.
     *
     * @param authentication The authentication context.
     * @param targetId       The id of the object to check for permissions on.
     * @param targetType     The class name of the object to check permissions based on.
     * @param permission     The permission to check for.
     * @return If permission is granted or not.
     */
    @Override
    public boolean hasPermission(Authentication authentication, Serializable targetId, String targetType,
                                 Object permission) {
        PermissionChecker permissionChecker = getPermissionChecker(targetType);

        User user = userService.getByUserName(authentication.getName());

        return permissionChecker.hasPermission(user, targetId, (String) permission);
    }

    /**
     * Retrieves the specified permission checker for the supplied class name.
     *
     * @param className The class name to retrieve a permission checker for.
     * @return The located permission checker.
     */
    private PermissionChecker getPermissionChecker(String className) {
        PermissionChecker permissionChecker = permissionCheckers.get(className);
        if (permissionChecker == null) throw new UnsupportedOperationException("No permission check for " + className);

        return permissionChecker;
    }
}
