package com.psu.devboards.dbapi.controllers;

import com.psu.devboards.dbapi.models.entities.OrganizationUser;
import com.psu.devboards.dbapi.models.entities.OrganizationUserKey;
import com.psu.devboards.dbapi.models.requests.OrganizationUserFullRequest;
import com.psu.devboards.dbapi.models.requests.OrganizationUserPatchRequest;
import com.psu.devboards.dbapi.services.OrganizationUserService;
import com.psu.devboards.dbapi.services.SendGridService;
import lombok.extern.slf4j.Slf4j;
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
import java.util.Set;

/**
 * REST Controller for all {@link OrganizationUser} related resources.
 */
@Slf4j
@Validated
@RestController
@RequestMapping("organizations/{orgId}/users")
public class OrganizationUserController {

    private final OrganizationUserService organizationUserService;
    private final SendGridService sendGridService;

    public OrganizationUserController(OrganizationUserService organizationUserService,
                                      SendGridService sendGridService) {
        this.organizationUserService = organizationUserService;
        this.sendGridService = sendGridService;
    }

    /**
     * Gets a list of organization users.
     *
     * @param orgId The id of the organization to retrieve users for.
     * @return The retrieved organization users.
     */
    @GetMapping()
    @PreAuthorize("@organizationUserPermissionChecker.hasListPermission(#orgId)")
    public Set<OrganizationUser> getOrganizationUsers(@PathVariable Integer orgId) {
        return organizationUserService.getOrganizationUsers(orgId);
    }

    /**
     * Creates a new organization user.
     *
     * @param orgId                   The id of the organization to create the new user in.
     * @param organizationUserRequest The request body containing the new user values.
     * @return The created organization user.
     */
    @PostMapping()
    @PreAuthorize("@organizationUserPermissionChecker.hasCreatePermission(#orgId)")
    public OrganizationUser postOrganizationUser(@PathVariable Integer orgId,
                                                 @Valid @RequestBody OrganizationUserFullRequest organizationUserRequest) {
        organizationUserRequest.setOrganizationId(orgId);

        // Create user and send welcome email
        OrganizationUser organizationUser = organizationUserService.create(organizationUserRequest);
        sendGridService.sendOrgInviteEmail(organizationUser.getUser().getEmail(),
                organizationUser.getOrganization().getName(), organizationUser.getOrganization().getId().toString());

        return organizationUser;
    }

    /**
     * Deletes an organization user by its id.
     *
     * @param orgId  The id of the organization the user belongs to.
     * @param userId The id of the user within the organization. Note this is the primary key ID of the user alone.
     */
    @DeleteMapping("/{userId}")
    @PreAuthorize("@organizationUserPermissionChecker.hasPermission(#orgId, #userId, 'delete')")
    public void deleteOrganizationUser(@PathVariable Integer orgId, @PathVariable Integer userId) {
        organizationUserService.deleteById(new OrganizationUserKey(orgId, userId));
    }

    /**
     * Performs a partial update of an organization user.
     *
     * @param orgId                   The id of the organization the user belongs to.
     * @param userId                  The id of the user within the organization. Note this is the primary key ID of
     *                                the user alone.
     * @param organizationUserRequest The request body containing update values.
     */
    @PatchMapping("/{userId}")
    @PreAuthorize("@organizationUserPermissionChecker.hasPermission(#orgId, #userId, 'edit')")
    public void patchOrganizationUser(@PathVariable Integer orgId, @PathVariable Integer userId,
                                      @RequestBody OrganizationUserPatchRequest organizationUserRequest) {
        organizationUserService.updateById(new OrganizationUserKey(orgId, userId), organizationUserRequest);
    }
}
