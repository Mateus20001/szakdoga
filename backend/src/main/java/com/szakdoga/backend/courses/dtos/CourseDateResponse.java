package com.szakdoga.backend.courses.dtos;

import com.szakdoga.backend.courses.models.DayOfWeekEnum;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalTime;
import java.util.List;

@Getter
@Setter
public class CourseDateResponse {
    private long id;
    private String name;
    private Long courseId; // ID of the course
    private List<String> teacherIds; // IDs of the teachers
    private DayOfWeekEnum dayOfWeek;
    private LocalTime startTime;
    private LocalTime endTime;
}
