package com.szakdoga.backend.auth.dtos;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class ContactDTO {
    private int id;
    private String email;
    private boolean _public;

    public ContactDTO(int id, String email, boolean _public) {
        this.id = id;
        this.email = email;
        this._public = _public;
    }
}
