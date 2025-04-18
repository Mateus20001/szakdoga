package com.szakdoga.backend.courses.models;

public enum ExamType {
    ONLINE("Online"),
    COOSPACE("Coospace"),
    PAPER("Paper");

    private final String name;

    ExamType(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
