package com.psu.devboards.dbapi.models.requests;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.psu.devboards.dbapi.models.entities.WorkItemType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
@Builder
@AllArgsConstructor
public class WorkItemRequest {

    @NotNull(message = "Work item type must be any of {TASK, STORY, FEATURE}.")
    private WorkItemType type;

    @NotBlank(message = "Work item name cannot be blank.")
    @NotNull(message = "Work item name must be defined.")
    private String name;

    @NotNull(message = "Work item priority must be defined.")
    private Integer priority;

    private String description;

    @JsonIgnore
    private Integer organizationId;
}