package com.szakdoga.backend.auth.dtos;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

public class RegisterUserDto {

    @Getter
    @Setter
    private String firstName;
    @Getter
    @Setter
    private String lastName;
    private List<String> roles; // Ez egy lista a szerepkörökről
    private List<String> emails; // Lista az email címekről
    private List<String> majors; // Lista a szakok neveiről
    public RegisterUserDto(String firstName, String lastName,
                       List<String> roleNames, List<String> emailAddresses, List<String> majorNames) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.roles = roleNames;
        this.emails = emailAddresses;
        this.majors = majorNames;
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

}