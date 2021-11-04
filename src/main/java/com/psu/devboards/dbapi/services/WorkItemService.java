package com.psu.devboards.dbapi.services;

import com.psu.devboards.dbapi.models.entities.Organization;
import com.psu.devboards.dbapi.models.entities.WorkItem;
import com.psu.devboards.dbapi.models.requests.WorkItemRequest;
import com.psu.devboards.dbapi.repositories.WorkItemRepository;
import org.springframework.stereotype.Service;

import java.util.Set;

/**
 * Singleton service for interacting with work items.
 */
@Service
public class WorkItemService extends CrudService<Integer, WorkItem, WorkItemRequest> {

    private final OrganizationService organizationService;

    public WorkItemService(WorkItemRepository workItemRepository, OrganizationService organizationService) {
        super(workItemRepository);
        this.organizationService = organizationService;
    }

    public Set<WorkItem> getAllWorkItems(Integer orgId) {
        return organizationService.getById(orgId).getWorkItems();
    }

    @Override
    protected WorkItem updateEntityFromRequest(WorkItemRequest request, WorkItem entity) {
        entity.setName(request.getName());
        entity.setType(request.getType());
        entity.setDescription(request.getDescription());
        entity.setPriority(request.getPriority());

        return entity;
    }

    @Override
    protected WorkItem createEntityFromRequest(WorkItemRequest request) {
        Organization organization = organizationService.getById(request.getOrganizationId());

        return WorkItem.builder()
                .organization(organization)
                .name(request.getName())
                .type(request.getType())
                .description(request.getDescription())
                .priority(request.getPriority())
                .build();
    }
}
