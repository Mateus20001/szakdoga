package com.szakdoga.backend.courses.repositories;

import com.szakdoga.backend.courses.models.CourseDetailEntity;
import com.szakdoga.backend.courses.models.EnrollmentTypeEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CourseEnrollmentRepository extends JpaRepository<EnrollmentTypeEntity, Long> {
}
