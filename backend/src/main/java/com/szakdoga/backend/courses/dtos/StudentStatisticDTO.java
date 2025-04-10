package com.szakdoga.backend.courses.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class StudentStatisticDTO {
    String courseDetailName;
    int bestGradeValue;
    String semester;
    int creditScore;
}
