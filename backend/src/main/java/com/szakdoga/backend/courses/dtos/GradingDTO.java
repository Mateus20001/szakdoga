package com.szakdoga.backend.courses.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class GradingDTO {
    private String identifier;
    private int gradeValue;
    private Long courseDateId;
}
