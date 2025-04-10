package com.szakdoga.backend.courses.models;

import com.szakdoga.backend.auth.model.User;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

@Setter
@Getter
@Entity
@Table(name = "course_dates")
public class CourseDateEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;
    
    @Column(name = "name", nullable = false)
    private String name;

    @Enumerated(EnumType.STRING)
    @Column(name = "location")
    private LocationEnum location;

    @Column(name = "max_limit")
    private int maxLimit;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "course_detail_id")
    @Fetch(FetchMode.JOIN)
    private CourseDetailEntity courseDetailEntity;

    @ManyToMany
    @JoinTable(
            name = "course_date_teachers",
            joinColumns = @JoinColumn(name = "course_date_id"),
            inverseJoinColumns = @JoinColumn(name = "user_id")
    )
    private List<User> teachers = new ArrayList<>();

    @Enumerated(EnumType.STRING)
    @Column(name = "day_of_week", nullable = false)
    private DayOfWeekEnum dayOfWeek;

    // Store the start time
    @Column(name = "start_time", nullable = false)
    private LocalTime startTime;

    // Store the end time
    @Column(name = "end_time", nullable = false)
    private LocalTime endTime;

    @OneToMany(mappedBy = "courseDateEntity", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<CourseApplicationEntity> applications;

    @Column(name = "semester")
    private String semester;
}
