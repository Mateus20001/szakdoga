package com.szakdoga.backend.courses.repositories;

import com.szakdoga.backend.courses.models.CourseTeacherEntity;
import com.szakdoga.backend.courses.models.CourseTimetablePlannerEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface CourseTimetablePlannerRepository extends JpaRepository<CourseTimetablePlannerEntity, Long> {

    List<CourseTimetablePlannerEntity> findAllByUserId(String userId);
    @Modifying
    @Query("DELETE FROM CourseTimetablePlannerEntity c WHERE c.courseDateEntity IS NULL OR c.user IS NULL")
    void deleteOrphanedCourseTimetableEntities();

    Optional<CourseTimetablePlannerEntity> findByCourseDateEntityIdAndUserId(long id, String userId);
}
