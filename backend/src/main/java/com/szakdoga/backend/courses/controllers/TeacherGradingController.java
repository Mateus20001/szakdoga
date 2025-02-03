package com.szakdoga.backend.courses.controllers;

import com.szakdoga.backend.courses.dtos.TeacherStudentGradingDTO;
import com.szakdoga.backend.courses.services.TeacherGradingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/grading")
public class TeacherGradingController {

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
}