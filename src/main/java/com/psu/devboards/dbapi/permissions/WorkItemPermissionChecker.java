package com.psu.devboards.dbapi.permissions;

import com.psu.devboards.dbapi.models.entities.WorkItem;
import com.psu.devboards.dbapi.repositories.WorkItemRepository;
import org.springframework.stereotype.Component;

import java.io.Serializable;

@Component
public class WorkItemPermissionChecker extends PermissionChecker<WorkItem> {

    private final WorkItemRepository workItemRepository;

    public WorkItemPermissionChecker(WorkItemRepository workItemRepository) {
        this.domainClassName = WorkItem.class.getSimpleName();
        this.workItemRepository = workItemRepository;
    }

    @Override
    protected Integer getOrganizationId(Serializable targetId) {
        WorkItem workItem = workItemRepository.findById((Integer) targetId).orElse(null);
        if (workItem == null) return null;

        return workItem.getOrganization().getId();
    }
}
