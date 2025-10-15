package com.szakdoga.backend.courses.controllers;

import com.szakdoga.backend.courses.dtos.AddExamRequest;
import com.szakdoga.backend.courses.dtos.AppliedExamResponse;
import com.szakdoga.backend.courses.dtos.ExamResponse;
import com.szakdoga.backend.courses.models.ExamApplicationEntity;
import com.szakdoga.backend.courses.services.ExamService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequestMapping("/api/exams")
public class ExamController {
    @Autowired
    private ExamService examService;
    private static final Logger log = LoggerFactory.getLogger(CourseController.class);


    @PreAuthorize("hasRole('STUDENT')")
    @PostMapping("/{id}/apply")
    public ResponseEntity<String> applyToExam(@PathVariable long id) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || !authentication.isAuthenticated()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build(); // 401
        }

        Object principal = authentication.getPrincipal();
        if (!(principal instanceof UserDetails)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build(); // 403
        }

        UserDetails userDetails = (UserDetails) principal;
        String userId = userDetails.getUsername();
        log.info("Applying user: " + userId + " and " + id);

        try {
            String message = examService.applyToExam(id, userId);
            return ResponseEntity.ok(message);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PreAuthorize("hasRole('TEACHER')")
    @PostMapping("/add")
    public ResponseEntity<?> addExam(@RequestBody AddExamRequest request) {
        log.info("ASD");
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
        try {
            log.info("ASD");
            examService.addExam(userId, request);
            return ResponseEntity.ok().build();
        } catch (ResponseStatusException e) {
            return ResponseEntity.status(403).body(e.getReason());
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body("Internal server error");
        }
    }
    @GetMapping("/me")
    public ResponseEntity<List<AppliedExamResponse>> getAllAppliedExams() {
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
        List<AppliedExamResponse> exams = examService.getAllAppliedExams(userId);
        return ResponseEntity.ok(exams);
    }
    @PreAuthorize("hasRole('STUDENT')")
    @GetMapping
    public ResponseEntity<List<ExamResponse>> getAllExams() {
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
        List<ExamResponse> response =  examService.getExamsForCurrentUser(userId);

        return ResponseEntity.ok(response);
    }

    @PreAuthorize("hasRole('STUDENT')")
    @DeleteMapping("/{id}/remove")
    public ResponseEntity<String> removeApplication(@PathVariable long id) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || !authentication.isAuthenticated()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Nincs bejelentkezett felhasználó.");
        }

        Object principal = authentication.getPrincipal();
        if (!(principal instanceof UserDetails)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Hozzáférés megtagadva.");
        }

        UserDetails userDetails = (UserDetails) principal;
        String userId = userDetails.getUsername();

        try {
            examService.removeApplication(id, userId);
            return ResponseEntity.ok("Sikeresen visszavontad a jelentkezést.");
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

}
