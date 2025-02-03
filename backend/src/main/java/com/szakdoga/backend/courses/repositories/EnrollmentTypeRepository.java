package com.szakdoga.backend.courses.repositories;

import com.szakdoga.backend.auth.model.MajorEntity;
import com.szakdoga.backend.courses.models.CourseDetailEntity;
import com.szakdoga.backend.courses.models.EnrollmentTypeEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface EnrollmentTypeRepository extends JpaRepository<EnrollmentTypeEntity, Long> {
    List<EnrollmentTypeEntity> findAllByCourseDetail(CourseDetailEntity courseDetail);

    List<EnrollmentTypeEntity> findAllByMajor(MajorEntity major);

    Optional<EnrollmentTypeEntity> findByCourseDetailAndMajor(CourseDetailEntity course, MajorEntity major);

    @Query("SELECT e FROM EnrollmentTypeEntity e WHERE e.courseDetail.id IN :courseIds AND e.major = :major")
    List<EnrollmentTypeEntity> findByCourseDetailIdsAndMajor(@Param("courseIds") List<Long> courseIds, @Param("major") MajorEntity major);

    @Query("SELECT e FROM EnrollmentTypeEntity e WHERE e.courseDetail.id IN :courseIds AND e.major.major.id = :major")
    List<EnrollmentTypeEntity> findByCourseDetailIdsAndMajorDetailsId(@Param("courseIds") List<Long> courseIds, @Param("major") int majorEntity);
}
