package com.psu.devboards.dbapi.services;

import com.psu.devboards.dbapi.models.entities.Organization;
import com.psu.devboards.dbapi.models.entities.OrganizationUser;
import com.psu.devboards.dbapi.models.entities.User;
import com.psu.devboards.dbapi.models.entities.WorkItem;
import com.psu.devboards.dbapi.models.requests.OrganizationRequest;
import com.psu.devboards.dbapi.models.requests.WorkItemRequest;
import com.psu.devboards.dbapi.repositories.WorkItemRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;

@Service
public class WorkItemService {

    private final WorkItemRepository workItemRepository;

    public WorkItemService(WorkItemRepository workItemRepository) {
        this.workItemRepository = workItemRepository;
    }

    public List<WorkItem> getAllWorkItems() {
        return workItemRepository.findAll();
    }

    public WorkItem findWorkItemById(User requestUser, Integer id) {
        WorkItem workItem = workItemRepository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Work Item not found."));

        //validateViewPermission(requestUser, workItem);

        return workItem;
    }

    public WorkItem createWorkItem(String name, String type, String description, Integer priority,) {
        WorkItem workItem = new WorkItem(name, type, description,priority);

        return workItemRepository.save(workItem);
    }

    public WorkItem updateWorkItemById(Integer id, WorkItemRequest workItemRequest,User requestUser) {
        WorkItem workItem = findWorkItemById(requestUser, id);
        //validateUpdatePermission(requestUser, organization);

        workItem.setName(workItemRequest.getName());
        workItem.setType(workItemRequest.getType());
        workItem.setDescription(workItem.getDescription());
        workItem.setPriority(workItemRequest.getPriority());

        return workItemRepository.save(workItem);
    }

    private void validateDeletePermission(User user, Organization organization) {
        if (!user.equals(organization.getOwner())) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "User not allowed to delete this organization.");
        }
    }

    public void deleteOrganizationById(User requestUser, Integer id) {
        WorkItem workItem = findWorkItemById(requestUser, id);


        WorkItemRepository.delete(workItem);
    }


}
