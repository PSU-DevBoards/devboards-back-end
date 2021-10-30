package com.psu.devboards.dbapi.controllers;

import com.psu.devboards.dbapi.models.entities.Organization;
import com.psu.devboards.dbapi.models.entities.User;
import com.psu.devboards.dbapi.models.entities.WorkItem;
import com.psu.devboards.dbapi.models.requests.OrganizationRequest;
import com.psu.devboards.dbapi.models.requests.WorkItemRequest;
import com.psu.devboards.dbapi.services.UserService;
import com.psu.devboards.dbapi.services.WorkItemService;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("work-items")
public class WorkItemController {

    private final WorkItemService workItemService;
    private final UserService userService;
    public WorkItemController(WorkItemService workItemService,UserService userService) {
        this.userService = userService;
        this.workItemService = workItemService;
    }

    @GetMapping
    public List<WorkItem> listWorkItems(Principal principal) {
        return workItemService.getAllWorkItems();
    }

    @GetMapping("/{id}")
    public WorkItem getWorkItem(@PathVariable Integer id, Principal principal) {
        User user = userService.getByUserName(principal.getName());

        return workItemService.findWorkItemById(user, id);
    }

    @PostMapping
    public WorkItem postWorkItem(@Valid @RequestBody WorkItemRequest workItemRequest,
                                         Principal principal) {
        User user = userService.getByUserName(principal.getName());

        return workItemService.createWorkItem(workItemRequest.getName(), workItemRequest.getType(), workItemRequest.getDescription(), workItemRequest.getPriority());
    }

    @PatchMapping("/{id}")
    public WorkItem patchWorkItem(@PathVariable Integer id,
                                          @Valid @RequestBody WorkItemRequest workItemRequest,
                                          Principal principal) {
        User user = userService.getByUserName(principal.getName());

        return workItemService.updateWorkItemById(id,workItemRequest,user);
    }

    @DeleteMapping("/{id}")
    public void deleteWorkItem(@PathVariable Integer id, Principal principal) {
        User user = userService.getByUserName(principal.getName());

        workItemService.deleteOrganizationById(user, id);
    }
}
