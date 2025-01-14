package com.szakdoga.backend.auth.dtos;

import java.time.LocalDateTime;
import java.util.List;

public class UserDetailsDTO {

    private String id;
    private String firstName;
    private String lastName;
    private String name;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<String> getRoles() {
        return roles;
    }

    public void setRoles(List<String> roles) {
        this.roles = roles;
    }

    public List<String> getEmails() {
        return emails;
    }

    public void setEmails(List<String> emails) {
        this.emails = emails;
    }

    public List<String> getMajors() {
        return majors;
    }

    public void setMajors(List<String> majors) {
        this.majors = majors;
    }

    public List<String> getFaculties() {
        return faculties;
    }

    public void setFaculties(List<String> faculties) {
        this.faculties = faculties;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    private List<String> roles;
    private List<String> emails;
    private List<String> majors;
    private List<String> faculties;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    // Constructor
    public UserDetailsDTO(String id, String firstName, String lastName, String name,
                   List<String> roles, List<String> emails, List<String> majors, List<String> faculties, LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.name = name;
        this.roles = roles;
        this.emails = emails;
        this.majors = majors;
        this.faculties = faculties;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }
}
