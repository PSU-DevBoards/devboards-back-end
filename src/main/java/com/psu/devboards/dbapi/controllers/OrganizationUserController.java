package com.psu.devboards.dbapi.controllers;

import com.psu.devboards.dbapi.models.entities.Organization;
import com.psu.devboards.dbapi.models.entities.OrganizationUser;
import com.psu.devboards.dbapi.models.entities.Role;
import com.psu.devboards.dbapi.models.entities.User;
import com.psu.devboards.dbapi.models.requests.OrganizationUserRequest;
import com.psu.devboards.dbapi.services.OrganizationService;
import com.psu.devboards.dbapi.services.OrganizationUserService;
import com.psu.devboards.dbapi.services.RoleService;
import com.psu.devboards.dbapi.services.UserService;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.security.Principal;
import java.util.Set;

@RestController
@RequestMapping("organizations/{orgId}")
public class OrganizationUserController {

    private final UserService userService;
    private final OrganizationUserService organizationUserService;
    private final OrganizationService organizationService;
    private final RoleService roleService;

    public OrganizationUserController(UserService userService, OrganizationUserService organizationUserService,
                                      OrganizationService organizationService, RoleService roleService) {
        this.userService = userService;
        this.organizationUserService = organizationUserService;
        this.organizationService = organizationService;
        this.roleService = roleService;
    }

    @GetMapping("/users")
    public Set<OrganizationUser> getOrganizationUsers(@PathVariable Integer orgId, Principal principal) {
        User user = userService.getByUserName(principal.getName());
        Organization organization = organizationService.findOrganizationById(user, orgId);

        return organizationUserService.getOrganizationUsers(user, organization);
    }

    @PostMapping("/users")
    public void postOrganizationUser(@PathVariable Integer orgId,
                                     @Valid @RequestBody OrganizationUserRequest organizationUserRequest,
                                     Principal principal) {
        User user = userService.getByUserName(principal.getName());
        User newUser = userService.findById(organizationUserRequest.getUserId());
        Organization organization = organizationService.findOrganizationById(user, orgId);
        Role role = roleService.findRoleById(organizationUserRequest.getRoleId());

        organizationUserService.addOrganizationUser(user, organization, newUser, role);
    }

    @DeleteMapping("/users/{userId}")
    public void deleteOrganizationUser(@PathVariable Integer orgId, @PathVariable Integer userId, Principal principal) {
        User user = userService.getByUserName(principal.getName());
        User userToRemove = userService.findById(userId);
        Organization organization = organizationService.findOrganizationById(user, orgId);

        organizationUserService.removeOrganizationUser(user, organization, userToRemove);
    }

    @PatchMapping("/users/{userId}")
    public void patchOrganizationUser(@PathVariable Integer orgId,
                                      @Valid @RequestBody OrganizationUserRequest organizationUserRequest,
                                      @PathVariable Integer userId, Principal principal) {
        User user = userService.getByUserName(principal.getName());
        Organization organization = organizationService.findOrganizationById(user, orgId);
        User userToUpdate = userService.findById(userId);
        Role role = roleService.findRoleById(organizationUserRequest.getRoleId());

        organizationUserService.updateOrganizationUser(user, organization, userToUpdate, role);
    }
}
