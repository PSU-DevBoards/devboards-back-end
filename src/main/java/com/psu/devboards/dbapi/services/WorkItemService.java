package com.psu.devboards.dbapi.services;

import com.psu.devboards.dbapi.models.entities.WorkItem;
import com.psu.devboards.dbapi.repositories.WorkItemRepository;
import org.springframework.stereotype.Service;

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
}
