package com.szakdoga.backend.courses.models;

import com.szakdoga.backend.auth.model.User;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Setter
@Getter
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

    @Enumerated(EnumType.STRING)
    @Column(name = "requirement_type")
    private RequirementType requirementType;

    @Column(name = "recommended_half_year")
    private int recommended_half_year;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "course_prerequisites",
            joinColumns = @JoinColumn(name = "course_id"),
            inverseJoinColumns = @JoinColumn(name = "prerequisite_id")
    )
    private List<CourseDetailEntity> requiredCourses;


    @OneToMany(mappedBy = "courseDetailEntity", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<CourseDateEntity> courseDates;

    @OneToMany(mappedBy = "courseDetail", fetch = FetchType.EAGER, orphanRemoval = true, cascade = CascadeType.ALL)
    private List<EnrollmentTypeEntity> enrollmentTypes;

    @OneToMany(mappedBy = "courseDetail", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    private List<CourseTeacherEntity> courseTeachers = new ArrayList<>();

    public CourseDetailEntity() { }
}
