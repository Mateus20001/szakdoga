package com.szakdoga.backend.courses.dtos;

import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.Map;

@Setter
@Getter
public class CourseDetailsRequest {
    // Getters and setters
    private String name;
    private String description;
    private int credits;
    private int recommendedHalfYear;
    private String requirementType; // This corresponds to the RequirementType enum as a string
    private List<Long> requiredCourses; // Recursive structure for prerequisites
    private List<EnrollmentTypeDTO> enrollmentTypes;
    private List<CourseTeacherDTO> teachers;
}