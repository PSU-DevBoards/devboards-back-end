package com.psu.devboards.dbapi.models.entities;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.criteria.CriteriaBuilder;

@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "work_item")
public class WorkItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column
    private String name;

    @Column
    private String type;

    @Column
    private String description;

    @Column
    private Integer priority;

    /*public WorkItem(Integer id, String name, String type, String description, Integer priority){
        this.id = id;
        this.name = name;
        this.type = type;
        this.description = description;
        this.priority = priority;
    }*/

    public WorkItem(String name, String type, String description, Integer priority){
        this.name = name;
        this.type = type;
        this.description = description;
        this.priority = priority;
    }

    /*public void setName(String name) {
        this.name = name;
    }*/
}
