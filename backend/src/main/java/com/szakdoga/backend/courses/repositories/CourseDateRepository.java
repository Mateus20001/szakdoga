package com.szakdoga.backend.courses.repositories;

import com.szakdoga.backend.courses.models.CourseDateEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CourseDateRepository  extends JpaRepository<CourseDateEntity, Long> {
    List<CourseDateEntity> findByCourseDetailEntityId(Long courseId);
    List<CourseDateEntity> findByCourseDetailEntityIdAndSemester(Long courseId, String semester);
}
