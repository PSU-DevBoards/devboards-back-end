package com.psu.devboards.dbapi.models.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

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
    @Pattern(regexp = "feature|story|task")
    private String type;

    @Column
    private Integer priority;

    @Column
    private String description;

    public WorkItem(String name, String type, Integer priority) {
        this.name = name;
        this.type = type;
        this.priority = priority;
        this.description = "";
    }
}
