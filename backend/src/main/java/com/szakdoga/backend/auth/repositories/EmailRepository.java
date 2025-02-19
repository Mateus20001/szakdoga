package com.szakdoga.backend.auth.repositories;

import com.szakdoga.backend.auth.model.EmailEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface EmailRepository extends JpaRepository<EmailEntity, Long> {
    Optional<EmailEntity> findByEmail(String email);

    List<EmailEntity> findAllByUserId(String user_id);
}
