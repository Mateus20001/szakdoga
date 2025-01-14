package com.szakdoga.backend.auth.repositories;

import com.szakdoga.backend.auth.model.FacultyEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FacultyRepository extends JpaRepository<FacultyEntity, Long> {
}
