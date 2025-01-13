package com.szakdoga.backend.auth.dtos;

import java.util.List;

public class RegisterUserDto {

    private String firstName;
    private String lastName;
    private String password;
    private String name;
    private List<String> roleNames; // Ez egy lista a szerepkörökről
    private List<String> emailAddresses; // Lista az email címekről
    private List<String> majorNames; // Lista a szakok neveiről
    private List<String> facultyNames; // Lista a karok neveiről
    public RegisterUserDto(String firstName, String lastName, String password, String antecedent,
                       List<String> roleNames, List<String> emailAddresses, List<String> majorNames,
                       List<String> facultyNames) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.password = password;
        this.name = antecedent;
        this.roleNames = roleNames;
        this.emailAddresses = emailAddresses;
        this.majorNames = majorNames;
        this.facultyNames = facultyNames;
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

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getName() {
        return name;
    }

    public void setName(String antecedent) {
        this.name = antecedent;
    }

    public List<String> getRoleNames() {
        return roleNames;
    }

    public void setRoleNames(List<String> roleNames) {
        this.roleNames = roleNames;
    }

    public List<String> getEmailAddresses() {
        return emailAddresses;
    }

    public void setEmailAddresses(List<String> emailAddresses) {
        this.emailAddresses = emailAddresses;
    }

    public List<String> getMajorNames() {
        return majorNames;
    }

    public void setMajorNames(List<String> majorNames) {
        this.majorNames = majorNames;
    }

    public List<String> getFacultyNames() {
        return facultyNames;
    }

    public void setFacultyNames(List<String> facultyNames) {
        this.facultyNames = facultyNames;
    }
}