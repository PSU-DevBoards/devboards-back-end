package com.psu.devboards.dbapi.controllers;

import com.psu.devboards.dbapi.models.entities.WorkItem;
import com.psu.devboards.dbapi.models.requests.WorkItemRequest;
import com.psu.devboards.dbapi.services.WorkItemService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.Set;

@Validated
@RestController
@RequestMapping("organizations/{orgId}/work-items")
public class WorkItemController {

    private final WorkItemService workItemService;

    public WorkItemController(WorkItemService workItemService) {
        this.workItemService = workItemService;
    }

    @GetMapping
    @PreAuthorize("@workItemPermissionChecker.hasListPermission(#orgId)")
    public Set<WorkItem> listWorkItems(@PathVariable Integer orgId) {
        return workItemService.getAllWorkItems(orgId);
    }

    @PostMapping()
    @PreAuthorize("@workItemPermissionChecker.hasCreatePermission(#orgId)")
    public WorkItem postWorkItem(@PathVariable Integer orgId, @Valid @RequestBody WorkItemRequest workItemRequest) {
        workItemRequest.setOrganizationId(orgId);
        return workItemService.create(workItemRequest);
    }

    @GetMapping("/{workItemId}")
    @PreAuthorize("hasPermission(#workItemId, 'WorkItem', 'view')")
    public WorkItem getWorkItem(@PathVariable Integer workItemId, @PathVariable String orgId) {
        return workItemService.getById(workItemId);
    }

    @DeleteMapping("/{workItemId}")
    @PreAuthorize("hasPermission(#workItemId, 'WorkItem', 'delete')")
    public void deleteWorkItem(@PathVariable Integer workItemId, @PathVariable String orgId) {
        workItemService.deleteById(workItemId);
    }

    @PatchMapping("/{workItemId}")
    @PreAuthorize("hasPermission(#workItemId, 'WorkItem', 'edit')")
    public void patchWorkItem(@PathVariable Integer workItemId, @Valid @RequestBody WorkItemRequest workItemRequest,
                              @PathVariable String orgId) {
        workItemService.updateById(workItemId, workItemRequest);
    }
}
