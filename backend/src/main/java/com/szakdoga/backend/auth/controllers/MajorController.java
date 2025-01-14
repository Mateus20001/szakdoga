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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/majors")
@CrossOrigin(origins = "http://localhost:4200")
public class MajorController {

    MajorDetailsRepository majorDetailsRepository;
    FacultyRepository facultyRepository;

    @Autowired
    public MajorController(MajorDetailsRepository majorDetailsRepository) {
        this.majorDetailsRepository = majorDetailsRepository;
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

        // Map each MajorDetails to a MajorWithFacultyDTO
        List<MajorWithFacultyDTO> majorDTOs = majorDetailsList.stream()
                .map(major -> {
                    String facultyName = major.getFaculty() != null ? major.getFaculty().getName() : "Unknown";
                    return new MajorWithFacultyDTO(major.getName(), major.getDescription(), facultyName);
                })
                .collect(Collectors.toList());

        return ResponseEntity.ok(majorDTOs);
    }

}
