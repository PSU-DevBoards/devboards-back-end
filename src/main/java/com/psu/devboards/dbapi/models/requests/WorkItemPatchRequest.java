package com.psu.devboards.dbapi.models.requests;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.psu.devboards.dbapi.models.entities.WorkItemStatus;
import com.psu.devboards.dbapi.models.entities.WorkItemType;
import com.psu.devboards.dbapi.models.requests.validators.NullOrNotBlank;
import com.psu.devboards.dbapi.models.requests.validators.ValueOfEnum;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.Min;

@Data
@Builder
@AllArgsConstructor
public class WorkItemPatchRequest implements WorkItemRequest {

    @ValueOfEnum(enumClass = WorkItemType.class)
    private WorkItemType type;

    @ValueOfEnum(enumClass = WorkItemStatus.class)
    private WorkItemStatus status;

    @NullOrNotBlank
    private String name;

    private Integer priority;

    private String description;

    @Min(0)
    private Integer estimate;

    private Integer parentId;

    @JsonIgnore
    private Integer organizationId;
}
