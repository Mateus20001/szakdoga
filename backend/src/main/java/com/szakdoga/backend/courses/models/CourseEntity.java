package com.szakdoga.backend.courses.models;

import com.szakdoga.backend.auth.model.User;
import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "course")
public class CourseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;


    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne
    @JoinColumn(name = "course_detail_id", nullable = false)
    private CourseDetailEntity courseDetailEntity;

    @Column(name = "apply_time")
    private LocalDateTime applicationDateTime;


    public void setId(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }
}
