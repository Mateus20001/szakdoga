package com.szakdoga.backend.courses.models;

import com.szakdoga.backend.auth.model.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.List;

@AllArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "course_applications")
@NoArgsConstructor
public class CourseApplicationEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "course_date_id")
    private CourseDateEntity courseDateEntity;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "courseApplicationEntity", cascade = CascadeType.ALL)
    private List<Grade> grades;

    @CreationTimestamp
    @Column(name = "creation_date", nullable = false)
    private LocalDateTime creationDate;


}
