package com.psu.devboards.dbapi.services;

import com.psu.devboards.dbapi.models.entities.User;
import com.psu.devboards.dbapi.models.entities.WorkItem;
import com.psu.devboards.dbapi.models.entities.WorkItemType;
import com.psu.devboards.dbapi.repositories.WorkItemRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class WorkItemService {

    private final WorkItemRepository workItemRepository;

    public WorkItemService(WorkItemRepository workItemRepository) {
        this.workItemRepository = workItemRepository;
    }

    /**
     * Gets all existing work items (features, stories, and tasks).
     *
     * @return A list of work items.
     */
    public List<WorkItem> getAllWorkItems() {
        return workItemRepository.findAll();
    }

    /**
     * Adds a work item with the given parameters.
     *
     * @param requestUser The user that created the work item.
     * @param name        The name of the work item.
     * @param type        The type of work item (enforced: feature, story, task).
     * @param priority    The assigned priority of the work item (lower = higher priority).
     * @return A list of work items.
     */
    public WorkItem createWorkItem(User requestUser, String name, WorkItemType type, Integer priority) {
        WorkItem item = new WorkItem(name, type, priority);
        return workItemRepository.save(item);
    }
}
