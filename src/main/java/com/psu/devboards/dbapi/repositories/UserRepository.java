package com.psu.devboards.dbapi.repositories;

import com.psu.devboards.dbapi.models.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Integer> {
    User getByUsername(String username);
}
