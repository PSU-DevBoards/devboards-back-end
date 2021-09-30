package com.psu.devboards.dbapi.controllers;

import com.psu.devboards.dbapi.models.entities.WorkItem;
import com.psu.devboards.dbapi.services.WorkItemService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("work-items")
public class WorkItemController {

    private final WorkItemService workItemService;

    public WorkItemController(WorkItemService workItemService) {
        this.workItemService = workItemService;
    }

    @GetMapping
    public List<WorkItem> listWorkItems(Principal principal) {
        return workItemService.getAllWorkItems();
    }
}
