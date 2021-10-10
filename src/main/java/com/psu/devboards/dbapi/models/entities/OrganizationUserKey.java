package com.psu.devboards.dbapi.models.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;

@Data
@Embeddable
@NoArgsConstructor
@AllArgsConstructor
public class OrganizationUserKey implements Serializable {

    @Column(name = "organization_id")
    private Integer organizationId;

    @Column(name = "user_id")
    private Integer userId;
}
