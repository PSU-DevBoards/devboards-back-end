package com.psu.devboards.dbapi.services;

import com.psu.devboards.dbapi.models.entities.Organization;
import com.psu.devboards.dbapi.models.entities.WorkItem;
import com.psu.devboards.dbapi.models.requests.WorkItemFullRequest;
import com.psu.devboards.dbapi.models.requests.WorkItemRequest;
import com.psu.devboards.dbapi.models.specifications.WorkItemSpecification;
import com.psu.devboards.dbapi.repositories.WorkItemRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

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
        entity.setName(request.getName());
        entity.setType(request.getType());
        entity.setDescription(request.getDescription());
        entity.setPriority(request.getPriority());
        entity.setStatus(request.getStatus());

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
