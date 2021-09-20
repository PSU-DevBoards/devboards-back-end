package com.psu.devboards.dbapi.controllers;

import com.psu.devboards.dbapi.models.entities.WorkItem;
import com.psu.devboards.dbapi.repositories.WorkItemRepository;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("work-items")
public class WorkItemController {

    private final WorkItemRepository workItemRepository;

    public WorkItemController(WorkItemRepository workItemRepository) {
        this.workItemRepository = workItemRepository;
    }

    @GetMapping
    public List<WorkItem> listWorkItems() {
        return workItemRepository.findAll();
    }
}
