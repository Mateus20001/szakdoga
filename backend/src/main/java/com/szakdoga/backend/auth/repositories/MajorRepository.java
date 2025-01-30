package com.szakdoga.backend.auth.repositories;

import com.szakdoga.backend.auth.model.MajorEntity;
import com.szakdoga.backend.auth.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface MajorRepository extends JpaRepository<MajorEntity, Long> {
    Optional<MajorEntity> findByUser(User user);

    Optional<MajorEntity> findByUserId(String userId);
}
