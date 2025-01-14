package com.szakdoga.backend.auth.dtos;

public class MajorWithFacultyDTO {
    private String name;
    private String description;
    private String facultyName;

    // Constructor
    public MajorWithFacultyDTO(String name, String description, String facultyName) {
        this.name = name;
        this.description = description;
        this.facultyName = facultyName;
    }

    // Getters and setters
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

    public String getFacultyName() {
        return facultyName;
    }

    public void setFacultyName(String facultyName) {
        this.facultyName = facultyName;
    }
}

