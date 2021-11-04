package com.psu.devboards.dbapi.controllers;

import com.psu.devboards.dbapi.models.entities.OrganizationUser;
import com.psu.devboards.dbapi.models.entities.OrganizationUserKey;
import com.psu.devboards.dbapi.models.requests.OrganizationUserRequest;
import com.psu.devboards.dbapi.services.OrganizationUserService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.Set;

@RestController
@RequestMapping("organizations/{orgId}/users")
public class OrganizationUserController {

    private final OrganizationUserService organizationUserService;

    public OrganizationUserController(OrganizationUserService organizationUserService) {
        this.organizationUserService = organizationUserService;
    }

    @GetMapping()
    @PreAuthorize("@organizationUserPermissionChecker.hasListPermission(#orgId)")
    public Set<OrganizationUser> getOrganizationUsers(@PathVariable Integer orgId) {
        return organizationUserService.getOrganizationUsers(orgId);
    }

    @PostMapping()
    @PreAuthorize("@organizationUserPermissionChecker.hasCreatePermission(#orgId)")
    public void postOrganizationUser(@PathVariable Integer orgId,
                                     @Valid @RequestBody OrganizationUserRequest organizationUserRequest) {
        organizationUserRequest.setOrganizationId(orgId);
        organizationUserService.create(organizationUserRequest);
    }

    @DeleteMapping("/{userId}")
    @PreAuthorize("@organizationUserPermissionChecker.hasPermission(#orgId, #userId, 'delete')")
    public void deleteOrganizationUser(@PathVariable Integer orgId, @PathVariable Integer userId) {
        organizationUserService.deleteById(new OrganizationUserKey(orgId, userId));
    }

    @PatchMapping("/{userId}")
    @PreAuthorize("@organizationUserPermissionChecker.hasPermission(#orgId, #userId, 'edit')")
    public void patchOrganizationUser(@PathVariable Integer orgId, @PathVariable Integer userId,
                                      @Valid @RequestBody OrganizationUserRequest organizationUserRequest) {
        organizationUserService.updateById(new OrganizationUserKey(orgId, userId), organizationUserRequest);
    }
}
