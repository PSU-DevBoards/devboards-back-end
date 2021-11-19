package com.psu.devboards.dbapi.models.entities;

import com.fasterxml.jackson.annotation.JsonCreator;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.stream.Stream;

@AllArgsConstructor
public enum WorkItemStatus {

    BACKLOG("BL"), READY("RD"), IN_PROGRESS("IP"), VERIFY("VE"), DONE("DN");

    @Getter
    private String type;

    @JsonCreator
    public static WorkItemStatus decode(final String name) {
        return Stream.of(WorkItemStatus.values()).filter(workItemStatus -> workItemStatus.name().equals(name)).findFirst()
                .orElse(null);
    }
}
