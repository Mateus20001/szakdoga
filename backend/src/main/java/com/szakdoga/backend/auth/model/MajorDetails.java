package com.szakdoga.backend.auth.model;

import jakarta.persistence.*;

@Entity
@Table(name = "major_details")
public class MajorDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private int id;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "description")
    private String description;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "faculty_id", nullable = false)
    private FacultyEntity faculty;

    public MajorDetails(String name, String description) {
        this.name = name;
        this.description = description;
    }

    public MajorDetails() {

    }

    public void setId(int id) {
        this.id = id;
    }


    public int getId() {
        return id;
    }
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
    public FacultyEntity getFaculty() {
        return faculty;
    }

    public void setFaculty(FacultyEntity faculty) {
        this.faculty = faculty;
    }
}
