package com.szakdoga.backend.courses.repositories;

import com.szakdoga.backend.courses.models.ExamEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ExamRepository extends JpaRepository<ExamEntity, Long> {
}
