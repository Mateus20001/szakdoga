package com.szakdoga.backend.courses.repositories;

import com.szakdoga.backend.courses.models.CourseDetailEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CourseDetailRepository extends JpaRepository<CourseDetailEntity, Long> {
}
