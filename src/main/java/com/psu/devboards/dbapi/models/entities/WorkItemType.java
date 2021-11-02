package com.psu.devboards.dbapi.models.entities;

public enum WorkItemType {
    TASK("T"), STORY("S"), FEATURE("F");

    private String type;

    private WorkItemType(String type) {
        this.type = type;
    }

    public String getType() {
        return type;
    }
}
