package com.szakdoga.backend.courses.services;

import com.szakdoga.backend.auth.model.RoleEntity;
import com.szakdoga.backend.auth.model.User;
import com.szakdoga.backend.auth.repositories.RoleRepository;
import com.szakdoga.backend.auth.repositories.UserRepository;
import com.szakdoga.backend.courses.models.CourseDetailEntity;
import com.szakdoga.backend.courses.models.CourseTeacherEntity;
import com.szakdoga.backend.courses.repositories.CourseTeacherRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CourseTeacherService {
    private static final Logger log = LoggerFactory.getLogger(CourseTeacherService.class);

    @Autowired
    private CourseTeacherRepository courseTeacherRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    public List<CourseTeacherEntity> findByCourseId(Long courseId) {
        return courseTeacherRepository.findByCourseDetailId(courseId);
    }

    public void saveCourseTeacher(CourseTeacherEntity courseTeacher) {
        if (courseTeacher.getCourseDetail() == null || courseTeacher.getTeacher() == null) {
            throw new IllegalArgumentException("Course and Teacher must not be null");
        }

        // Check if the CourseTeacherEntity already exists
        Optional<CourseTeacherEntity> existingCourseTeacher = courseTeacherRepository
                .findByCourseDetailAndTeacher(courseTeacher.getCourseDetail(), courseTeacher.getTeacher());

        if (existingCourseTeacher.isPresent()) {
            // If the entity exists, update it
            CourseTeacherEntity existingEntity = existingCourseTeacher.get();

            // Update only the 'responsible' field or any other field you need
            existingEntity.setResponsible(courseTeacher.isResponsible());

            // Save the updated entity
            courseTeacherRepository.save(existingEntity);
        } else {
            // If it doesn't exist, save the new entity
            courseTeacherRepository.save(courseTeacher);
        }
        addTeacherRoleToUser(courseTeacher.getTeacher().getId());
    }
    public boolean isResponsibleTeacherForCourse(Long courseId, String userId) {
        return courseTeacherRepository.existsByCourseDetailIdAndTeacherIdAndResponsible(courseId, userId, true);
    }
    public CourseTeacherEntity findByCourseAndTeacher(CourseDetailEntity course, User teacher) {

        return courseTeacherRepository.findByCourseDetailAndTeacher(course, teacher).orElse(null);
    }
    @Transactional
    public void deleteCourseTeacher(CourseTeacherEntity courseTeacher) {
        courseTeacher.setCourseDetail(null);  // Unset the course reference
        courseTeacher.setTeacher(null);
        courseTeacherRepository.delete(courseTeacher);
        courseTeacherRepository.deleteOrphanedCourseTeacherEntities();
    }

    @Transactional
    public CourseTeacherEntity findByTeacherId(String userId, Long courseId) {
        log.info(userId, courseId);
        return courseTeacherRepository.findByCourseDetailIdAndTeacherId(courseId, userId).orElse(null);
    }
    /**
     * Adds the 'TEACHER' role to the user with the specified user ID.
     *
     * @param userId The ID of the user to whom the role should be added.
     */
    private void addTeacherRoleToUser(String userId) {
        Optional<User> userOptional = userRepository.findById(userId);

        if (userOptional.isPresent()) {
            User user = userOptional.get();

            // Check if the user already has the 'TEACHER' role
            boolean hasTeacherRole = user.getRoles().stream()
                    .anyMatch(role -> role.getRoleName().equalsIgnoreCase("TEACHER"));

            if (!hasTeacherRole) {
                RoleEntity teacherRole = new RoleEntity(user, "TEACHER");

                roleRepository.save(teacherRole);
            }
        } else {
            throw new EntityNotFoundException("User with ID " + userId + " not found");
        }
    }
}