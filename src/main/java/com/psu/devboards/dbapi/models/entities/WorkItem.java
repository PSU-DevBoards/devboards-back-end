package com.psu.devboards.dbapi.models.entities;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIdentityReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import com.psu.devboards.dbapi.utils.WorkItemStatusAttributeConverter;
import com.psu.devboards.dbapi.utils.WorkItemTypeAttributeConverter;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import java.util.Set;

@Data
@Entity
@Builder
@DynamicUpdate
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "work_item")
public class WorkItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column
    @NotNull
    private String name;

    @NotNull
    @Convert(converter = WorkItemTypeAttributeConverter.class)
    private WorkItemType type;

    @Column
    @NotNull
    @Convert(converter = WorkItemStatusAttributeConverter.class)
    private WorkItemStatus status = WorkItemStatus.BACKLOG;

    @Column
    @NotNull
    private Integer priority;

    @Column
    private String description;

    @ManyToOne
    @JsonProperty("organizationId")
    @JsonIdentityReference(alwaysAsId = true)
    @JoinColumn(name = "organization_id", nullable = false)
    @JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
    private Organization organization;

    @ManyToOne
    @JsonProperty("parentId")
    @JsonIdentityReference(alwaysAsId = true)
    @JoinColumn(name = "parent_id")
    @JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
    private WorkItem parent;

    @JsonIgnore
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    @OneToMany(mappedBy = "parent", cascade = CascadeType.ALL)
    private Set<WorkItem> children;
}
