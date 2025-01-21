package com.szakdoga.backend.courses.models;

public enum RequirementType {
    COLLOQUIUM("Colloquium"),
    PRACTICE("Practice"),
    PE("PE");

    private final String name;

    RequirementType(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}