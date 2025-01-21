package com.szakdoga.backend.courses.repositories;

import com.szakdoga.backend.auth.model.MajorEntity;
import com.szakdoga.backend.courses.models.CourseDetailEntity;
import com.szakdoga.backend.courses.models.EnrollmentTypeEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface EnrollmentTypeRepository extends JpaRepository<EnrollmentTypeEntity, Long> {
    List<EnrollmentTypeEntity> findAllByCourseDetail(CourseDetailEntity courseDetail);

    List<EnrollmentTypeEntity> findAllByMajor(MajorEntity major);
}
