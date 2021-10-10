package com.psu.devboards.dbapi.models.entities;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIdentityReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.MapsId;
import javax.persistence.OneToOne;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class OrganizationUser {

    @EmbeddedId
    @JsonIgnore
    private OrganizationUserKey id = new OrganizationUserKey();

    @ManyToOne
    @MapsId("organizationId")
    @JsonProperty("organization_id")
    @JoinColumn(name = "organization_id")
    @JsonIdentityReference(alwaysAsId = true)
    @JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
    private Organization organization;

    @ManyToOne
    @MapsId("userId")
    @JsonProperty("user_id")
    @JoinColumn(name = "user_id")
    @JsonIdentityReference(alwaysAsId = true)
    @JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
    private User user;

    @OneToOne
    @JsonProperty("role_id")
    @JoinColumn(name = "role_id")
    @JsonIdentityReference(alwaysAsId = true)
    @JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
    private Role role;

    public OrganizationUser(Organization organization, User user) {
        this.organization = organization;
        this.user = user;
    }

    public OrganizationUser(Organization organization, User user, Role role) {
        this.organization = organization;
        this.user = user;
        this.role = role;
    }
}
