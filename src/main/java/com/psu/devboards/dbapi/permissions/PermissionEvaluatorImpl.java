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

@Component
public class PermissionEvaluatorImpl implements PermissionEvaluator {

    private final UserService userService;
    private final Map<String, PermissionChecker<?>> permissionCheckers;

    public PermissionEvaluatorImpl(UserService userService, WorkItemPermissionChecker workItemPermissionChecker,
                                   OrganizationPermissionChecker organizationPermissionChecker) {
        this.userService = userService;

        this.permissionCheckers = new HashMap<>();
        this.permissionCheckers.put(WorkItem.class.getSimpleName(), workItemPermissionChecker);
        this.permissionCheckers.put(Organization.class.getSimpleName(), organizationPermissionChecker);
    }

    @Override
    @SuppressWarnings({"rawtypes", "unchecked"})
    public boolean hasPermission(Authentication authentication, Object targetDomainObject, Object permission) {
        PermissionChecker permissionChecker = getPermissionChecker(targetDomainObject.getClass().getSimpleName());

        User user = userService.getByUserName(authentication.getName());

        return permissionChecker.hasPermission(user, targetDomainObject, (String) permission);
    }

    @Override
    public boolean hasPermission(Authentication authentication, Serializable targetId, String targetType,
                                 Object permission) {
        PermissionChecker<?> permissionChecker = getPermissionChecker(targetType);

        User user = userService.getByUserName(authentication.getName());

        return permissionChecker.hasPermission(user, targetId, (String) permission);
    }

    private PermissionChecker<?> getPermissionChecker(String className) {
        PermissionChecker<?> permissionChecker = permissionCheckers.get(className);
        if (permissionChecker == null) throw new UnsupportedOperationException("No permission check for " + className);

        return permissionChecker;
    }
}
