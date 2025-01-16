package com.szakdoga.backend.courses.models;

import com.szakdoga.backend.auth.model.User;
import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "course_details")
public class CourseDetailEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "description")
    private String description;

    @Column(name = "credits")
    private int credits;

    @Column(name = "created_at")
    @CreationTimestamp
    private LocalDateTime createdAt;

    @ManyToMany
    @JoinTable(
            name = "course_teacher", // Kapcsolótábla neve
            joinColumns = @JoinColumn(name = "course_detail_id"), // Ez az oszlop a CourseDetailEntity-re hivatkozik
            inverseJoinColumns = @JoinColumn(name = "teacher_id") // Ez az oszlop a User-re hivatkozik
    )
    private List<User> teachers;


    public void setId(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }
}
