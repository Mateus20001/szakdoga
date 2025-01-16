package com.szakdoga.backend.auth.model;

import jakarta.persistence.*;

@Entity
@Table(name = "phone_numbers")
public class PhoneEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(name = "phone_number", length = 512, nullable = false)
    private String phone_number;


    @Column(name = "public", nullable = false)
    private boolean _public;

    public PhoneEntity(User user, String phone_number, boolean _public) {
        this.user = user;
        this.phone_number = phone_number;
        this._public = _public;
    }
    public PhoneEntity(User user, String phone_number) {
        this.user = user;
        this.phone_number = phone_number;
        this._public = true;
    }

    public PhoneEntity() {

    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getPhone_number() {
        return phone_number;
    }

    public void setPhone_number(String phone_number) {
        this.phone_number = phone_number;
    }

    public boolean is_public() {
        return _public;
    }

    public void set_public(boolean _public) {
        this._public = _public;
    }
}
