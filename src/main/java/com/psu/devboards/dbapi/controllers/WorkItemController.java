package com.psu.devboards.dbapi.controllers;

import com.psu.devboards.dbapi.models.entities.WorkItem;
import com.psu.devboards.dbapi.models.entities.WorkItemStatus;
import com.psu.devboards.dbapi.models.entities.WorkItemType;
import com.psu.devboards.dbapi.models.requests.WorkItemFullRequest;
import com.psu.devboards.dbapi.models.requests.WorkItemPatchRequest;
import com.psu.devboards.dbapi.models.specifications.WorkItemSpecification;
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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;

/**
 * REST Controller for all {@link WorkItem} related resources.
 */
@Validated
@RestController
@RequestMapping("organizations/{orgId}/work-items")
public class WorkItemController {

    private final WorkItemService workItemService;

    public WorkItemController(WorkItemService workItemService) {
        this.workItemService = workItemService;
    }

    /**
     * Gets a list of work items within an organization.
     *
     * @param orgId    The organization to retrieve work items from.
     * @param status   Optional query parameter to filter by status.
     * @param type     Optional query parameter to filter by type.
     * @param parentId Optional query parameter to filter by parent id.
     * @return The list of work items after the filters are applied.
     */
    @GetMapping
    @PreAuthorize("@workItemPermissionChecker.hasListPermission(#orgId)")
    public List<WorkItem> listWorkItems(@PathVariable Integer orgId,
                                        @RequestParam(required = false) WorkItemStatus status,
                                        @RequestParam(required = false) WorkItemType type,
                                        @RequestParam(required = false) Integer parentId) {
        WorkItemSpecification specification = WorkItemSpecification.builder()
                .organizationId(orgId)
                .status(status)
                .type(type)
                .parentId(parentId)
                .build();

        return workItemService.list(specification);
    }

    /**
     * Creates a new work item within an organization.
     *
     * @param orgId           The organization to create the work item in.
     * @param workItemRequest Request body containing the new work item values.
     * @return The created work item.
     */
    @PostMapping()
    @PreAuthorize("@workItemPermissionChecker.hasCreatePermission(#orgId)")
    public WorkItem postWorkItem(@PathVariable Integer orgId, @Valid @RequestBody WorkItemFullRequest workItemRequest) {
        workItemRequest.setOrganizationId(orgId);
        return workItemService.create(workItemRequest);
    }

    /**
     * Gets a specific work item within an organization by its id.
     *
     * @param workItemId The id of the work item to get.
     * @param orgId      The id of the organization containing the work item.
     * @return The retrieved work item.
     */
    @GetMapping("/{workItemId}")
    @PreAuthorize("hasPermission(#workItemId, 'WorkItem', 'view')")
    public WorkItem getWorkItem(@PathVariable Integer workItemId, @PathVariable String orgId) {
        return workItemService.getById(workItemId);
    }

    /**
     * Deletes a work item within an organization by its id.
     *
     * @param workItemId The id of the work item to delete.
     * @param orgId      The organization it belongs to.
     */
    @DeleteMapping("/{workItemId}")
    @PreAuthorize("hasPermission(#workItemId, 'WorkItem', 'delete')")
    public void deleteWorkItem(@PathVariable Integer workItemId, @PathVariable String orgId) {
        workItemService.deleteById(workItemId);
    }

    /**
     * Partially updates a work item within an organization by its id.
     *
     * @param workItemId      The id of the work item.
     * @param workItemRequest Request body to gather update values from.
     * @param orgId           The id of the organization the work item belongs to.
     */
    @PatchMapping("/{workItemId}")
    @PreAuthorize("hasPermission(#workItemId, 'WorkItem', 'edit')")
    public void patchWorkItem(@PathVariable Integer workItemId,
                              @Valid @RequestBody WorkItemPatchRequest workItemRequest,
                              @PathVariable String orgId) {
        workItemService.updateById(workItemId, workItemRequest);
    }
}
