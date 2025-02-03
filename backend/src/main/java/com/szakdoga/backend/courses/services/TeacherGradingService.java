package com.szakdoga.backend.courses.services;

import com.szakdoga.backend.auth.dtos.UserListingDTO;
import com.szakdoga.backend.courses.dtos.GradeDTO;
import com.szakdoga.backend.courses.dtos.TeacherStudentGradingDTO;
import com.szakdoga.backend.courses.repositories.CourseDateRepository;
import com.szakdoga.backend.courses.repositories.GradeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class TeacherGradingService {
    @Autowired
    private CourseDateRepository courseDateRepository;

    public List<TeacherStudentGradingDTO> getTeacherStudentGrades(String teacherId) {
        return courseDateRepository.findAll().stream()
                .filter(courseDate -> courseDate.getTeachers().stream().anyMatch(teacher -> teacher.getId().equals(teacherId)))
                .map(courseDate ->
                        new TeacherStudentGradingDTO(
                                courseDate.getCourseDetailEntity().getName(),
                                courseDate.getId(),
                                courseDate.getName(),
                                courseDate.getApplications().stream()
                                        .map(app -> new UserListingDTO(app.getUser().getId(), app.getUser().getFirstName(), app.getUser().getLastName(), app.getUser().getUsername()))
                                        .collect(Collectors.toList()),
                                courseDate.getApplications().stream()
                                        .flatMap(app -> app.getGrades().stream())
                                        .map(grade -> new GradeDTO(grade.getId(), grade.getGradeValue(), grade.getGradedBy().getId(), grade.getCreationDate()))
                                        .collect(Collectors.toList())
                        )
                ).collect(Collectors.toList());
    }
}
