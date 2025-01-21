package com.szakdoga.backend.courses.models;

import com.szakdoga.backend.auth.model.User;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Table(name = "grades")
public class Grade {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "course_application_id", nullable = false)
    private CourseApplicationEntity courseApplicationEntity;

    @Column(name = "grade_value", nullable = false)
    private int gradeValue;

    @ManyToOne
    @JoinColumn(name = "graded_by_user_id", nullable = false)
    private User gradedBy;

    @Column(name = "creation_date", nullable = false)
    private LocalDateTime creationDate;

    public Grade() {
        this.creationDate = LocalDateTime.now();
    }
}