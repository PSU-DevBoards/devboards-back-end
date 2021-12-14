package com.psu.devboards.dbapi.permissions;

import com.psu.devboards.dbapi.models.entities.WorkItem;
import com.psu.devboards.dbapi.services.WorkItemService;
import org.springframework.stereotype.Component;

import java.io.Serializable;

/**
 * Permission checker for WorkItem resources.
 */
@Component
public class WorkItemPermissionChecker extends PermissionChecker {

    private final WorkItemService workItemService;

    public WorkItemPermissionChecker(WorkItemService workItemService) {
        this.domainClassName = WorkItem.class.getSimpleName();
        this.workItemService = workItemService;
    }

    @Override
    protected Integer getOrganizationId(Serializable targetId) {
        return workItemService.getById((Integer) targetId).getOrganization().getId();
    }
}
