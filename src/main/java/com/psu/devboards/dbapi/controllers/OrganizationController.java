package com.psu.devboards.dbapi.controllers;

import com.psu.devboards.dbapi.models.entities.Organization;
import com.psu.devboards.dbapi.models.requests.OrganizationFullRequest;
import com.psu.devboards.dbapi.models.requests.OrganizationPatchRequest;
import com.psu.devboards.dbapi.services.OrganizationService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

/**
 * REST Controller for all {@link Organization} related resources.
 */
@Validated
@RestController
@RequestMapping("organizations")
public class OrganizationController {

    private final OrganizationService organizationService;

    public OrganizationController(OrganizationService organizationService) {
        this.organizationService = organizationService;
    }

    /**
     * Gets a single organization by its id.
     *
     * @param id The id to use to retrieve the organization.
     * @return The found organization.
     */
    @GetMapping("/{id}")
    @PreAuthorize("hasPermission(#id, 'Organization', 'view')")
    public Organization getOrganization(@PathVariable Integer id) {
        return organizationService.getById(id);
    }

    /**
     * Creates a new organization.
     *
     * @param organizationRequest The request body containing values for the organization.
     * @return The created organization.
     */
    @PostMapping
    public Organization postOrganization(@Valid @RequestBody OrganizationFullRequest organizationRequest) {
        return organizationService.create(organizationRequest);
    }

    /**
     * Performs a partial update on an organization by its id.
     *
     * @param id                  The id of the organization to update.
     * @param organizationRequest Request body containing the update values.
     */
    @PatchMapping("/{id}")
    @PreAuthorize("hasPermission(#id, 'Organization', 'edit')")
    public void patchOrganization(@PathVariable Integer id,
                                  @Valid @RequestBody OrganizationPatchRequest organizationRequest) {
        organizationService.updateById(id, organizationRequest);
    }

    /**
     * Deletes an organization by its id.
     *
     * @param id The id of the organization to delete.
     */
    @DeleteMapping("/{id}")
    @PreAuthorize("hasPermission(#id, 'Organization', 'delete')")
    public void deleteOrganization(@PathVariable Integer id) {
        organizationService.deleteById(id);
    }
}
