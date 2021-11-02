package com.psu.devboards.dbapi.models.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "work_item")
public class WorkItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column
    private String name;

    @Column
    private WorkItemType type;

    @Column
    private Integer priority;

    @Column
    private String description;

    public WorkItem(String name, WorkItemType type, Integer priority) {
        this.name = name;
        this.type = type;
        this.priority = priority;
        this.description = "";
    }
}
