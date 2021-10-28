package com.psu.devboards.dbapi.models.entities;

import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@EqualsAndHashCode(callSuper = true)
@Data
@Entity
@Table(name = "feature")
public class Feature extends WorkItem {
    @Column
    private String description;

}
