package com.szakdoga.backend.courses.repositories;

import com.szakdoga.backend.auth.model.User;
import com.szakdoga.backend.courses.models.CourseDetailEntity;
import com.szakdoga.backend.courses.models.CourseTeacherEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CourseTeacherRepository extends JpaRepository<CourseTeacherEntity, Long> {
    Optional<CourseTeacherEntity> findByCourseDetailAndTeacher(CourseDetailEntity course, User teacher);

    List<CourseTeacherEntity> findByCourseDetail(CourseDetailEntity course);
}
