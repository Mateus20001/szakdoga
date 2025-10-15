package com.szakdoga.backend.courses.services;

import com.szakdoga.backend.auth.model.User;
import com.szakdoga.backend.auth.repositories.UserRepository;
import com.szakdoga.backend.courses.dtos.AddExamRequest;
import com.szakdoga.backend.courses.dtos.AppliedExamResponse;
import com.szakdoga.backend.courses.dtos.ExamResponse;
import com.szakdoga.backend.courses.models.*;
import com.szakdoga.backend.courses.repositories.CourseDateRepository;
import com.szakdoga.backend.courses.repositories.ExamApplicationRepository;
import com.szakdoga.backend.courses.repositories.ExamRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
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
    public String applyToExam(long examId, String userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("Felhasználó nem található."));
        ExamEntity examEntity = examRepository.findById(examId)
                .orElseThrow(() -> new RuntimeException("Vizsga nem található."));

        ExamApplicationEntity existing = examApplicationRepository.findByUserAndExamEntity(user, examEntity);
        if (existing != null) {
            return "Már jelentkeztél erre a vizsgára.";
        }

        if (examEntity.getCourseDateEntity() == null || examEntity.getCourseDateEntity().getApplications() == null) {
            throw new RuntimeException("Csak olyan vizsgára jelentkezhetsz, amelynek kurzusára már jelentkeztél.");
        }

        boolean hasCourseDateApplication = examEntity.getCourseDateEntity().getApplications().stream()
                .anyMatch(app -> app.getUser() != null && userId.equals(app.getUser().getId()));

        if (!hasCourseDateApplication) {
            throw new RuntimeException("Csak olyan vizsgára jelentkezhetsz, amelynek kurzusára már jelentkeztél.");
        }

        boolean alreadyAppliedToCourseDate = examApplicationRepository.existsByUserAndExamEntity_CourseDateEntity(user, examEntity.getCourseDateEntity());
        if (alreadyAppliedToCourseDate) {
            return "Már jelentkeztél egy másik vizsgára ezen a kurzusidőponton.";
        }
        ExamApplicationEntity newApplication = new ExamApplicationEntity();
        newApplication.setUser(user);
        newApplication.setExamEntity(examEntity);
        examApplicationRepository.save(newApplication);

        return "Sikeresen jelentkeztél a vizsgára.";
    }

    @Transactional
    public void removeApplication(long id, String user_id) {
        User user = userRepository.findById(user_id)
                .orElseThrow(() -> new RuntimeException("Felhasználó nem található."));
        ExamEntity exam = examRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Vizsga nem található."));
        ExamApplicationEntity alreadyExamApplicationEntity = examApplicationRepository.findByUserAndExamEntity(user, exam);
        alreadyExamApplicationEntity.setExamEntity(null);
        alreadyExamApplicationEntity.setUser(null);
        examApplicationRepository.save(alreadyExamApplicationEntity);
        examApplicationRepository.delete(alreadyExamApplicationEntity);
        examApplicationRepository.deleteOrphanedExamApplicationEntities();
    }

    @Transactional
    public void addExam(String userId, AddExamRequest request) {
        var courseDateEntity = courseDateRepository.findById(request.getCourseDateId())
                .orElseThrow(() -> new IllegalArgumentException("CourseDate not found"));
        var teachers = courseDateEntity.getTeachers();
        boolean isTeacher = teachers.stream()
                .anyMatch(teacher -> teacher.getId().equals(userId));

        if (!isTeacher) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "You are not a teacher for this course");
        }
        ExamEntity exam = new ExamEntity();
        LocalDateTime date = LocalDateTime.parse(request.getExamDate());
        exam.setExamDate(date);
        try {
            LocationEnum locationEnum = LocationEnum.fromString(request.getLocation());
            exam.setLocation(locationEnum);
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Invalid location: " + request.getLocation());
        }
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
    public List<AppliedExamResponse> getAllAppliedExams(String userId) {
        // Find all applications by this user
        List<ExamApplicationEntity> userApplications = examApplicationRepository.findAllByUserId(userId);

        return userApplications.stream()
                .map(app -> {
                    ExamEntity exam = app.getExamEntity();

                    AppliedExamResponse response = new AppliedExamResponse();
                    response.setId(exam.getId());
                    response.setExamDate(exam.getExamDate().toString());
                    response.setLocation(exam.getLocation().toString());
                    response.setLongevity(exam.getLongevity());
                    response.setType(exam.getType());
                    response.setCourseApplicationId(app.getId());
                    response.setCourseDetailName(exam.getCourseDateEntity().getCourseDetailEntity().getName());
                    if (exam.getCourseDateEntity() != null) {
                        response.setCourseDateName(exam.getCourseDateEntity().getName());
                    }

                    return response;
                })
                .collect(Collectors.toList());
    }

    @Transactional
    public List<ExamResponse> getExamsForCurrentUser(String userId) {
        return examRepository.findAllByUserId(userId).stream()
                .filter(exam -> {
                    if (exam.getCourseDateEntity() == null) return false;
                    var applications = exam.getCourseDateEntity().getApplications();
                    if (applications == null) return false;
                    return applications.stream()
                            .anyMatch(app ->
                                    app.getUser() != null &&
                                            app.getUser().getId() != null &&
                                            userId.equals(app.getUser().getId()));
                })
                .map(exam -> {
                    ExamResponse response = new ExamResponse();
                    response.setId(exam.getId());
                    response.setExamDate(exam.getExamDate().toString());
                    response.setLocation(exam.getLocation() != null ? exam.getLocation().toString() : null);
                    response.setLongevity(exam.getLongevity());
                    response.setType(exam.getType());
                    response.setCourseDateName(exam.getCourseDateEntity().getName());
                    boolean applied = exam.getExamApplicationEntities() != null &&
                            exam.getExamApplicationEntities().stream()
                                    .anyMatch(app -> app.getUser() != null && userId.equals(app.getUser().getId()));
                    response.setApplied(applied);
                    return response;
                })
                .collect(Collectors.toList());
    }
    @Transactional
    public List<AppliedExamResponse> getAllTeachedExams(String userId) {
        return examRepository.findAll().stream()
                .filter(exam -> exam.getCourseDateEntity() != null &&
                        exam.getCourseDateEntity().getApplications().stream()
                                .anyMatch(app -> app.getUser() != null && app.getUser().getId().equals(userId)))
                .map(exam -> {
                    AppliedExamResponse response = new AppliedExamResponse();
                    response.setId(exam.getId());
                    response.setExamDate(exam.getExamDate().toString());
                    response.setLocation(exam.getLocation().toString());
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
