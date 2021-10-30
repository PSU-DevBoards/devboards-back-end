package com.psu.devboards.dbapi.models.requests;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class WorkItemRequest {
    private String name;
    private String type;
    private String description;
    private Integer priority;
}
