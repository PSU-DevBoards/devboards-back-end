package com.psu.devboards.dbapi.services;

import com.psu.devboards.dbapi.models.entities.User;
import com.psu.devboards.dbapi.repositories.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    UserRepository userRepository;

    @InjectMocks
    UserService userService;

    @Test
    void saveUserCallsRepositorySave() {
        User user = new User("test");

        userService.saveUser(user);

        verify(userRepository, times(1)).save(user);
    }
}