package com.kfc.app.repository;

import com.kfc.app.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Integer> {
    Optional<User> findUserByUsernameAndPassword(String username, String password);
}