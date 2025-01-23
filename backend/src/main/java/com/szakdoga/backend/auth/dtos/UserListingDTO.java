package com.szakdoga.backend.auth.dtos;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class UserListingDTO {
    private String identifier;
    private String firstName;
    private String lastName;
    private String name;
    public UserListingDTO(String identifier, String firstName, String lastName, String name) {
        this.identifier = identifier;
        this.firstName = firstName;
        this.lastName = lastName;
        this.name = name;
    }
}
