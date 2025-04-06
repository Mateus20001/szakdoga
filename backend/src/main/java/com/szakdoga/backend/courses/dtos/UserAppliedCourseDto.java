package com.szakdoga.backend.courses.dtos;

import com.szakdoga.backend.courses.models.LocationEnum;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserAppliedCourseDto {
    private String courseDetailName;
    private Long courseDateId;
    private String courseDateName;
    private List<String> teacherIds;
    private LocationEnum location;
    private List<AppliedGradeDTO> grades;

    // Getters & Setters
}

