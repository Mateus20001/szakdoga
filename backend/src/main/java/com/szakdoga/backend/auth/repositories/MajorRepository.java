package com.szakdoga.backend.auth.repositories;

import com.szakdoga.backend.auth.model.MajorEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MajorRepository extends JpaRepository<MajorEntity, Long> {
}
