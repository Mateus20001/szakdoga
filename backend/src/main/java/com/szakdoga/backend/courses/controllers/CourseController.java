package com.szakdoga.backend.courses.controllers;

import com.szakdoga.backend.auth.model.User;
import com.szakdoga.backend.auth.services.UserService;
import com.szakdoga.backend.courses.dtos.CourseDetailListingDTO;
import com.szakdoga.backend.courses.dtos.CourseTeacherDTO;
import com.szakdoga.backend.courses.models.CourseDetailEntity;
import com.szakdoga.backend.courses.models.CourseTeacherEntity;
import com.szakdoga.backend.courses.repositories.CourseTeacherRepository;
import com.szakdoga.backend.courses.services.CourseService;
import com.szakdoga.backend.courses.services.CourseTeacherService;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/api/courses")
public class CourseController {
    @Autowired
    private CourseService courseService;
    private static final Logger log = LoggerFactory.getLogger(CourseController.class);

    @Autowired
    private UserService userService;  // Assuming you have a UserService to fetch user details
    @Autowired
    private CourseTeacherService courseTeacherService;
    // Endpoint to get courses by teacher ID from the authToken
    @GetMapping("/by-teacher")
    public ResponseEntity<List<CourseDetailListingDTO>> getCoursesByTeacher(HttpServletRequest request) {
        // Get the authentication object from the SecurityContext
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        // Check if the user is authenticated
        if (authentication == null || !authentication.isAuthenticated()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build(); // Return 401 if not authenticated
        }

        // Get the principal (user details) from the authentication object
        Object principal = authentication.getPrincipal();
        if (!(principal instanceof UserDetails)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build(); // Return 403 if unauthorized
        }

        UserDetails userDetails = (UserDetails) principal;
        String teacherId = userDetails.getUsername(); // Assuming teacherId is stored in the username field
        log.info("getCoursesByTeacher: " + teacherId);
        // Verify the user exists
        User user = userService.findUserById(teacherId);
        if (user == null) {
            return ResponseEntity.notFound().build(); // Return 404 if user not found
        }

        // Fetch the courses assigned to the teacher
        List<CourseDetailEntity> courses = courseService.getCoursesByTeacherId(teacherId);
        List<CourseDetailListingDTO> courseDetailDTOs = courses.stream()
                .map(course -> new CourseDetailListingDTO(
                        course.getId(),
                        course.getName(),
                        course.getDescription(),
                        course.getCredits()))
                .collect(Collectors.toList());

        // Return the mapped list of DTOs
        return ResponseEntity.ok(courseDetailDTOs);
    }

    @GetMapping("/teachers/{courseId}")
    public ResponseEntity<List<CourseTeacherDTO>> getTeachersByCourseId(@PathVariable Long courseId) {
        log.info("getTeachersByCourseId: ");

        // Fetch the course detail by ID (Optional: validate if the course exists)
        CourseDetailEntity course = courseService.findCourseById(courseId);
        if (course == null) {
            return ResponseEntity.notFound().build();
        }

        // Fetch the course-teacher relationships
        List<CourseTeacherEntity> courseTeachers = courseTeacherService.findByCourseId(courseId);

        if (courseTeachers.isEmpty()) {
            return ResponseEntity.noContent().build(); // Return 204 if no teachers found
        }
        List<CourseTeacherDTO> teacherDTOs = courseTeachers.stream()
                .map(courseTeacher -> new CourseTeacherDTO(
                        courseTeacher.getTeacher().getId(),  // Teacher ID
                        courseTeacher.isResponsible()         // Whether the teacher is responsible
                ))
                .collect(Collectors.toList());
        // Return the list of teachers
        return ResponseEntity.ok(teacherDTOs);
    }
}
