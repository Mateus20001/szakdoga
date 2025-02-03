package com.szakdoga.backend.courses.repositories;

import com.szakdoga.backend.courses.models.Grade;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GradeRepository extends JpaRepository<Grade, Long> {
}
