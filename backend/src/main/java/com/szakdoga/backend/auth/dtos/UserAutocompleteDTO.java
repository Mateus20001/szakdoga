package com.szakdoga.backend.auth.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserAutocompleteDTO {
    private String identifier;
    private String firstName;
    private String lastName;
    private String name;
}
