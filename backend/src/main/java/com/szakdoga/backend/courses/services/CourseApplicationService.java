package com.szakdoga.backend.courses.services;

import com.szakdoga.backend.auth.model.GlobalSettings;
import com.szakdoga.backend.auth.model.User;
import com.szakdoga.backend.auth.repositories.GlobalSettingsRepository;
import com.szakdoga.backend.auth.repositories.UserRepository;
import com.szakdoga.backend.courses.controllers.CourseController;
import com.szakdoga.backend.courses.dtos.*;
import com.szakdoga.backend.courses.models.*;
import com.szakdoga.backend.courses.repositories.CourseApplicationRepository;
import com.szakdoga.backend.courses.repositories.CourseDateRepository;
import com.szakdoga.backend.courses.repositories.CourseTimetablePlannerRepository;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class CourseApplicationService {
    private final CourseApplicationRepository courseApplicationRepository;
    private final UserRepository userRepository;
    private final CourseDateRepository courseDateRepository;
    private final CourseTimetablePlannerRepository courseTimetablePlannerRepository;
    private final GlobalSettingsRepository globalSettingsRepository;
    private static final Logger log = LoggerFactory.getLogger(CourseApplicationService.class);

    public CourseApplicationService(
            CourseApplicationRepository courseApplicationRepository,
            UserRepository userRepository,
            CourseDateRepository courseDateRepository,
            CourseTimetablePlannerRepository courseTimetablePlannerRepository, GlobalSettingsRepository globalSettingsRepository) {
        this.courseApplicationRepository = courseApplicationRepository;
        this.userRepository = userRepository;
        this.courseDateRepository = courseDateRepository;
        this.courseTimetablePlannerRepository = courseTimetablePlannerRepository;
        this.globalSettingsRepository = globalSettingsRepository;
    }
    @Transactional
    public CourseApplicationResponse applyToCourse(String userId, Long courseDateId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("User not found"));
        // Fetch the course date entity
        CourseDateEntity courseDate = courseDateRepository.findById(courseDateId)
                .orElseThrow(() -> new RuntimeException("Course date not found"));
        CourseDetailEntity courseDetail = courseDate.getCourseDetailEntity();
        List<CourseDetailEntity> requiredCourses = courseDetail.getRequiredCourses();
        List<String> notCompletedCourses = new ArrayList<>();

        for (CourseDetailEntity requiredCourse : requiredCourses) {
            boolean completed = false;

            for (CourseDateEntity requiredCourseDate : requiredCourse.getCourseDates()) {
                Optional<CourseApplicationEntity> userApplicationOpt =
                        courseApplicationRepository.findByUserIdAndCourseDateEntity(userId, requiredCourseDate);

                if (userApplicationOpt.isPresent()) {
                    CourseApplicationEntity userApplication = userApplicationOpt.get();

                    if (userApplication.getGrades() == null || userApplication.getGrades().isEmpty()) {
                        notCompletedCourses.add(requiredCourse.getName());
                        completed = false;
                        break; // no point checking other dates
                    }

                    boolean passed = userApplication.getGrades().stream()
                            .anyMatch(g -> {
                                try {
                                    return g.getGradeValue() > 1;
                                } catch (NumberFormatException e) {
                                    return false;
                                }
                            });

                    if (passed) {
                        completed = true;
                        break;
                    }
                }
            }

            if (!completed) {
                notCompletedCourses.add(requiredCourse.getName());
            }
        }

        if (!notCompletedCourses.isEmpty()) {
            String failedCourses = String.join(", ", notCompletedCourses);
            return new CourseApplicationResponse(4,
                    "Nem teljesítetted az alábbi előfeltétel kurzus(ok)at: " + failedCourses);
        }

        GlobalSettings semester = globalSettingsRepository.getReferenceById(1L);
        GlobalSettings exam_period = globalSettingsRepository.getReferenceById(4L);
        if (!Objects.equals(courseDate.getSemester(), semester.getAttribute())) {
            return new CourseApplicationResponse(1, "Csak ezen szemeszter kurzusait veheted fel!");
        }
        if (!exam_period.isActivated()) {
            return new CourseApplicationResponse(2, "Nincs kurzusjelentkezési időszak!");
        }
        // Check if the user has already applied
        Optional<CourseApplicationEntity> existingApplication =
                courseApplicationRepository.findByUserIdAndCourseDateEntity(userId, courseDate);

        if (existingApplication.isPresent()) {
            return new CourseApplicationResponse(3, "Már felvetted ezt a kurzust!");
        }


        Optional<CourseApplicationEntity> existingApplicationCourseDetail =
                courseApplicationRepository.findByUserIdAndCourseDetailId(userId, courseDate.getCourseDetailEntity().getId());
        if (existingApplicationCourseDetail.isPresent()) {
            existingApplicationCourseDetail.get().setCourseDateEntity(null);
            existingApplicationCourseDetail.get().setUser(null);
            courseApplicationRepository.delete(existingApplicationCourseDetail.get());
            courseApplicationRepository.deleteOrphanedCourseTeacherEntities();
        }
        log.info("Applying course application to user {}", userId);
        // Create new course application
        CourseApplicationEntity application = new CourseApplicationEntity();
        application.setUser(user);
        application.setCourseDateEntity(courseDate);
        this.addCourseToTimetable(userId, courseDateId);
        courseApplicationRepository.save(application);
        return new CourseApplicationResponse(0, "Sikeresen felvetted a kurzust!");
    }

    @Transactional
    public void removeApplication(String userId, Long courseDateId) {
        // Fetch the course date entity
        CourseDateEntity courseDate = courseDateRepository.findById(courseDateId)
                .orElseThrow(() -> new RuntimeException("Course date not found"));

        // Find the application and delete it
        CourseApplicationEntity application = courseApplicationRepository
                .findByUserIdAndCourseDateEntity(userId, courseDate)
                .orElseThrow(() -> new RuntimeException("Application not found"));
        application.setCourseDateEntity(null);
        application.setUser(null);
        courseApplicationRepository.delete(application);
        courseApplicationRepository.deleteOrphanedCourseTeacherEntities();
        this.removeTimetableEntity(courseDateId, userId);
    }

    @Transactional
    public List<CourseApplicationEntity> getUserApplicationsForCourse(String userId, Long courseId) {
        log.info("Getting user applications for course id {}", courseId);
        return courseApplicationRepository.findByUserIdAndCourseDateEntity_CourseDetailEntity_Id(userId, courseId);
    }
    @Transactional
    public List<UserAppliedCourseDto> getUserAppliedCourses(String userId) {
        List<CourseApplicationEntity> applications = courseApplicationRepository.findByUserId(userId);

        return applications.stream()
                .map(application -> {
                    CourseDateEntity courseDate = application.getCourseDateEntity();
                    CourseDetailEntity courseDetail = courseDate.getCourseDetailEntity();

                    List<String> teacherIds = courseDate.getTeachers().stream()
                            .map(User::getId)
                            .collect(Collectors.toList());
                    List<AppliedGradeDTO> gradeDTOs = application.getGrades().stream()
                            .map(grade -> new AppliedGradeDTO(
                                    grade.getId(),
                                    grade.getGradeValue(),
                                    grade.getGradedBy().getId(),  // Assuming you have a getFullName() method in User class
                                    grade.getCreationDate()
                            ))
                            .collect(Collectors.toList());
                    return new UserAppliedCourseDto(
                            courseDetail.getName(),  // Course Detail Name
                            courseDate.getId(),      // Course Date ID
                            courseDate.getName(),    // Course Date Name
                            teacherIds,              // Teacher IDs
                            courseDate.getLocation(), // Location
                            gradeDTOs
                    );
                })
                .collect(Collectors.toList());
    }
    @Transactional
    public List<CourseTimetableEntityDto> getUserTimetable(String userId) {
        log.info(userId);
        List<CourseTimetablePlannerEntity> courses = courseTimetablePlannerRepository.findAllByUserId(userId);
        return courses.stream().map(entity -> {
            CourseTimetableEntityDto dto = new CourseTimetableEntityDto();
            dto.setId(entity.getId());
            dto.setCourseDateId(entity.getCourseDateEntity().getId());
            return dto;
        }).collect(Collectors.toList());
    }
    public void addCourseToTimetable(String userId, long courseDateId) {
        log.info("Adding course to timetable {}", courseDateId);
        CourseTimetablePlannerEntity course = new CourseTimetablePlannerEntity();
        course.setUser(userRepository.findById(userId).orElseThrow(() -> new RuntimeException("User not found")));
        course.setCourseDateEntity(courseDateRepository.findById(courseDateId).get());
        Optional<CourseTimetablePlannerEntity> existingCourse = courseTimetablePlannerRepository.findByCourseDateEntityIdAndUserId(courseDateId, userId);
        if (existingCourse.isPresent()) {
            log.info("Course already exists in the timetable for user {}", userId);
            return;  // If the course already exists, exit the method
        }
        courseTimetablePlannerRepository.save(course);
    }

    @Transactional
    public void removeTimetableEntity(long id, String userId) {
        log.info("aaaa");
        CourseTimetablePlannerEntity course = courseTimetablePlannerRepository.findByCourseDateEntityIdAndUserId(id, userId).orElseThrow();
        course.setUser(null);
        course.setCourseDateEntity(null);
        courseTimetablePlannerRepository.save(course);
        courseTimetablePlannerRepository.delete(course);
        courseTimetablePlannerRepository.deleteOrphanedCourseTimetableEntities();
    }

    @Transactional
    public List<StudentStatisticDTO> getUserStatistics(String userId) {
        List<CourseApplicationEntity> applications = courseApplicationRepository.findByUserId(userId);

        return applications.stream()
                .map(application -> {
                    return application.getGrades().stream()
                            .max(Comparator.comparingInt(Grade::getGradeValue))
                            .map(bestGrade -> {
                                StudentStatisticDTO dto = new StudentStatisticDTO();
                                dto.setBestGradeValue(bestGrade.getGradeValue());
                                dto.setSemester(bestGrade.getCourseApplicationEntity().getCourseDateEntity().getSemester());
                                dto.setCourseDetailName(bestGrade.getCourseApplicationEntity().getCourseDateEntity().getName());
                                dto.setCreditScore(bestGrade.getCourseApplicationEntity().getCourseDateEntity().getCourseDetailEntity().getCredits());
                                return dto;
                            }).orElse(null);
                })
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
    }

}