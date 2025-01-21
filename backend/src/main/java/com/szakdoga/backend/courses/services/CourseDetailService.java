package com.szakdoga.backend.courses.services;

import com.szakdoga.backend.auth.model.MajorEntity;
import com.szakdoga.backend.auth.repositories.MajorRepository;
import com.szakdoga.backend.courses.dtos.CourseDetailListingDTO;
import com.szakdoga.backend.courses.dtos.EnrollmentTypeDTO;
import com.szakdoga.backend.courses.models.CourseDetailEntity;
import com.szakdoga.backend.courses.models.EnrollmentType;
import com.szakdoga.backend.courses.models.EnrollmentTypeEntity;
import com.szakdoga.backend.courses.models.RequirementType;
import com.szakdoga.backend.courses.repositories.CourseDetailRepository;
import com.szakdoga.backend.courses.repositories.EnrollmentTypeRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class CourseDetailService {

    @Autowired
    private CourseDetailRepository courseDetailRepository;

    @Autowired
    private MajorRepository majorRepository;

    @Autowired
    private EnrollmentTypeRepository enrollmentTypeRepository;

    // Add new CourseDetailEntity
    public CourseDetailEntity addCourse(String name, String description, int credits, String requirementType,
                                        List<Long> requiredCourseIds, List<EnrollmentTypeEntity> enrollmentTypes) {
        // Fetch required courses by IDs
        List<CourseDetailEntity> requiredCourses = courseDetailRepository.findAllById(requiredCourseIds);

        // Create new course
        CourseDetailEntity courseDetail = new CourseDetailEntity();
        courseDetail.setName(name);
        courseDetail.setDescription(description);
        courseDetail.setCredits(credits);
        courseDetail.setRequirementType(RequirementType.valueOf(requirementType));
        courseDetail.setRequiredCourses(requiredCourses);

        // Save the course
        CourseDetailEntity savedCourse = courseDetailRepository.save(courseDetail);

        // Map and save enrollment types
        enrollmentTypes.forEach(enrollmentType -> {
            enrollmentType.setCourseDetail(savedCourse);
            enrollmentTypeRepository.save(enrollmentType);
        });

        return savedCourse;
    }

    // Get all CourseDetailEntities
    public List<CourseDetailListingDTO> getAllCourseDetails() {
        List<CourseDetailEntity> courseDetails = courseDetailRepository.findAll();
        return courseDetails.stream()
                .map(course -> new CourseDetailListingDTO(course.getId(), course.getName(), course.getDescription(), course.getCredits()))
                .collect(Collectors.toList());
    }

    // Get a CourseDetailEntity by ID
    public CourseDetailEntity getCourseDetailById(Long id) {
        return courseDetailRepository.findById(id).orElse(null);
    }
    public List<CourseDetailListingDTO> getAllCourses() {
        return courseDetailRepository.findAll().stream()
                .map(course -> new CourseDetailListingDTO(
                        course.getId(),
                        course.getName(),
                        course.getDescription(),
                        course.getCredits()
                ))
                .collect(Collectors.toList()); // Fetch all courses from the database
    }


    public void updateCourse(Long courseId, String name, String description, int credits,
                             String requirementType, List<Long> requiredCourseIds,
                             List<EnrollmentTypeDTO> enrollmentTypes) {
        CourseDetailEntity course = courseDetailRepository.findById(courseId)
                .orElseThrow(() -> new EntityNotFoundException("Course not found with id: " + courseId));

        course.setName(name);
        course.setDescription(description);
        course.setCredits(credits);
        course.setRequirementType(RequirementType.valueOf(requirementType));

        // Update required courses
        List<CourseDetailEntity> requiredCourses = courseDetailRepository.findAllById(requiredCourseIds);
        course.setRequiredCourses(requiredCourses);

        // Update enrollment types
        List<EnrollmentTypeEntity> enrollmentEntities = new ArrayList<>();
        for (EnrollmentTypeDTO dto : enrollmentTypes) {
            MajorEntity major = majorRepository.findById(dto.getMajorId())
                    .orElseThrow(() -> new EntityNotFoundException("Major not found with id: " + dto.getMajorId()));

            EnrollmentTypeEntity enrollmentEntity = new EnrollmentTypeEntity();
            enrollmentEntity.setMajor(major);
            enrollmentEntity.setEnrollmentType(EnrollmentType.valueOf(dto.getEnrollmentType()));
            enrollmentEntity.setCourseDetail(course);
            enrollmentEntities.add(enrollmentEntity);
        }

        // Remove existing enrollment types and add the updated ones
        course.getEnrollmentTypes().clear();
        course.getEnrollmentTypes().addAll(enrollmentEntities);

        courseDetailRepository.save(course);
    }
}
