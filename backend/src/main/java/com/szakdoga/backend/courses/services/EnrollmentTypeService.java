package com.szakdoga.backend.courses.services;

import com.szakdoga.backend.auth.model.MajorEntity;
import com.szakdoga.backend.courses.models.CourseDetailEntity;
import com.szakdoga.backend.courses.models.EnrollmentTypeEntity;
import com.szakdoga.backend.courses.models.EnrollmentType;
import com.szakdoga.backend.courses.repositories.EnrollmentTypeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EnrollmentTypeService {

    @Autowired
    private EnrollmentTypeRepository enrollmentTypeRepository;

    // Create an EnrollmentType
    public EnrollmentTypeEntity createEnrollmentType(MajorEntity major, CourseDetailEntity courseDetail, EnrollmentType enrollmentType) {
        EnrollmentTypeEntity enrollmentTypeEntity = new EnrollmentTypeEntity();
        enrollmentTypeEntity.setMajor(major);
        enrollmentTypeEntity.setCourseDetail(courseDetail);
        enrollmentTypeEntity.setEnrollmentType(enrollmentType);

        return enrollmentTypeRepository.save(enrollmentTypeEntity);
    }

    // Get all EnrollmentTypes for a specific course
    public List<EnrollmentTypeEntity> getEnrollmentTypesByCourse(CourseDetailEntity courseDetail) {
        return enrollmentTypeRepository.findAllByCourseDetail(courseDetail);
    }

    // Get all EnrollmentTypes for a specific major
    public List<EnrollmentTypeEntity> getEnrollmentTypesByMajor(MajorEntity major) {
        return enrollmentTypeRepository.findAllByMajor(major);
    }
}
