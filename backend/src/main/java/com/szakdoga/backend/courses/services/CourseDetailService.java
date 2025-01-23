package com.szakdoga.backend.courses.services;

import com.szakdoga.backend.auth.model.MajorEntity;
import com.szakdoga.backend.auth.model.User;
import com.szakdoga.backend.auth.repositories.MajorRepository;
import com.szakdoga.backend.auth.repositories.UserRepository;
import com.szakdoga.backend.courses.controllers.CourseDetailController;
import com.szakdoga.backend.courses.dtos.CourseDetailListingDTO;
import com.szakdoga.backend.courses.dtos.CourseTeacherDTO;
import com.szakdoga.backend.courses.dtos.EnrollmentTypeDTO;
import com.szakdoga.backend.courses.models.*;
import com.szakdoga.backend.courses.repositories.CourseDetailRepository;
import com.szakdoga.backend.courses.repositories.CourseTeacherRepository;
import com.szakdoga.backend.courses.repositories.EnrollmentTypeRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class CourseDetailService {
    private static final Logger log = LoggerFactory.getLogger(CourseDetailService.class);

    @Autowired
    private CourseDetailRepository courseDetailRepository;

    @Autowired
    private CourseTeacherRepository courseTeacherRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private MajorRepository majorRepository;

    @Autowired
    private EnrollmentTypeRepository enrollmentTypeRepository;

    // Add new CourseDetailEntity
    public CourseDetailEntity addCourse(String name, String description, int credits, int recommendedHalfYear, String requirementType,
                                        List<Long> requiredCourseIds, List<EnrollmentTypeEntity> enrollmentTypes, List<CourseTeacherEntity> teachers) {
        // Fetch required courses by IDs
        List<CourseDetailEntity> requiredCourses = courseDetailRepository.findAllById(requiredCourseIds);

        // Create new course
        CourseDetailEntity courseDetail = new CourseDetailEntity();
        courseDetail.setName(name);
        courseDetail.setDescription(description);
        courseDetail.setCredits(credits);
        courseDetail.setRecommended_half_year(recommendedHalfYear);
        courseDetail.setRequirementType(RequirementType.valueOf(requirementType));
        courseDetail.setRequiredCourses(requiredCourses);


        // Save the course
        CourseDetailEntity savedCourse = courseDetailRepository.save(courseDetail);

        if (teachers != null && !teachers.isEmpty()) {
            teachers.forEach(teacherEntity -> {
                teacherEntity.setCourseDetail(savedCourse);
                courseTeacherRepository.save(teacherEntity);
            });
        }

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
                .collect(Collectors.toList());
    }
    @Transactional
    public void updateCourse(Long courseId, String name, String description, int credits, int recomendedHalfYear,
                             String requirementType, List<Long> requiredCourseIds,
                             List<EnrollmentTypeDTO> enrollmentTypes, List<CourseTeacherDTO> teachers) {
        CourseDetailEntity course = courseDetailRepository.findById(courseId)
                .orElseThrow(() -> new EntityNotFoundException("Course not found with id: " + courseId));

        course.setName(name);
        course.setDescription(description);
        course.setCredits(credits);
        course.setRecommended_half_year(recomendedHalfYear);
        course.setRequirementType(RequirementType.valueOf(requirementType));

        if (requiredCourseIds != null && !requiredCourseIds.isEmpty()) {
            List<CourseDetailEntity> requiredCourses = courseDetailRepository.findAllById(requiredCourseIds);
            course.setRequiredCourses(requiredCourses);
        }

        if (teachers != null && !teachers.isEmpty()) {
            // Get existing teacher IDs
            List<String> incomingTeacherIds = teachers.stream()
                    .map(CourseTeacherDTO::getTeacherId)
                    .toList();

            // Remove teachers not in the incoming list
            course.getCourseTeachers().removeIf(existing -> {
                boolean toRemove = !incomingTeacherIds.contains(existing.getTeacher().getId());
                if (toRemove) {
                    log.info("TÖRÖLVE: " + existing.getTeacher().getId());
                }
                return toRemove;
            });

            // Add or update teachers in the incoming list
            for (CourseTeacherDTO teacherDTO : teachers) {
                String teacherId = teacherDTO.getTeacherId();
                boolean responsible = teacherDTO.isResponsible();

                // Find existing or create a new CourseTeacherEntity
                Optional<CourseTeacherEntity> existingTeacher = course.getCourseTeachers().stream()
                        .filter(ct -> ct.getTeacher().getId().equals(teacherId))
                        .findFirst();

                if (existingTeacher.isEmpty()) {
                    User teacher = userRepository.findById(teacherId)
                            .orElseThrow(() -> new EntityNotFoundException("Teacher not found with id: " + teacherId));
                    CourseTeacherEntity newTeacher = new CourseTeacherEntity();
                    newTeacher.setCourseDetail(course);
                    newTeacher.setTeacher(teacher);
                    newTeacher.setResponsible(responsible);
                    course.getCourseTeachers().add(newTeacher);
                } else {
                    // Update the responsible flag for existing teacher
                    existingTeacher.get().setResponsible(responsible);
                }
            }
        }
        if (enrollmentTypes != null && !enrollmentTypes.isEmpty()) {
            // Similar logic for enrollment types
            List<Long> incomingMajorIds = enrollmentTypes.stream()
                    .map(EnrollmentTypeDTO::getMajorId)
                    .toList();

            course.getEnrollmentTypes().removeIf(existing -> {
                boolean toRemove = !incomingMajorIds.contains(existing.getMajor().getId());
                if (toRemove) {
                    log.info("Enrollment type removed for major ID: " + existing.getMajor().getId());
                }
                return toRemove;
            });

            for (EnrollmentTypeDTO enrollmentDTO : enrollmentTypes) {
                MajorEntity major = majorRepository.findById(enrollmentDTO.getMajorId())
                        .orElseThrow(() -> new EntityNotFoundException("Major not found with id: " + enrollmentDTO.getMajorId()));

                Optional<EnrollmentTypeEntity> existingEnrollment = course.getEnrollmentTypes().stream()
                        .filter(et -> et.getMajor().getId() == enrollmentDTO.getMajorId()) // Compare primitive long
                        .findFirst();

                if (existingEnrollment.isEmpty()) {
                    EnrollmentTypeEntity newEnrollment = new EnrollmentTypeEntity();
                    newEnrollment.setMajor(major);
                    newEnrollment.setCourseDetail(course);
                    newEnrollment.setEnrollmentType(EnrollmentType.valueOf(enrollmentDTO.getEnrollmentType()));
                    course.getEnrollmentTypes().add(newEnrollment);
                }
            }
        }
        // Save the updated course
        courseDetailRepository.save(course);
    }
}
