package com.szakdoga.backend.auth.repositories;

import com.szakdoga.backend.auth.dtos.UserListingDTO;
import com.szakdoga.backend.auth.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.web.bind.annotation.CrossOrigin;

import java.util.List;
import java.util.Optional;

/**
 * Repository is an interface that provides access to data in a database
 */
@CrossOrigin
public interface UserRepository extends JpaRepository<User, String> {
    Optional<User> findByName(String name);

    boolean existsByName(String name);

    @Query("SELECT u FROM User u JOIN FETCH u.roles r WHERE r.roleName = 'TEACHER'")
    List<User> findAllTeachers();

    @Query("SELECT new com.szakdoga.backend.auth.dtos.UserListingDTO(u.id, u.firstName, u.lastName, u.name) " +
            "FROM User u JOIN u.roles r WHERE r.roleName = 'TEACHER'")
    List<UserListingDTO> findAllTeacherDTOs();
}