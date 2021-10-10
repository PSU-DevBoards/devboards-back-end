package com.psu.devboards.dbapi.repositories;

import com.psu.devboards.dbapi.models.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    User getByUsername(String username);

    Optional<User> findById(Integer id);
}
