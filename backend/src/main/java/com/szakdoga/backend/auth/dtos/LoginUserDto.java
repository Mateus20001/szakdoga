package com.szakdoga.backend.auth.dtos;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class LoginUserDto {
    private String id;

    private String password;

}
