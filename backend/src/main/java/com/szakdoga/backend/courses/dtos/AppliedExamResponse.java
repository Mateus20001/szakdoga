package com.szakdoga.backend.courses.dtos;

import com.szakdoga.backend.courses.models.ExamType;
import com.szakdoga.backend.courses.models.LocationEnum;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AppliedExamResponse {
    private Long id;
    private LocalDateTime examDate;
    private LocationEnum location;
    private int longevity;
    private ExamType type;
    private Long courseApplicationId;
}