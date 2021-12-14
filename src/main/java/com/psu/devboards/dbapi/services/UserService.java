package com.psu.devboards.dbapi.services;

import com.psu.devboards.dbapi.models.entities.User;
import com.psu.devboards.dbapi.repositories.UserRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

/**
 * Singleton service for interacting with {@link User}s.
 */
@Service
public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    /**
     * Gets a user by their username. Note that in this system the username correlates to the Auth0 user id.
     *
     * @param username The username to locate the user by.
     * @return The located user.
     */
    public User getByUserName(String username) {
        return userRepository.getByUsername(username);
    }

    /**
     * Finds a user by their email.
     *
     * @param email The email to locate the user with.
     * @return An optional possibly containing the located user.
     */
    public Optional<User> findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    /**
     * Saves a user entity.
     *
     * @param user The user to save.
     * @return The saved user.
     */
    public User saveUser(User user) {
        return userRepository.save(user);
    }
}
