package com.szakdoga.backend.courses.controllers;

import com.szakdoga.backend.auth.controllers.MajorController;
import com.szakdoga.backend.auth.model.MajorEntity;
import com.szakdoga.backend.auth.repositories.MajorRepository;
import com.szakdoga.backend.courses.dtos.CourseDetailListingDTO;
import com.szakdoga.backend.courses.dtos.CourseDetailsRequest;
import com.szakdoga.backend.courses.dtos.EnrollmentTypeDTO;
import com.szakdoga.backend.courses.models.CourseDetailEntity;
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

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/course-details")
public class CourseDetailController {
    private static final Logger logger = LoggerFactory.getLogger(CourseDetailController.class);

    @Autowired
    private MajorRepository majorRepository;

    @Autowired
    private CourseDetailService courseDetailService;

    @Autowired
    private CourseDetailRepository courseDetailRepository;

    @PostMapping("/add")
    public CourseDetailEntity addCourse(@RequestBody CourseDetailsRequest request) {
        // Map enrollmentTypes from DTOs
        List<EnrollmentTypeEntity> enrollmentTypes = request.getEnrollmentTypes().stream().map(dto -> {
            EnrollmentTypeEntity entity = new EnrollmentTypeEntity();
            entity.setEnrollmentType(EnrollmentType.valueOf(dto.getEnrollmentType()));
            MajorEntity major = majorRepository.findById(dto.getMajorId())
                    .orElseThrow(() -> new IllegalArgumentException("Major not found with ID: " + dto.getMajorId()));

            entity.setMajor(major);
            return entity;
        }).collect(Collectors.toList());

        return courseDetailService.addCourse(
                request.getName(),
                request.getDescription(),
                request.getCredits(),
                request.getRequirementType(),
                request.getRequiredCourses(),
                enrollmentTypes
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

        CourseDetailsRequest dto = new CourseDetailsRequest();
        dto.setName(course.getName());
        dto.setDescription(course.getDescription());
        dto.setCredits(course.getCredits());
        dto.setRequirementType(course.getRequirementType().name());
        dto.setRequiredCourses(course.getRequiredCourses().stream()
                .map(CourseDetailEntity::getId)
                .collect(Collectors.toList()));
        dto.setEnrollmentTypes(course.getEnrollmentTypes().stream().map(enrollment -> {
            EnrollmentTypeDTO enrollmentDTO = new EnrollmentTypeDTO();
            enrollmentDTO.setMajorId(enrollment.getMajor().getId());
            enrollmentDTO.setEnrollmentType(enrollment.getEnrollmentType().name());
            return enrollmentDTO;
        }).collect(Collectors.toList()));

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
                request.getRequirementType(),
                request.getRequiredCourses(),
                request.getEnrollmentTypes()
        );

        return ResponseEntity.ok().build();
    }

}
