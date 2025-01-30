package com.szakdoga.backend.courses.repositories;

import com.szakdoga.backend.auth.model.User;
import com.szakdoga.backend.courses.models.CourseApplicationEntity;
import com.szakdoga.backend.courses.models.CourseDateEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface CourseApplicationRepository extends JpaRepository<CourseApplicationEntity, Long> {
    Optional<CourseApplicationEntity> findByUserIdAndCourseDateEntity(String userId, CourseDateEntity courseDateEntity);
    @Modifying
    @Query("DELETE FROM CourseApplicationEntity c WHERE c.courseDateEntity IS NULL OR c.user IS NULL")
    void deleteOrphanedCourseTeacherEntities();

    List<CourseApplicationEntity> findByUserId(String userid);

    List<CourseApplicationEntity> findByUserIdAndCourseDateEntity_CourseDetailEntity_Id(String userId, Long courseDetailId);
}
