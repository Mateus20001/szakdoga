package com.szakdoga.backend.courses.controllers;

import com.szakdoga.backend.auth.model.User;
import com.szakdoga.backend.auth.model.MajorEntity;
import com.szakdoga.backend.auth.repositories.MajorRepository;
import com.szakdoga.backend.auth.repositories.UserRepository;
import com.szakdoga.backend.courses.dtos.CourseDetailListingDTO;
import com.szakdoga.backend.courses.dtos.CourseDetailsRequest;
import com.szakdoga.backend.courses.dtos.CourseTeacherDTO;
import com.szakdoga.backend.courses.dtos.EnrollmentTypeDTO;
import com.szakdoga.backend.courses.models.CourseDetailEntity;
import com.szakdoga.backend.courses.models.CourseTeacherEntity;
import com.szakdoga.backend.courses.models.EnrollmentType;
import com.szakdoga.backend.courses.models.EnrollmentTypeEntity;
import com.szakdoga.backend.courses.repositories.CourseDetailRepository;
import com.szakdoga.backend.courses.services.CourseDetailService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/course-details")
public class CourseDetailController {
    private static final Logger log = LoggerFactory.getLogger(CourseDetailController.class);

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private MajorRepository majorRepository;

    @Autowired
    private CourseDetailService courseDetailService;

    @Autowired
    private CourseDetailRepository courseDetailRepository;

    @PostMapping("/add")
    public CourseDetailEntity addCourse(@RequestBody CourseDetailsRequest request) {

        log.info("LESSGO");
        // Map enrollmentTypes from DTOs
        List<EnrollmentTypeEntity> enrollmentTypes = request.getEnrollmentTypes().stream().map(dto -> {
            EnrollmentTypeEntity entity = new EnrollmentTypeEntity();
            entity.setEnrollmentType(EnrollmentType.valueOf(dto.getEnrollmentType()));
            MajorEntity major = majorRepository.findById(dto.getMajorId())
                    .orElseThrow(() -> new IllegalArgumentException("Major not found with ID: " + dto.getMajorId()));

            entity.setMajor(major);
            return entity;
        }).collect(Collectors.toList());
        List<CourseTeacherDTO> teacherInfo = request.getTeachers();
        List<CourseTeacherEntity> teachers = teacherInfo.stream().map(teacherData -> {
            CourseTeacherEntity entity = new CourseTeacherEntity();
            String teacherId = teacherData.getTeacherId();
            boolean responsible = teacherData.isResponsible();

            User teacher = userRepository.findById(teacherId)
                    .orElseThrow(() -> new IllegalArgumentException("Teacher not found with ID: " + teacherId));
            entity.setTeacher(teacher);
            entity.setResponsible(responsible);
            return entity;
        }).collect(Collectors.toList());
        return courseDetailService.addCourse(
                request.getName(),
                request.getDescription(),
                request.getCredits(),
                request.getRecommendedHalfYear(),
                request.getRequirementType(),
                request.getRequiredCourses(),
                enrollmentTypes,
                teachers
        );
    }
    // List all CourseDetails
    @GetMapping("/all")
    public List<CourseDetailListingDTO> getAllCourses() {
        return courseDetailService.getAllCourses();
    }


    // Get a specific CourseDetail by ID
    @GetMapping("/{id}")
    public CourseDetailsRequest getCourseDetails(@PathVariable Long id) {
        CourseDetailEntity course = courseDetailRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Course not found"));

        // Create and populate the DTO
        CourseDetailsRequest dto = new CourseDetailsRequest();
        dto.setName(course.getName());
        dto.setDescription(course.getDescription());
        dto.setCredits(course.getCredits());
        dto.setRequirementType(course.getRequirementType().name());
        dto.setRecommendedHalfYear(course.getRecommended_half_year());
        dto.setRequiredCourses(course.getRequiredCourses().stream()
                .map(CourseDetailEntity::getId)
                .collect(Collectors.toList()));

        if (course.getCourseTeachers() != null) {
            dto.setTeachers(course.getCourseTeachers().stream()
                    .map(courseTeacher -> {
                        CourseTeacherDTO courseTeacherDTO = new CourseTeacherDTO();
                        courseTeacherDTO.setTeacherId(courseTeacher.getTeacher().getId()); // Assuming getTeacher() gets the User entity
                        courseTeacherDTO.setResponsible(courseTeacher.isResponsible()); // Assuming isResponsible() is a method in CourseTeacherEntity
                        return courseTeacherDTO;
                    }).collect(Collectors.toList()));
        } else {
            dto.setTeachers(Collections.emptyList());
        }
        // Populate enrollment types
        dto.setEnrollmentTypes(course.getEnrollmentTypes().stream().map(enrollment -> {
            EnrollmentTypeDTO enrollmentDTO = new EnrollmentTypeDTO();
            enrollmentDTO.setMajorId(enrollment.getMajor().getId());
            enrollmentDTO.setEnrollmentType(enrollment.getEnrollmentType().name());
            return enrollmentDTO;
        }).collect(Collectors.toList()));

        // Logging for debugging purposes
        log.info("Returning course details: {}", dto.getTeachers());

        return dto;
    }

    @PutMapping("/{id}")
    public ResponseEntity<Void> updateCourse(
            @PathVariable("id") Long id,
            @RequestBody CourseDetailsRequest request) {

        courseDetailService.updateCourse(
                id,
                request.getName(),
                request.getDescription(),
                request.getCredits(),
                request.getRecommendedHalfYear(),
                request.getRequirementType(),
                request.getRequiredCourses(),
                request.getEnrollmentTypes(),
                request.getTeachers()
        );

        return ResponseEntity.ok().build();
    }

}
