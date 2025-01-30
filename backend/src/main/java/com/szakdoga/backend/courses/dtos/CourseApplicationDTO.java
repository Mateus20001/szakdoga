package com.szakdoga.backend.courses.dtos;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CourseApplicationDTO {
    private Long applicationId;
    private Long courseDateId;

    public CourseApplicationDTO(Long applicationId, Long courseDateId) {
        this.applicationId = applicationId;
        this.courseDateId = courseDateId;
    }
}

