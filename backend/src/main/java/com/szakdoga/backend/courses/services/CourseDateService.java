package com.szakdoga.backend.courses.services;


import com.szakdoga.backend.auth.model.User;
import com.szakdoga.backend.auth.repositories.UserRepository;
import com.szakdoga.backend.courses.dtos.CourseDateRequest;
import com.szakdoga.backend.courses.dtos.CourseDateResponse;
import com.szakdoga.backend.courses.dtos.EditCourseDateRequest;
import com.szakdoga.backend.courses.models.CourseDateEntity;
import com.szakdoga.backend.courses.models.CourseDetailEntity;
import com.szakdoga.backend.courses.repositories.CourseDateRepository;
import com.szakdoga.backend.courses.repositories.CourseDetailRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CourseDateService {
    private static final Logger log = LoggerFactory.getLogger(CourseDateService.class);

    @Autowired
    private final CourseDateRepository courseDateRepository;

    @Autowired
    private final CourseDetailRepository courseDetailRepository;

    @Autowired
    private final UserRepository userRepository;

    public CourseDateEntity addCourseDate(CourseDateEntity courseDateEntity, Long courseId, List<String> teacherIds) {
        // Validate CourseDetail
        CourseDetailEntity courseDetail = courseDetailRepository.findById(courseId)
                .orElseThrow(() -> new IllegalArgumentException("Course not found with ID: " + courseId));
        courseDateEntity.setCourseDetailEntity(courseDetail);

        // Validate and set teachers
        List<User> teachers = userRepository.findAllById(teacherIds);
        if (teachers.isEmpty()) {
            throw new IllegalArgumentException("No valid teachers found for IDs: " + teacherIds);
        }
        courseDateEntity.setTeachers(teachers);

        // Save the entity
        return courseDateRepository.save(courseDateEntity);
    }
    public List<CourseDateEntity> getCourseDatesByCourseId(Long courseId) {
        return courseDateRepository.findByCourseDetailEntityId(courseId);
    }
    @Transactional
    public List<CourseDateResponse> getCourseDatesAsRequestObjects(Long courseId) {
        log.info("getCourseDatesAsRequestObjects courseDates: ");
        List<CourseDateEntity> courseDates = courseDateRepository.findByCourseDetailEntityId(courseId);

        return courseDates.stream().map(courseDate -> {
            CourseDateResponse request = new CourseDateResponse();
            request.setId(courseDate.getId());
            request.setName(courseDate.getName());
            request.setCourseId(courseDate.getCourseDetailEntity().getId());
            request.setTeacherIds(courseDate.getTeachers().stream()
                    .map(User::getId)
                    .toList());
            request.setDayOfWeek(courseDate.getDayOfWeek());
            request.setStartTime(courseDate.getStartTime());
            request.setEndTime(courseDate.getEndTime());
            return request;
        }).toList();
    }

    public CourseDateEntity updateCourseDate(EditCourseDateRequest request) {
        // Find the existing CourseDateEntity by ID
        log.info("OK");
        Optional<CourseDateEntity> optionalCourseDate = courseDateRepository.findById(request.getId());

        if (optionalCourseDate.isEmpty()) {
            // Return null if the course date is not found
            return null;
        }

        // Update the existing entity with new values from the request
        CourseDateEntity courseDateEntity = optionalCourseDate.get();
        courseDateEntity.setName(request.getName());
        CourseDetailEntity courseDetail = courseDetailRepository.findById(request.getCourseId())
                .orElseThrow(() -> new IllegalArgumentException("Course not found with ID: " + request.getCourseId()));
        courseDateEntity.setCourseDetailEntity(courseDetail);
        List<User> teachers = userRepository.findAllById(request.getTeacherIds());

        if (teachers.isEmpty()) {
            throw new IllegalArgumentException("No valid teachers found for IDs: " + request.getTeacherIds());
        }

        List<User> existingTeachers = courseDateEntity.getTeachers();
        if (existingTeachers != null) {
            existingTeachers.removeIf(teacher -> !request.getTeacherIds().contains(teacher.getId()));
        }

        courseDateEntity.setTeachers(teachers);
        courseDateEntity.setDayOfWeek(request.getDayOfWeek());
        courseDateEntity.setStartTime(request.getStartTime());
        courseDateEntity.setEndTime(request.getEndTime());

        // Save the updated entity back to the database
        return courseDateRepository.save(courseDateEntity);
    }
    public boolean deleteCourseDate(Long id) {
        Optional<CourseDateEntity> courseDateOpt = courseDateRepository.findById(id);
        if (courseDateOpt.isPresent()) {
            CourseDateEntity courseDate = courseDateOpt.get();

            // Clear teacher associations
            if (courseDate.getTeachers() != null) {
                courseDate.getTeachers().clear();
            }

            // Delete the course date
            courseDateRepository.delete(courseDate);
            return true;
        }
        return false;
    }
}