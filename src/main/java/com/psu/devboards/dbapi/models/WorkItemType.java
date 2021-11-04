package com.psu.devboards.dbapi.models;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
public enum WorkItemType {

    TASK("T"), STORY("S"), FEATURE("F");

    @Getter
    private String type;
}
