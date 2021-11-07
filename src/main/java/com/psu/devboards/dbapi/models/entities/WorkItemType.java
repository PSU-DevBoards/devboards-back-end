package com.psu.devboards.dbapi.models.entities;

import com.fasterxml.jackson.annotation.JsonCreator;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.stream.Stream;

@AllArgsConstructor
public enum WorkItemType {

    TASK("T"), STORY("S"), FEATURE("F");

    @Getter
    private String type;

    @JsonCreator
    public static WorkItemType decode(final String name) {
        return Stream.of(WorkItemType.values()).filter(workItemType -> workItemType.name().equals(name)).findFirst()
                .orElse(null);
    }
}
