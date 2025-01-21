package com.szakdoga.backend.courses.models;

import com.szakdoga.backend.auth.model.MajorEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "course_enrollments")
public class EnrollmentTypeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "major_id", nullable = false)
    private MajorEntity major;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "course_detail_id", nullable = false)
    private CourseDetailEntity courseDetail;

    // Enrollment type can be mandatory, mandatory optional, or optional
    @Enumerated(EnumType.STRING)
    @Column(name = "enrollment_type")
    private EnrollmentType enrollmentType;

}