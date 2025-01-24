package com.szakdoga.backend.courses.services;

import com.szakdoga.backend.courses.controllers.CourseController;
import com.szakdoga.backend.courses.models.CourseDetailEntity;
import com.szakdoga.backend.courses.models.CourseTeacherEntity;
import com.szakdoga.backend.courses.repositories.CourseDetailRepository;
import com.szakdoga.backend.courses.repositories.CourseTeacherRepository;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CourseService {
    @Autowired
    private CourseDetailRepository courseDetailRepository;
    private static final Logger log = LoggerFactory.getLogger(CourseService.class);

    @Autowired
    private CourseTeacherRepository courseTeacherRepository;

    @Transactional
    public List<CourseDetailEntity> getCoursesByTeacherId(String teacherId) {
        return courseTeacherRepository.findCoursesByTeacherId(teacherId);
    }

    public CourseDetailEntity findCourseById(Long courseId) {
        return courseDetailRepository.findById(courseId).orElse(null);
    }
}
