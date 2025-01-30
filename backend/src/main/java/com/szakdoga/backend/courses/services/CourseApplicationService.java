package com.szakdoga.backend.courses.services;

import com.szakdoga.backend.auth.model.User;
import com.szakdoga.backend.auth.repositories.UserRepository;
import com.szakdoga.backend.courses.controllers.CourseController;
import com.szakdoga.backend.courses.models.CourseApplicationEntity;
import com.szakdoga.backend.courses.models.CourseDateEntity;
import com.szakdoga.backend.courses.repositories.CourseApplicationRepository;
import com.szakdoga.backend.courses.repositories.CourseDateRepository;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CourseApplicationService {
    private final CourseApplicationRepository courseApplicationRepository;
    private final UserRepository userRepository;
    private final CourseDateRepository courseDateRepository;
    private static final Logger log = LoggerFactory.getLogger(CourseApplicationService.class);

    public CourseApplicationService(
            CourseApplicationRepository courseApplicationRepository,
            UserRepository userRepository,
            CourseDateRepository courseDateRepository) {
        this.courseApplicationRepository = courseApplicationRepository;
        this.userRepository = userRepository;
        this.courseDateRepository = courseDateRepository;
    }
    @Transactional
    public void applyToCourse(String userId, Long courseDateId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("User not found"));
        // Fetch the course date entity
        CourseDateEntity courseDate = courseDateRepository.findById(courseDateId)
                .orElseThrow(() -> new RuntimeException("Course date not found"));

        // Check if the user has already applied
        Optional<CourseApplicationEntity> existingApplication =
                courseApplicationRepository.findByUserIdAndCourseDateEntity(userId, courseDate);

        if (existingApplication.isPresent()) {
            throw new RuntimeException("User has already applied to this course date.");
        }
        log.info("Applying course application to user {}", userId);
        // Create new course application
        CourseApplicationEntity application = new CourseApplicationEntity();
        application.setUser(user);
        application.setCourseDateEntity(courseDate);

        courseApplicationRepository.save(application);
    }

    @Transactional
    public void removeApplication(String userId, Long courseDateId) {
        // Fetch the course date entity
        CourseDateEntity courseDate = courseDateRepository.findById(courseDateId)
                .orElseThrow(() -> new RuntimeException("Course date not found"));

        // Find the application and delete it
        CourseApplicationEntity application = courseApplicationRepository
                .findByUserIdAndCourseDateEntity(userId, courseDate)
                .orElseThrow(() -> new RuntimeException("Application not found"));
        application.setCourseDateEntity(null);
        application.setUser(null);
        courseApplicationRepository.delete(application);
        courseApplicationRepository.deleteOrphanedCourseTeacherEntities();
    }

    @Transactional
    public List<CourseApplicationEntity> getUserApplicationsForCourse(String userId, Long courseId) {
        log.info("Getting user applications for course id {}", courseId);
        return courseApplicationRepository.findByUserIdAndCourseDateEntity_CourseDetailEntity_Id(userId, courseId);
    }
}