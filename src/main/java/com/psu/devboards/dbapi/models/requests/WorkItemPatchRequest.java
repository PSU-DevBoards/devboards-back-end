package com.psu.devboards.dbapi.models.requests;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.psu.devboards.dbapi.models.entities.WorkItemStatus;
import com.psu.devboards.dbapi.models.entities.WorkItemType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
@Builder
@AllArgsConstructor
public class WorkItemPatchRequest implements WorkItemRequest {

    private WorkItemType type;

    private WorkItemStatus status;

    private String name;

    private Integer priority;

    private String description;

    private Integer parentId;

    @JsonIgnore
    private Integer organizationId;
}
