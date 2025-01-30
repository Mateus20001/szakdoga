package com.szakdoga.backend.courses.dtos;

import com.szakdoga.backend.courses.models.RequirementType;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CourseDetailStudentListingDTO {
    private long id;
    private String name;
    private int credits;
    private RequirementType requirementType;
    private String enrollmentType;
    private int recommendedHalfYear;
    private int course_dates_size;

    public CourseDetailStudentListingDTO(long id, String name, int credits, RequirementType requirementType, String enrollmentType, int recommendedHalfYear, int course_dates_size) {
        this.id = id;
        this.name = name;
        this.credits = credits;
        this.requirementType = requirementType;
        this.enrollmentType = enrollmentType;
        this.recommendedHalfYear = recommendedHalfYear;
        this.course_dates_size = course_dates_size;
    }
}
