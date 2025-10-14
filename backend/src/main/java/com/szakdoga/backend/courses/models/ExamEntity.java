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

    @Enumerated(EnumType.STRING)
    @Column(name = "location")
    private LocationEnum location;

    @Column(name = "longevity", nullable = false)
    private int longevity;

    @Enumerated(EnumType.STRING)
    @Column(name = "type")
    private ExamType type;

    @ManyToOne
    @JoinColumn(name = "course_date_id")
    private CourseDateEntity courseDateEntity;

    @OneToMany(mappedBy = "examEntity", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<ExamApplicationEntity> examApplicationEntities;
}
