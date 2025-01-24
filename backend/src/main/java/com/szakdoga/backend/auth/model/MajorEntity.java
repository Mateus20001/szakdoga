package com.szakdoga.backend.auth.model;

import com.szakdoga.backend.auth.repositories.MajorDetailsRepository;
import jakarta.persistence.*;

@Entity
@Table(name = "user_majors")
public class MajorEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private int id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "major_id", nullable = false)
    private MajorDetails major;

    public MajorEntity(User user, int majorId, MajorDetailsRepository majorDetailsRepository) {
        this.user = user;
        this.major = majorDetailsRepository.findById(majorId)
                .orElseThrow(() -> new IllegalArgumentException("Major not found with id: " + majorId));
    }

    public MajorEntity() {

    }

    public long getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public MajorDetails getMajor() {
        return major;
    }

    public void setMajor(MajorDetails major) {
        this.major = major;
    }
}
