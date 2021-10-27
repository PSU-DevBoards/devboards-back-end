package com.psu.devboards.dbapi.controllers;

import com.psu.devboards.dbapi.models.entities.User;
import com.psu.devboards.dbapi.models.entities.WorkItem;
import com.psu.devboards.dbapi.models.requests.WorkItemRequest;
import com.psu.devboards.dbapi.services.UserService;
import com.psu.devboards.dbapi.services.WorkItemService;
import org.springframework.web.bind.annotation.*;
import org.springframework.validation.annotation.Validated;

import javax.validation.Valid;
import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("work-items")
@Validated
public class WorkItemController {

    private final UserService userService;
    private final WorkItemService workItemService;

    public WorkItemController(UserService userService, WorkItemService workItemService) {
        this.userService = userService;
        this.workItemService = workItemService;
    }

    @GetMapping
    public List<WorkItem> listWorkItems(Principal principal) {
        return workItemService.getAllWorkItems();
    }

    @PostMapping
    public void postFeature(@Valid @RequestBody WorkItemRequest workItemRequest, Principal principal){
        User user = userService.getByUserName(principal.getName());
        workItemService.createWorkItem(user, workItemRequest.getName(), workItemRequest.getType(), workItemRequest.getPriority());
    }
}