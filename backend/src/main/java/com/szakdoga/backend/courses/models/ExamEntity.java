package com.szakdoga.backend.courses.models;

import com.szakdoga.backend.auth.model.User;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "exams")
public class ExamEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "exam_date", nullable = false)
    private LocalDateTime examDate;

    @Column(name = "grade", nullable = false)
    private int grade;

    @Enumerated(EnumType.STRING)
    @Column(name = "location")
    private LocationEnum location;

    @ManyToOne
    @JoinColumn(name = "course_application_id", nullable = false)
    private CourseApplicationEntity courseApplicationEntity;

}
