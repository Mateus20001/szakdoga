package com.szakdoga.backend.auth.controllers;

import com.szakdoga.backend.auth.model.FacultyEntity;
import com.szakdoga.backend.auth.services.FacultyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/faculties")
@CrossOrigin(origins = "http://localhost:4200")
public class FacultyController {
    @Autowired
    private FacultyService facultyService;

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    public ResponseEntity<FacultyEntity> addFaculty(@RequestParam String name, @RequestParam String description, @RequestParam("image") MultipartFile imageFile) {
        try {
            byte[] imageBytes = imageFile.getBytes();
            FacultyEntity facultyEntity = new FacultyEntity(imageBytes);
            facultyEntity.setName(name);
            facultyEntity.setDescription(description);
            FacultyEntity addedFaculty = facultyService.addFaculty(facultyEntity);
            return new ResponseEntity<>(addedFaculty, HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping
    public ResponseEntity<List<FacultyEntity>> getAllFaculties() {
        List<FacultyEntity> faculties = facultyService.getAllFaculties();
        return new ResponseEntity<>(faculties, HttpStatus.OK);
    }
}
