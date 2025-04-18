package com.szakdoga.backend.courses.controllers;

import com.szakdoga.backend.courses.dtos.CourseDateIdRequest;
import com.szakdoga.backend.courses.dtos.CourseDateRequest;
import com.szakdoga.backend.courses.dtos.CourseDateResponse;
import com.szakdoga.backend.courses.dtos.EditCourseDateRequest;
import com.szakdoga.backend.courses.models.CourseDateEntity;
import com.szakdoga.backend.courses.models.LocationEnum;
import com.szakdoga.backend.courses.services.CourseDateService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/course-dates")
@RequiredArgsConstructor
public class CourseDateController {
    private static final Logger log = LoggerFactory.getLogger(CourseDateController.class);

    private final CourseDateService courseDateService;

    @PostMapping
    public ResponseEntity<CourseDateRequest> addCourseDate(
            @RequestBody CourseDateRequest request) {
        log.info("addCourseDate: {}", request);
        CourseDateEntity courseDateEntity = new CourseDateEntity();
        courseDateEntity.setName(request.getName());
        courseDateEntity.setDayOfWeek(request.getDayOfWeek());
        courseDateEntity.setStartTime(request.getStartTime());
        courseDateEntity.setEndTime(request.getEndTime());
        try {
            LocationEnum locationEnum = LocationEnum.fromString(request.getLocation());
            courseDateEntity.setLocation(locationEnum);
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Invalid location: " + request.getLocation());
        }
        courseDateEntity.setMaxLimit(request.getMaxLimit());
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
        List<CourseDateResponse> courseDates = courseDateService.getCourseDatesAsResponseObjects(courseId);

        if (courseDates.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return ResponseEntity.ok(courseDates);
    }
    @GetMapping("/{courseId}/{semester}-{enrollment}")
    public ResponseEntity<List<CourseDateResponse>> getCourseDatesByCourseIdSemesterEnrollment(@PathVariable Long courseId, @PathVariable String semester, @PathVariable String enrollment) {
        List<CourseDateResponse> courseDates = courseDateService.getCourseDatesAsResponseObjectsSemesterEnrollment(courseId, semester, enrollment);

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
