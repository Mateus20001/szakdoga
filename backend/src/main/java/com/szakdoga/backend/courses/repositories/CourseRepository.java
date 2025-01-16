package com.szakdoga.backend.courses.repositories;

import com.szakdoga.backend.courses.models.CourseEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CourseRepository extends JpaRepository<CourseEntity, Long> {
}
