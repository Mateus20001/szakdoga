package com.szakdoga.backend.courses.services;

import com.szakdoga.backend.auth.model.User;
import com.szakdoga.backend.auth.repositories.UserRepository;
import com.szakdoga.backend.courses.dtos.AddExamRequest;
import com.szakdoga.backend.courses.dtos.ExamResponse;
import com.szakdoga.backend.courses.models.CourseApplicationEntity;
import com.szakdoga.backend.courses.models.ExamApplicationEntity;
import com.szakdoga.backend.courses.models.ExamEntity;
import com.szakdoga.backend.courses.repositories.CourseDateRepository;
import com.szakdoga.backend.courses.repositories.ExamApplicationRepository;
import com.szakdoga.backend.courses.repositories.ExamRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ExamService {
    @Autowired
    private ExamRepository examRepository;
    @Autowired
    private ExamApplicationRepository examApplicationRepository;

    @Autowired
    private CourseDateRepository courseDateRepository;

    @Autowired
    private UserRepository userRepository;

    @Transactional
    public void applyToExam(long id, String user_id) {
        User user = userRepository.findById(user_id).orElseThrow(() -> new RuntimeException("User not found"));
        ExamEntity examEntity = examRepository.findById(id).orElseThrow(() -> new RuntimeException("Exam not found"));
        ExamApplicationEntity alreadyExamApplicationEntity = examApplicationRepository.findByUserAndExamEntity(user, examEntity);
        if (alreadyExamApplicationEntity != null) {
            alreadyExamApplicationEntity.setExamEntity(null);
            alreadyExamApplicationEntity.setUser(null);
            examApplicationRepository.save(alreadyExamApplicationEntity);
            examApplicationRepository.delete(alreadyExamApplicationEntity);
            examApplicationRepository.deleteOrphanedExamApplicationEntities();
        }
        ExamApplicationEntity examApplicationEntity = new ExamApplicationEntity();
        examApplicationEntity.setUser(user);
        examApplicationEntity.setExamEntity(examEntity);
        examApplicationRepository.save(examApplicationEntity);
    }
    @Transactional
    public void removeApplication(long id, String user_id) {
        User user = userRepository.findById(user_id).orElseThrow(() -> new RuntimeException("User not found"));
        ExamEntity examEntity = examRepository.findById(id).orElseThrow(() -> new RuntimeException("Exam not found"));
        ExamApplicationEntity alreadyExamApplicationEntity = examApplicationRepository.findByUserAndExamEntity(user, examEntity);
        alreadyExamApplicationEntity.setExamEntity(null);
        alreadyExamApplicationEntity.setUser(null);
        examApplicationRepository.save(alreadyExamApplicationEntity);
        examApplicationRepository.delete(alreadyExamApplicationEntity);
        examApplicationRepository.deleteOrphanedExamApplicationEntities();
    }

    @Transactional
    public void addExam(AddExamRequest request) {
        var courseDateEntity = courseDateRepository.findById(request.getCourseDateId())
                .orElseThrow(() -> new IllegalArgumentException("CourseDate not found"));

        ExamEntity exam = new ExamEntity();
        exam.setExamDate(request.getExamDate());
        exam.setLocation(request.getLocation());
        exam.setLongevity(request.getLongevity());
        exam.setType(request.getType());
        exam.setCourseDateEntity(courseDateEntity);

        examRepository.save(exam);
    }

    @Transactional
    public void removeExamDate(long id) {
        ExamEntity examEntity = examRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Exam date not found"));

        examEntity.setCourseDateEntity(null);

        examRepository.save(examEntity);
        examRepository.delete(examEntity);
        examRepository.deleteOrphanedExamApplicationEntities();
    }
    @Transactional
    public List<ExamResponse> getAllExams(String userId) {
        return examRepository.findAll().stream()
                .filter(exam -> exam.getCourseDateEntity() != null &&
                        exam.getCourseDateEntity().getApplications().stream()
                                .anyMatch(app -> app.getUser() != null && app.getUser().getId().equals(userId)))
                .map(exam -> {
                    ExamResponse response = new ExamResponse();
                    response.setId(exam.getId());
                    response.setExamDate(exam.getExamDate());
                    response.setLocation(exam.getLocation());
                    response.setLongevity(exam.getLongevity());
                    response.setType(exam.getType());

                    // Pick the application relevant to the user
                    Long userCourseAppId = exam.getCourseDateEntity().getApplications().stream()
                            .filter(app -> app.getUser() != null && app.getUser().getId().equals(userId))
                            .findFirst()
                            .map(CourseApplicationEntity::getId)
                            .orElse(null);

                    response.setCourseApplicationId(userCourseAppId);
                    return response;
                })
                .collect(Collectors.toList());
    }

}
