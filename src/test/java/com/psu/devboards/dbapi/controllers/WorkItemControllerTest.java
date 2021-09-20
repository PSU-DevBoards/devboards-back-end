package com.psu.devboards.dbapi.controllers;

import com.psu.devboards.dbapi.models.entities.WorkItem;
import com.psu.devboards.dbapi.repositories.WorkItemRepository;
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
    WorkItemRepository workItemRepository;

    @InjectMocks
    WorkItemController workItemController;

    @Test
    void listWorkItems() {
        when(workItemRepository.findAll()).thenReturn(Collections.singletonList(new WorkItem()));

        List<WorkItem> workItems = workItemController.listWorkItems();

        assertEquals(1,workItems.size());
    }
}