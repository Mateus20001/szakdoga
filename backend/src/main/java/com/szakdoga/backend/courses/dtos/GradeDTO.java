package com.szakdoga.backend.courses.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class GradeDTO{
    Long id;
    int gradeValue;
    String gradedBy;
    LocalDateTime creationDate;
    String studentId;

    public GradeDTO(Long id, int gradeValue, String id1, LocalDateTime creationDate) {
        this.id = id;
        this.gradeValue = gradeValue;
        this.studentId = id1;
        this.creationDate = creationDate;

    }
}