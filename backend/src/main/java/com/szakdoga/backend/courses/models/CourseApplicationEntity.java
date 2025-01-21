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
@Table(name = "course_applications")
public class CourseApplicationEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne
    @JoinColumn(name = "course_date_id", nullable = false)
    private CourseDateEntity courseDateEntity;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "courseApplicationEntity", cascade = CascadeType.ALL)
    private List<Grade> grades;

    @Column(name = "creation_date", nullable = false)
    private LocalDateTime creationDate;

    public CourseApplicationEntity() {
        this.creationDate = LocalDateTime.now();
    }
}
