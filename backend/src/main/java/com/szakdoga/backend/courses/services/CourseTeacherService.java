package com.szakdoga.backend.courses.services;

import com.szakdoga.backend.courses.models.CourseTeacherEntity;
import com.szakdoga.backend.courses.repositories.CourseTeacherRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CourseTeacherService {

    @Autowired
    private CourseTeacherRepository courseTeacherRepository;

    public List<CourseTeacherEntity> findByCourseId(Long courseId) {
        return courseTeacherRepository.findByCourseDetailId(courseId);
    }
}