package com.szakdoga.backend.courses.services;

import com.szakdoga.backend.auth.dtos.UserListingDTO;
import com.szakdoga.backend.auth.model.User;
import com.szakdoga.backend.auth.repositories.UserRepository;
import com.szakdoga.backend.courses.controllers.CourseDetailController;
import com.szakdoga.backend.courses.dtos.GradeDTO;
import com.szakdoga.backend.courses.dtos.GradingDTO;
import com.szakdoga.backend.courses.dtos.TeacherStudentGradingDTO;
import com.szakdoga.backend.courses.models.CourseApplicationEntity;
import com.szakdoga.backend.courses.models.Grade;
import com.szakdoga.backend.courses.repositories.CourseApplicationRepository;
import com.szakdoga.backend.courses.repositories.CourseDateRepository;
import com.szakdoga.backend.courses.repositories.GradeRepository;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class TeacherGradingService {
    private static final Logger log = LoggerFactory.getLogger(TeacherGradingService.class);

    @Autowired
    private CourseDateRepository courseDateRepository;
    @Autowired
    private GradeRepository gradeRepository;
    @Autowired
    private CourseApplicationRepository courseApplicationRepository;
    @Autowired
    private UserRepository userRepository;
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
    @Transactional
    public void saveGrades(List<GradingDTO> grades, String userId) {
        List<Grade> gradeEntities = grades.stream().map(gradeDTO -> {
            CourseApplicationEntity courseApplicationEntity = courseApplicationRepository.findByUserIdAndCourseDateEntityId(gradeDTO.getIdentifier(), gradeDTO.getCourseDateId());
            log.info("Saved gradings for courseApl {}", courseApplicationEntity);
            User user = userRepository.findById(userId).orElseThrow();
            log.info("Saved gradings for user {}", user);
            log.info("Saved gradings for coursevalue {}", gradeDTO.getGradeValue());
            return new Grade(courseApplicationEntity, gradeDTO.getGradeValue(), user);
        }).collect(Collectors.toList());
        gradeRepository.saveAll(gradeEntities);
    }
}
