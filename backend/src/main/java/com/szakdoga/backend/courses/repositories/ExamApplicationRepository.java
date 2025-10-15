package com.szakdoga.backend.courses.repositories;

import com.szakdoga.backend.auth.model.User;
import com.szakdoga.backend.courses.models.CourseDateEntity;
import com.szakdoga.backend.courses.models.ExamApplicationEntity;
import com.szakdoga.backend.courses.models.ExamEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ExamApplicationRepository extends JpaRepository<ExamApplicationEntity, Long> {
    ExamApplicationEntity findByUserAndExamEntity(User user, ExamEntity examEntity);
    @Modifying
    @Query("DELETE FROM ExamApplicationEntity c WHERE c.examEntity IS NULL OR c.user IS NULL")
    void deleteOrphanedExamApplicationEntities();
    boolean existsByUserAndExamEntity_CourseDateEntity(User user, CourseDateEntity courseDateEntity);
    List<ExamApplicationEntity> findAllByUserId(String userId);

}
