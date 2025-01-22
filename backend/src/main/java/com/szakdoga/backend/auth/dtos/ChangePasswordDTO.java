package com.szakdoga.backend.auth.dtos;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class ChangePasswordDTO {
    // Getters and Setters
    private String newPassword;
    private String confirmPassword;

}

