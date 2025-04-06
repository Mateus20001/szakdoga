package com.szakdoga.backend.courses.controllers;

import com.szakdoga.backend.courses.dtos.*;
import com.szakdoga.backend.courses.models.CourseApplicationEntity;
import com.szakdoga.backend.courses.models.CourseTimetablePlannerEntity;
import com.szakdoga.backend.courses.services.CourseApplicationService;
import com.szakdoga.backend.courses.services.CourseDateService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/course-applications")
public class CourseApplicationController {
    private static final Logger log = LoggerFactory.getLogger(CourseApplicationController.class);
    @Autowired
    private CourseApplicationService courseApplicationService;
    @Autowired
    private CourseDateService courseDateService;


    @PostMapping("/apply")
    public ResponseEntity<String> applyToCourse(@RequestBody Map<String, Long> requestBody) {
        log.info("Start applying to course");

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        Object principal = authentication.getPrincipal();
        if (!(principal instanceof UserDetails)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }


        UserDetails userDetails = (UserDetails) principal;
        String userId = userDetails.getUsername();
        Long courseDateId = requestBody.get("courseDateId"); // Extract courseDateId from request body

        if (courseDateId == null) {
            return ResponseEntity.badRequest().body("{\"message\": \"Course Date ID is missing.\"}");
        }

        log.info("User ID: {}, Course Date ID: {}", userId, courseDateId);

        try {
            courseApplicationService.applyToCourse(userId, courseDateId);
            return ResponseEntity.ok("{\"message\": \"Sikeresen felvetted a kurzust!\"}");
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body("{\"message\": \"Hiba történt a kurzus felvételekor!\"}");
        }
    }

    @DeleteMapping("/remove/{courseDateId}")
    public ResponseEntity<String> removeApplication(
            @PathVariable Long courseDateId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || !authentication.isAuthenticated()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build(); // Return 401 if not authenticated
        }

        Object principal = authentication.getPrincipal();
        if (!(principal instanceof UserDetails)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build(); // Return 403 if unauthorized
        }

        UserDetails userDetails = (UserDetails) principal;
        String userId = userDetails.getUsername();
        if (courseDateId == null) {
            return ResponseEntity.badRequest().body("{\"message\": \"Course Date ID is missing.\"}");
        }
        try {
            courseApplicationService.removeApplication(userId, courseDateId);
            return ResponseEntity.ok("{\"message\": \"Sikeresen leadtad a kurzust!\"}");
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body("{\"message\": \"Hiba történt a kurzus felvételekor!\"}");
        }
    }

    @GetMapping("/user-applications/{courseId}")
    public ResponseEntity<List<CourseApplicationDTO>> getUserApplicationsByCourse(@PathVariable Long courseId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || !authentication.isAuthenticated()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build(); // Return 401 if not authenticated
        }

        Object principal = authentication.getPrincipal();
        if (!(principal instanceof UserDetails)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build(); // Return 403 if unauthorized
        }

        UserDetails userDetails = (UserDetails) principal;
        String userId = userDetails.getUsername();

        if (userId == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        log.info("User ID: {}, Course ID: {}", userId, courseId);
        // Retrieve the course applications based on courseId and userId
        List<CourseApplicationEntity> applications = courseApplicationService.getUserApplicationsForCourse(userId, courseId);
        log.info("SIZE: {}", applications.size());
        List<CourseApplicationDTO> applicationDTOs = applications.stream()
                .map(app -> new CourseApplicationDTO(app.getId(), app.getCourseDateEntity().getId()))
                .collect(Collectors.toList());

        return ResponseEntity.ok(applicationDTOs);
    }

    @GetMapping("/applied-courses")
    public ResponseEntity<List<UserAppliedCourseDto>> getUserAppliedCourses() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || !authentication.isAuthenticated()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build(); // Return 401 if not authenticated
        }

        Object principal = authentication.getPrincipal();
        if (!(principal instanceof UserDetails)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build(); // Return 403 if unauthorized
        }

        UserDetails userDetails = (UserDetails) principal;
        String userId = userDetails.getUsername();

        List<UserAppliedCourseDto> appliedCourses = courseApplicationService.getUserAppliedCourses(userId);
        log.info("appliedCourses: {}", appliedCourses.getFirst().getCourseDetailName());
        return ResponseEntity.ok(appliedCourses);
    }

    @GetMapping("/timetable-courses")
    public ResponseEntity<List<CourseDateResponse>> getUserTimetablePlanner() {
        log.info("asd");
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || !authentication.isAuthenticated()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build(); // Return 401 if not authenticated
        }

        Object principal = authentication.getPrincipal();
        if (!(principal instanceof UserDetails)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build(); // Return 403 if unauthorized
        }

        UserDetails userDetails = (UserDetails) principal;
        String userId = userDetails.getUsername();
        List<CourseTimetableEntityDto> timetableDTOs = courseApplicationService.getUserTimetable(userId);
        List<Long> courseDateIds = timetableDTOs.stream()
                .map(CourseTimetableEntityDto::getCourseDateId)
                .collect(Collectors.toList());
        List<CourseDateResponse> courseDateResponses = courseDateService.findAllById(courseDateIds);

        return ResponseEntity.ok(courseDateResponses);
    }

    @PostMapping("/add-timetable")
    public ResponseEntity<CourseTimetableEntityDto> addCourseToTimetable(@RequestBody AddTimetableRequest request) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || !authentication.isAuthenticated()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build(); // Return 401 if not authenticated
        }

        Object principal = authentication.getPrincipal();
        if (!(principal instanceof UserDetails)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build(); // Return 403 if unauthorized
        }

        UserDetails userDetails = (UserDetails) principal;
        String userId = userDetails.getUsername();
        courseApplicationService.addCourseToTimetable(userId, request.getCourseDateId());
        return ResponseEntity.ok(new CourseTimetableEntityDto());
    }
    @DeleteMapping("/remove-timetable/{id}")
    public ResponseEntity<CourseTimetableEntityDto> removeFromTimetable(@PathVariable long id) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || !authentication.isAuthenticated()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build(); // Return 401 if not authenticated
        }

        Object principal = authentication.getPrincipal();
        if (!(principal instanceof UserDetails)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build(); // Return 403 if unauthorized
        }

        UserDetails userDetails = (UserDetails) principal;
        String userId = userDetails.getUsername();
        courseApplicationService.removeTimetableEntity(id);
        return ResponseEntity.ok(new CourseTimetableEntityDto());
    }
}
