package com.szakdoga.backend.courses.controllers;

import com.szakdoga.backend.courses.dtos.AddExamRequest;
import com.szakdoga.backend.courses.dtos.ExamResponse;
import com.szakdoga.backend.courses.models.ExamApplicationEntity;
import com.szakdoga.backend.courses.services.ExamService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/exams")
public class ExamController {
    @Autowired
    private ExamService examService;


    @PostMapping("/apply")
    public ResponseEntity<ExamApplicationEntity> addCourse(@RequestBody long id) {
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
        examService.applyToExam(id, userId);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/add")
    public ResponseEntity<Void> addExam(@RequestBody AddExamRequest request) {
        examService.addExam(request);
        return ResponseEntity.ok().build();
    }
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
        List<ExamResponse> exams = examService.getAllExams(userId);
        return ResponseEntity.ok(exams);
    }
}
