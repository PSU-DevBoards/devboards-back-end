package com.psu.devboards.dbapi.controllers;

import com.psu.devboards.dbapi.models.entities.User;
import com.psu.devboards.dbapi.services.UserService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;

@RestController
@RequestMapping("user")
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/me")
    public User getMe(Principal principal){
        return userService.findByUsername(principal.getName());
    }
}