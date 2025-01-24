package com.szakdoga.backend.courses.dtos;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class CourseDetailListingDTO {
    private long id;
    private String name;
    private String description;
    private int credits;

    public CourseDetailListingDTO(long id, String name, String description, int credits) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.credits = credits;
    }

}
