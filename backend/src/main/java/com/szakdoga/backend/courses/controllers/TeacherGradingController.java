package com.szakdoga.backend.courses.controllers;

import com.szakdoga.backend.courses.dtos.GradingDTO;
import com.szakdoga.backend.courses.dtos.TeacherStudentGradingDTO;
import com.szakdoga.backend.courses.services.TeacherGradingService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/grading")
public class TeacherGradingController {
    private static final Logger log = LoggerFactory.getLogger(TeacherGradingController.class);

    @Autowired
    private TeacherGradingService gradingService;

    @GetMapping("/grades")
    public ResponseEntity<List<TeacherStudentGradingDTO>> getTeacherStudentGrades() {
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
        return ResponseEntity.ok(gradingService.getTeacherStudentGrades(userId));
    }

    @PostMapping("/save")
    public ResponseEntity<Map<String, String>> saveGrades(@RequestBody List<GradingDTO> grades) {
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
            gradingService.saveGrades(grades, userId);
            Map<String, String> response = new HashMap<>();
            response.put("message", "Grades saved successfully");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Collections.singletonMap("error", "Failed to save grades"));
        }
    }

}