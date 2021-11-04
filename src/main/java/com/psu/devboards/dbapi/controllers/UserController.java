package com.psu.devboards.dbapi.controllers;

import com.psu.devboards.dbapi.models.entities.Organization;
import com.psu.devboards.dbapi.models.entities.User;
import com.psu.devboards.dbapi.services.UserService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;
import java.util.Set;

@RestController
@RequestMapping("users")
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/me")
    public User getCurrentUser(Principal principal) {
        return userService.getByUserName(principal.getName());
    }

    @GetMapping("/me/organizations")
    public Set<Organization> getCurrentUserOrganizations(Principal principal) {
        User user = userService.getByUserName(principal.getName());
        return user.getOwnedOrganizations();
    }
}