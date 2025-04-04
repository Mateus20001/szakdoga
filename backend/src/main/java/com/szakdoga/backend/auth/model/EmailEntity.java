package com.szakdoga.backend.auth.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Setter
@Getter
@Entity
@Table(name = "user_emails")
public class EmailEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private int id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(name = "email", length = 512, nullable = false)
    private String email;

    @Column(name = "public", nullable = false)
    private boolean _public;

    public EmailEntity(User user, String email, Boolean _public) {
        this.user = user;
        this.email = email;
        this._public = _public;
    }
    public EmailEntity(User user, String email) {
        this.user = user;
        this.email = email;
        this._public = true;
    }

    public EmailEntity() {

    }

    public boolean isPublic() {
        return _public;
    }
}
