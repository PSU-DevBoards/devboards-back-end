package com.psu.devboards.dbapi.controllers;

import com.psu.devboards.dbapi.models.entities.Organization;
import com.psu.devboards.dbapi.models.entities.User;
import com.psu.devboards.dbapi.models.requests.OrganizationRequest;
import com.psu.devboards.dbapi.services.OrganizationService;
import com.psu.devboards.dbapi.services.UserService;
import com.psu.devboards.dbapi.utils.ApiErrorAttributes;
import com.psu.devboards.dbapi.exceptions.NameUniqueViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.security.Principal;
import java.util.Arrays;

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

    @ExceptionHandler(NameUniqueViolationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    public ResponseEntity<Object> handleNameUniqueViolation(NameUniqueViolationException ex)
    {
        ApiErrorAttributes err = new ApiErrorAttributes();
        return err.generateErrorResponse(Arrays.asList(ex.getMessage()), HttpStatus.BAD_REQUEST);
    }
}
