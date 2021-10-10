package com.psu.devboards.dbapi.controllers;

import com.psu.devboards.dbapi.models.entities.Organization;
import com.psu.devboards.dbapi.models.entities.User;
import com.psu.devboards.dbapi.models.requests.OrganizationRequest;
import com.psu.devboards.dbapi.services.OrganizationService;
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

@RestController
@RequestMapping("organizations")
public class OrganizationController {

    private final UserService userService;
    private final OrganizationService organizationService;

    public OrganizationController(UserService userService, OrganizationService organizationService) {
        this.userService = userService;
        this.organizationService = organizationService;
    }

    @GetMapping("/{id}")
    public Organization getOrganization(@PathVariable Integer id, Principal principal) {
        User user = userService.getByUserName(principal.getName());

        return organizationService.findOrganizationById(user, id);
    }

    @PostMapping
    public Organization postOrganization(@Valid @RequestBody OrganizationRequest organizationRequest,
                                         Principal principal) {
        User user = userService.getByUserName(principal.getName());

        return organizationService.createOrganization(user, organizationRequest.getName());
    }

    @PatchMapping("/{id}")
    public Organization patchOrganization(@PathVariable Integer id,
                                          @Valid @RequestBody OrganizationRequest organizationRequest,
                                          Principal principal) {
        User user = userService.getByUserName(principal.getName());

        return organizationService.updateOrganizationById(user, id, organizationRequest);
    }

    @DeleteMapping("/{id}")
    public void deleteOrganization(@PathVariable Integer id, Principal principal) {
        User user = userService.getByUserName(principal.getName());

        organizationService.deleteOrganizationById(user, id);
    }
}
