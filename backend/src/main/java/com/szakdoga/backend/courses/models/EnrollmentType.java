package com.szakdoga.backend.courses.models;

import lombok.Getter;

@Getter
public enum EnrollmentType {
    MANDATORY("Mandatory"),
    MANDATORY_OPTIONAL("Mandatory Optional"),
    OPTIONAL("Optional");

    private final String name;

    EnrollmentType(String name) {
        this.name = name;
    }

}
