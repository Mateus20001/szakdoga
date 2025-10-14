package com.szakdoga.backend.courses.repositories;

import com.szakdoga.backend.courses.models.ExamEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ExamRepository extends JpaRepository<ExamEntity, Long> {
    @Modifying
    @Query("DELETE FROM ExamEntity c WHERE c.courseDateEntity IS NULL")
    void deleteOrphanedExamApplicationEntities();

    @Query("""
        SELECT DISTINCT e
        FROM ExamEntity e
        JOIN e.courseDateEntity cd
        JOIN cd.applications app
        WHERE app.user.id = :userId
    """)
    List<ExamEntity> findAllByUserId(@Param("userId") String userId);
}
