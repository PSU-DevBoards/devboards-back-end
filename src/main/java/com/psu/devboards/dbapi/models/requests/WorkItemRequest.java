package com.psu.devboards.dbapi.models.requests;

import lombok.AllArgsConstructor;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

@Data
@AllArgsConstructor
public class WorkItemRequest {
    @NotBlank(message="Work item must be of type: feature, story, or task.")
    @NotNull(message="Work item type must be defined.")
    @Pattern(regexp = "feature|story|task", flags = Pattern.Flag.CASE_INSENSITIVE, message="Work item must be of type: feature, story, or task.")
    private String type;

    @NotBlank(message="Work item name cannot be blank.")
    @NotNull(message="Work item name must be defined.")
    private String name;

    @NotNull(message="Work item priority must be defined.")
    private Integer priority;
}
