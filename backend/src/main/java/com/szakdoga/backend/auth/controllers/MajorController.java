package com.szakdoga.backend.auth.controllers;

import com.szakdoga.backend.auth.dtos.MajorDTO;
import com.szakdoga.backend.auth.dtos.MajorWithFacultyDTO;
import com.szakdoga.backend.auth.model.FacultyEntity;
import com.szakdoga.backend.auth.model.MajorDetails;
import com.szakdoga.backend.auth.model.User;
import com.szakdoga.backend.auth.repositories.FacultyRepository;
import com.szakdoga.backend.auth.repositories.MajorDetailsRepository;
import com.szakdoga.backend.auth.services.JwtService;
import com.szakdoga.backend.auth.services.MajorDetailsService;
import com.szakdoga.backend.auth.services.UserService;
import org.hibernate.Hibernate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/majors")
@CrossOrigin(origins = "http://localhost:4200")
public class MajorController {

    MajorDetailsRepository majorDetailsRepository;
    FacultyRepository facultyRepository;
    @Autowired
    public MajorController(MajorDetailsRepository majorDetailsRepository, FacultyRepository facultyRepository) {
        this.majorDetailsRepository = majorDetailsRepository;
        this.facultyRepository = facultyRepository;
    }

    @GetMapping
    public ResponseEntity<List<MajorDTO>> getMajorNames() {
        List<MajorDetails> majorDetailsList = majorDetailsRepository.findAll();

        // Map MajorDetails to MajorDTO, which contains id and name
        List<MajorDTO> majorDTOs = majorDetailsList.stream()
                .map(major -> new MajorDTO(major.getId(), major.getName()))  // Mapping each MajorDetails to MajorDTO
                .collect(Collectors.toList());

        // Return the list of MajorDTOs
        return ResponseEntity.ok(majorDTOs);
    }

    @GetMapping("/descriptions")
    public ResponseEntity<List<MajorWithFacultyDTO>> getMajorNameAndDescriptions() {
        List<MajorDetails> majorDetailsList = majorDetailsRepository.findAll();

        List<MajorWithFacultyDTO> majorDTOs = majorDetailsList.stream()
                .map(major -> {
                    FacultyEntity faculty = major.getFaculty();

                    if (faculty != null) {
                        Hibernate.initialize(faculty);
                    }

                    String facultyName = faculty != null ? faculty.getName() : "Unknown";
                    String description = major.getDescription() != null ? major.getDescription() : "No description available";

                    return new MajorWithFacultyDTO(major.getName(), description, facultyName);
                })
                .collect(Collectors.toList());

        return ResponseEntity.ok(majorDTOs);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/add")
    public ResponseEntity<?> addMajorDetails(@RequestBody MajorDetailsRequest request) {

        Optional<FacultyEntity> facultyOptional = facultyRepository.findById(request.getFacultyId());


        if (facultyOptional.isEmpty()) {
            return ResponseEntity.badRequest().body("Faculty with ID " + request.getFacultyId() + " not found.");
        }
        FacultyEntity faculty = facultyOptional.get();
        MajorDetails majorDetails = new MajorDetails(request.getName(), request.getDescription());
        majorDetails.setFaculty(faculty);

        try {
            MajorDetails savedMajorDetails = majorDetailsRepository.save(majorDetails);
            return ResponseEntity.ok(savedMajorDetails);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("An error occurred while saving major details. Please try again later.");
        }
    }

    public static class MajorDetailsRequest {
        private String name;
        private String description;
        private long facultyId;

        // Getters and Setters
        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        public long getFacultyId() {
            return facultyId;
        }

        public void setFacultyId(long facultyId) {
            this.facultyId = facultyId;
        }
    }
}
