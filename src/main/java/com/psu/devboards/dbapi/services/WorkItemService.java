package com.psu.devboards.dbapi.services;

import com.psu.devboards.dbapi.models.entities.Organization;
import com.psu.devboards.dbapi.models.entities.WorkItem;
import com.psu.devboards.dbapi.models.requests.WorkItemRequest;
import com.psu.devboards.dbapi.models.specifications.WorkItemSpecification;
import com.psu.devboards.dbapi.repositories.WorkItemRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;

/**
 * Singleton service for interacting with work items.
 */
@Service
public class WorkItemService extends FilterableCrudService<Integer, WorkItem, WorkItemRequest, WorkItemSpecification> {

    private final OrganizationService organizationService;

    public WorkItemService(WorkItemRepository workItemRepository, OrganizationService organizationService) {
        super(workItemRepository);
        this.organizationService = organizationService;
    }

    @Override
    protected WorkItem updateEntityFromRequest(WorkItemRequest request, WorkItem entity) {
        Optional.ofNullable(request.getName()).ifPresent(entity::setName);
        Optional.ofNullable(request.getType()).ifPresent(entity::setType);
        Optional.ofNullable(request.getDescription()).ifPresent(entity::setDescription);
        Optional.ofNullable(request.getPriority()).ifPresent(entity::setPriority);
        Optional.ofNullable(request.getStatus()).ifPresent(entity::setStatus);

        checkSetWorkItemParent(request, entity);
        return entity;
    }

    @Override
    protected WorkItem createEntityFromRequest(WorkItemRequest request) {
        Organization organization = organizationService.getById(request.getOrganizationId());
        WorkItem workItem = WorkItem.builder()
                .organization(organization)
                .name(request.getName())
                .type(request.getType())
                .status(request.getStatus())
                .description(request.getDescription())
                .priority(request.getPriority())
                .build();

        checkSetWorkItemParent(request, workItem);

        return workItem;
    }

    /**
     * Checks to see if the request includes a parent id and attempts to set the work item parent if it does.
     *
     * @param request  The incoming request.
     * @param workItem The work item to try to set the parent on.
     */
    private void checkSetWorkItemParent(WorkItemRequest request, WorkItem workItem) {
        if (request.getParentId() != null) {
            WorkItem parent = repository.findById(request.getParentId()).orElseThrow(() ->
                    new ResponseStatusException(HttpStatus.BAD_REQUEST)
            );

            workItem.setParent(parent);
        }
    }
}
