package com.szakdoga.backend.courses.dtos;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CourseTeacherDTO {
    private String teacherId;
    private boolean responsible;

    public CourseTeacherDTO(String id, boolean responsible) {
        teacherId = id;
        this.responsible = responsible;
    }

    public CourseTeacherDTO() {
    }
}
