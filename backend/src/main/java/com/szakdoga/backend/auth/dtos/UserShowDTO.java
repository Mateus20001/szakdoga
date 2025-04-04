package com.szakdoga.backend.auth.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserShowDTO {
    private String identifier;
    private String firstName;
    private String lastName;
    private String name;
    private List<String> emails;
    private List<String> phoneNumbers;
}
