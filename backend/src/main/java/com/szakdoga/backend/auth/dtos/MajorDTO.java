package com.szakdoga.backend.auth.dtos;


public class MajorDTO {
    private int id;
    private String name;

    // Constructor, getters, and setters
    public MajorDTO(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
