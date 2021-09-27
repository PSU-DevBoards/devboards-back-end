package com.psu.devboards.dbapi.controllers;

import com.psu.devboards.dbapi.models.entities.WorkItem;
import com.psu.devboards.dbapi.services.WorkItemService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class WorkItemControllerTest {

    @Mock
    WorkItemService workItemService;

    @InjectMocks
    WorkItemController workItemController;

    @Test
    void listWorkItems() {
        when(workItemService.getAllWorkItems()).thenReturn(Collections.singletonList(new WorkItem()));

        List<WorkItem> workItems = workItemController.listWorkItems(null);

        assertEquals(1, workItems.size());
    }
}