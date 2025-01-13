package com.szakdoga.backend.auth.dtos;

import java.util.List;

public class UserNameAndRolesDTO {
    private String identifier;
    private List<String> roles;

    public UserNameAndRolesDTO(String identifier, List<String> roles) {
        this.identifier = identifier;
        this.roles = roles;
    }

    public String getIdentifier() {
        return identifier;
    }

    public void setIdentifier(String identifier) {
        this.identifier = identifier;
    }

    public List<String> getRoles() {
        return roles;
    }

    public void setRoles(List<String> roles) {
        this.roles = roles;
    }
}
