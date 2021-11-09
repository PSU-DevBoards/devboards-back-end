package com.psu.devboards.dbapi.controllers;

import com.psu.devboards.dbapi.models.entities.OrganizationUser;
import com.psu.devboards.dbapi.models.entities.OrganizationUserKey;
import com.psu.devboards.dbapi.models.requests.OrganizationUserRequest;
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

    @GetMapping()
    @PreAuthorize("@organizationUserPermissionChecker.hasListPermission(#orgId)")
    public Set<OrganizationUser> getOrganizationUsers(@PathVariable Integer orgId) {
        return organizationUserService.getOrganizationUsers(orgId);
    }

    @PostMapping()
    @PreAuthorize("@organizationUserPermissionChecker.hasCreatePermission(#orgId)")
    public OrganizationUser postOrganizationUser(@PathVariable Integer orgId,
                                                 @Valid @RequestBody OrganizationUserRequest organizationUserRequest) {
        organizationUserRequest.setOrganizationId(orgId);

        // Create user and send welcome email
        OrganizationUser organizationUser = organizationUserService.create(organizationUserRequest);
        sendGridService.sendOrgInviteEmail(organizationUser.getUser().getEmail(),
                organizationUser.getOrganization().getName(), organizationUser.getOrganization().getId().toString());

        return organizationUser;
    }

    @DeleteMapping("/{userId}")
    @PreAuthorize("@organizationUserPermissionChecker.hasPermission(#orgId, #userId, 'delete')")
    public void deleteOrganizationUser(@PathVariable Integer orgId, @PathVariable Integer userId) {
        organizationUserService.deleteById(new OrganizationUserKey(orgId, userId));
    }

    @PatchMapping("/{userId}")
    @PreAuthorize("@organizationUserPermissionChecker.hasPermission(#orgId, #userId, 'edit')")
    public void patchOrganizationUser(@PathVariable Integer orgId, @PathVariable Integer userId,
                                      @RequestBody OrganizationUserRequest organizationUserRequest) {
        organizationUserService.updateById(new OrganizationUserKey(orgId, userId), organizationUserRequest);
    }
}
