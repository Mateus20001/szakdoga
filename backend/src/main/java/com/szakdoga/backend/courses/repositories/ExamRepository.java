package com.szakdoga.backend.courses.repositories;

import com.szakdoga.backend.courses.models.ExamEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

public interface ExamRepository extends JpaRepository<ExamEntity, Long> {
    @Modifying
    @Query("DELETE FROM ExamEntity c WHERE c.courseDateEntity IS NULL")
    void deleteOrphanedExamApplicationEntities();
}
