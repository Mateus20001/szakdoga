package com.szakdoga.backend.courses.dtos;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class EnrollmentTypeDTO {
    // Getters and setters
    private Long majorId;
    private String enrollmentType; // This corresponds to the EnrollmentType enum as a string

}
