package com.psu.devboards.dbapi.models.requests;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.psu.devboards.dbapi.models.WorkItemType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

@Data
@Builder
@AllArgsConstructor
public class WorkItemRequest {

    @NotBlank(message = "Work item must be of type: feature, story, or task.")
    @NotNull(message = "Work item type must be defined.")
    @Pattern(regexp = "feature|story|task", flags = Pattern.Flag.CASE_INSENSITIVE, message = "Work item must be of " +
            "type: feature, story, or task.")
    private WorkItemType type;

    @NotBlank(message = "Work item name cannot be blank.")
    @NotNull(message = "Work item name must be defined.")
    private String name;

    @NotBlank(message="Work item name cannot be blank.")
    @NotNull(message = "Work item priority must be defined.")
    private Integer priority;

    private String description;

    @JsonIgnore
    private Integer organizationId;
}