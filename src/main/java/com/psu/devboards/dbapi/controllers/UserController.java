package com.psu.devboards.dbapi.controllers;

import com.psu.devboards.dbapi.models.entities.Organization;
import com.psu.devboards.dbapi.models.entities.User;
import com.psu.devboards.dbapi.services.UserService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;
import java.util.Set;

/**
 * REST Controller for all {@link User} related resources.
 */
@RestController
@RequestMapping("users")
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    /**
     * Gets the current authenticated user.
     *
     * @param principal The current authenticated principal.
     * @return The current user.
     */
    @GetMapping("/me")
    public User getCurrentUser(Principal principal) {
        return userService.getByUserName(principal.getName());
    }

    /**
     * Gets the current authenticated users organizations.
     *
     * @param principal The current authenticated principal.
     * @return A list of the organizations the current user owns.
     */
    @GetMapping("/me/organizations")
    public Set<Organization> getCurrentUserOrganizations(Principal principal) {
        User user = userService.getByUserName(principal.getName());
        return user.getOwnedOrganizations();
    }
}