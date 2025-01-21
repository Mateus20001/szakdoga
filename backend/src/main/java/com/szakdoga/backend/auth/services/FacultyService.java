package com.szakdoga.backend.auth.services;

import com.szakdoga.backend.auth.model.FacultyEntity;
import com.szakdoga.backend.auth.repositories.FacultyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class FacultyService {

    @Autowired
    private FacultyRepository facultyRepository;

    // Method to add a new faculty
    public FacultyEntity addFaculty(FacultyEntity facultyEntity) {
        return facultyRepository.save(facultyEntity);
    }

    // Method to retrieve all faculties
    public List<FacultyEntity> getAllFaculties() {
        return facultyRepository.findAll();
    }

    public FacultyEntity getFacultyById(long facultyId) {
        Optional<FacultyEntity> facultyEntity = facultyRepository.findById(facultyId);
        return facultyEntity.orElse(null); // Returns null if not found
    }
}