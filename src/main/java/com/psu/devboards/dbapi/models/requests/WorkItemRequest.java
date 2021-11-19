package com.psu.devboards.dbapi.models.requests;

import com.psu.devboards.dbapi.models.entities.WorkItemStatus;
import com.psu.devboards.dbapi.models.entities.WorkItemType;

public interface WorkItemRequest {

    WorkItemType getType();

    WorkItemStatus getStatus();

    String getName();

    Integer getPriority();

    String getDescription();

    Integer getParentId();

    Integer getOrganizationId();
}
