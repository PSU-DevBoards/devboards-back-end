package com.psu.devboards.dbapi.services;

import com.psu.devboards.dbapi.models.entities.User;
import com.psu.devboards.dbapi.repositories.UserRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User getByUserName(String username) {
        return userRepository.getByUsername(username);
    }

    public User findById(Integer id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User does not exist."));
    }

    public User saveUser(User user) {
        return userRepository.save(user);
    }
}
