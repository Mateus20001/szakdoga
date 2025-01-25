package com.szakdoga.backend.courses.controllers;

import com.szakdoga.backend.auth.model.User;
import com.szakdoga.backend.auth.services.UserService;
import com.szakdoga.backend.courses.dtos.CourseDetailListingDTO;
import com.szakdoga.backend.courses.dtos.CourseTeacherDTO;
import com.szakdoga.backend.courses.models.CourseDetailEntity;
import com.szakdoga.backend.courses.models.CourseTeacherEntity;
import com.szakdoga.backend.courses.repositories.CourseTeacherRepository;
import com.szakdoga.backend.courses.services.CourseDetailService;
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
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
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
    @Autowired
    private CourseDetailService courseDetailService;

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
    @GetMapping("/current-teacher/{courseId}")
    public ResponseEntity<CourseTeacherDTO> getCurrentTeacherByCourseId(@PathVariable Long courseId) {
        log.info("Received courseId: {}", courseId);
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || !authentication.isAuthenticated()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build(); // Return 401 if not authenticated
        }

        Object principal = authentication.getPrincipal();
        if (!(principal instanceof UserDetails userDetails)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build(); // Return 403 if unauthorized
        }

        String userId = userDetails.getUsername();
        log.info(userId, courseId);
        log.info("Received courseId: {}", courseId);
        CourseTeacherEntity currentCourseTeacher = courseTeacherService.findByTeacherId(userId, courseId);
        if (currentCourseTeacher == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        return ResponseEntity.ok(new CourseTeacherDTO(
                currentCourseTeacher.getTeacher().getId(),
                currentCourseTeacher.isResponsible()));
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

    @PostMapping("/teachers/{courseId}")
    public ResponseEntity<Void> addTeacherToCourse(
            @PathVariable Long courseId,
            @RequestBody CourseTeacherDTO request,
            Principal principal) { // Use Principal to identify the logged-in user
        log.info("addTeacherToCourse: courseId={}, teacherId={}, responsible={}",
                courseId, request.getTeacherId(), request.isResponsible());

        // Get the logged-in user
        User currentUser = userService.findUserById(principal.getName());
        if (currentUser == null) {
            log.error("Unauthorized access attempt");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build(); // 401 Unauthorized
        }

        // Check if the course exists
        CourseDetailEntity course = courseDetailService.getCourseDetailById(courseId);
        if (course == null) {
            return ResponseEntity.notFound().build();
        }

        // Check if the logged-in user is responsible for this course
        boolean isResponsible = courseTeacherService.isResponsibleTeacherForCourse(courseId, currentUser.getId());
        if (!isResponsible) {
            log.error("User is not authorized to modify this course: userId={}, courseId={}",
                    currentUser.getId(), courseId);
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build(); // 403 Forbidden
        }

        // Check if the teacher to be added exists
        User teacher = userService.findUserById(request.getTeacherId());
        if (teacher == null) {
            return ResponseEntity.badRequest().build(); // 400 if teacher not found
        }
        try {
            // Create a new CourseTeacherEntity and save it
            CourseTeacherEntity courseTeacher = new CourseTeacherEntity();
            courseTeacher.setCourseDetail(course);
            courseTeacher.setTeacher(teacher);
            courseTeacher.setResponsible(request.isResponsible());

            courseTeacherService.saveCourseTeacher(courseTeacher);
        } catch (IllegalStateException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }

        // Return a response indicating success
        return ResponseEntity.ok().build();
    }
    @PutMapping("/teachers/{courseId}")
    public ResponseEntity<Void> updateTeacherInCourse(
            @PathVariable Long courseId,
            @RequestBody CourseTeacherDTO request) {
        log.info("updateTeacherInCourse: courseId={}, teacherId={}, responsible={}",
                courseId, request.getTeacherId(), request.isResponsible());

        // Check if the course exists
        CourseDetailEntity course = courseDetailService.getCourseDetailById(courseId);
        if (course == null) {
            return ResponseEntity.notFound().build(); // 404 if course not found
        }

        // Check if the teacher exists
        User teacher = userService.findUserById(request.getTeacherId());
        if (teacher == null) {
            return ResponseEntity.badRequest().build(); // 400 if teacher not found
        }

        // Fetch the existing CourseTeacherEntity
        CourseTeacherEntity courseTeacher = courseTeacherService.findByCourseAndTeacher(course, teacher);

        log.info("courseTeacher");
        if (courseTeacher == null) {
            return ResponseEntity.notFound().build(); // 404 if the teacher is not associated with the course
        }
        log.info("minden ok");
        // Update the responsibility status
        courseTeacher.setResponsible(request.isResponsible());
        courseTeacherService.saveCourseTeacher(courseTeacher);

        return ResponseEntity.ok().build(); // 200 success
    }
    @DeleteMapping("/teachers/{courseId}/{teacherId}")
    public ResponseEntity<Void> removeTeacherFromCourse(
            @PathVariable Long courseId,
            @PathVariable String teacherId) {
        log.info("removeTeacherFromCourse: courseId={}, teacherId={}", courseId, teacherId);

        // Check if the course exists
        CourseDetailEntity course = courseDetailService.getCourseDetailById(courseId);
        if (course == null) {
            return ResponseEntity.notFound().build();
        }

        // Check if the teacher exists
        User teacher = userService.findUserById(teacherId);
        if (teacher == null) {
            return ResponseEntity.badRequest().build();
        }

        // Fetch the existing CourseTeacherEntity
        CourseTeacherEntity courseTeacher = courseTeacherService.findByCourseAndTeacher(course, teacher);
        if (courseTeacher == null) {
            return ResponseEntity.notFound().build();
        }
        log.info("Minden ok");
        // Delete the relationship
        courseTeacherService.deleteCourseTeacher(courseTeacher);

        return ResponseEntity.noContent().build();
    }
}
