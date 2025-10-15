package com.szakdoga.backend.courses.dtos;

import com.szakdoga.backend.courses.models.ExamType;
import com.szakdoga.backend.courses.models.LocationEnum;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class ExamResponse {
    private Long id;
    private String examDate;
    private String location;
    private int longevity;
    private ExamType type;
    private boolean applied;
    private String courseDateName;
}
