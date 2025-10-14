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
@AllArgsConstructor
@NoArgsConstructor
public class AddExamRequest {
    private String examDate;
    private String location;
    private int longevity;
    private ExamType type;
    private Long courseDateId;
}
