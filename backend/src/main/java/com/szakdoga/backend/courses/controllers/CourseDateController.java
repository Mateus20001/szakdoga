package com.szakdoga.backend.courses.controllers;

import com.szakdoga.backend.courses.dtos.CourseDateRequest;
import com.szakdoga.backend.courses.dtos.CourseDateResponse;
import com.szakdoga.backend.courses.dtos.EditCourseDateRequest;
import com.szakdoga.backend.courses.models.CourseDateEntity;
import com.szakdoga.backend.courses.services.CourseDateService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/course-dates")
@RequiredArgsConstructor
public class CourseDateController {

    private final CourseDateService courseDateService;

    @PostMapping
    public ResponseEntity<CourseDateRequest> addCourseDate(
            @RequestBody CourseDateRequest request) {
        CourseDateEntity courseDateEntity = new CourseDateEntity();
        courseDateEntity.setName(request.getName());
        courseDateEntity.setDayOfWeek(request.getDayOfWeek());
        courseDateEntity.setStartTime(request.getStartTime());
        courseDateEntity.setEndTime(request.getEndTime());

        CourseDateEntity savedCourseDate = courseDateService.addCourseDate(
                courseDateEntity,
                request.getCourseId(),
                request.getTeacherIds()
        );
        if (savedCourseDate == null) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        return ResponseEntity.ok(request);
    }

    @GetMapping("/{courseId}/all")
    public ResponseEntity<List<CourseDateResponse>> getCourseDatesByCourseId(@PathVariable Long courseId) {
        List<CourseDateResponse> courseDates = courseDateService.getCourseDatesAsRequestObjects(courseId);

        if (courseDates.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return ResponseEntity.ok(courseDates);
    }
    @PutMapping
    public ResponseEntity<EditCourseDateRequest> updateCourseDate(
            @RequestBody EditCourseDateRequest request) {

        CourseDateEntity updatedCourseDate = courseDateService.updateCourseDate(request);

        if (updatedCourseDate == null) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(request);
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCourseDate(@PathVariable Long id) {
        boolean deleted = courseDateService.deleteCourseDate(id);
        if (deleted) {
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
