package com.szakdoga.backend.courses.dtos;


import com.szakdoga.backend.auth.dtos.UserListingDTO;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TeacherStudentGradingDTO {
    private String courseDetailName;
    private Long courseDateId;
    private String courseDateName;
    private List<UserListingDTO> students;
    private List<GradeDTO> grades;
}
