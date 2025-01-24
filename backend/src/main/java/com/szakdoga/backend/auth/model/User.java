package com.szakdoga.backend.auth.model;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.szakdoga.backend.courses.models.CourseApplicationEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;
import java.util.Random;

@Entity
@Table(name = "users")
public class User implements UserDetails {

    @Setter
    @Id
    @Column(name = "id", length = 6, unique = true, nullable = false)
    private String id;

    @Setter
    @Getter
    @Column(name = "name")
    private String name;

    @Setter
    @Getter
    @Column(name = "firstLogIn")
    private boolean firstLogIn;

    @Setter
    @Getter
    @JsonIgnore
    @OneToMany(mappedBy = "user", cascade = CascadeType.PERSIST, orphanRemoval = true, fetch = FetchType.EAGER)
    private List<RoleEntity> roles;

    @Getter
    @Setter
    @Column(name = "password", length = 374, nullable = false)
    private String password;

    @Setter
    @Getter
    @Column(name = "created_at", updatable = false, nullable = false)
    @CreationTimestamp
    private LocalDateTime createdAt;

    @Getter
    @Column(name = "enabled", nullable = false)
    private boolean enabled = true;

    @Setter
    @Getter
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @Setter
    @Getter
    @Column(name = "first_name", nullable = false)
    private String firstName;

    @Setter
    @Getter
    @Column(name = "last_name", nullable = false)
    private String lastName;

    @Setter
    @Getter
    @JsonIgnore
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<EmailEntity> emails;

    @Setter
    @Getter
    @JsonIgnore
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    private List<MajorEntity> majors;

    @Getter
    @Setter
    @JsonIgnore
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<PhoneEntity> phone_numbers;

    @JsonIgnore
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<CourseApplicationEntity> appliedCourses;


    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return roles.stream()
                .map(role -> new SimpleGrantedAuthority(role.getRoleName())) // Convert each RoleEntity to GrantedAuthority
                .toList();
    }

    @PrePersist
    private void generateId() {
        if (this.id == null) {
            this.id = generateRandomId();
        }
    }

    private String generateRandomId() {
        StringBuilder sb = new StringBuilder(6);
        String characters = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
        Random random = new Random();
        for (int i = 0; i < 6; i++) {
            sb.append(characters.charAt(random.nextInt(characters.length())));
        }
        return sb.toString();
    }
    // Getters and Setters

    public String getId() {
        return id;
    }


    @Override
    public String getUsername() {
        return this.id;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

}
