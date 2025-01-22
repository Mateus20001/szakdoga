package com.szakdoga.backend.auth.repositories;

import com.szakdoga.backend.auth.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.web.bind.annotation.CrossOrigin;

import java.util.Optional;

/**
 * Repository is an interface that provides access to data in a database
 */
@CrossOrigin
public interface UserRepository extends JpaRepository<User, String> {
    Optional<User> findByName(String name);

    boolean existsByName(String name);
}