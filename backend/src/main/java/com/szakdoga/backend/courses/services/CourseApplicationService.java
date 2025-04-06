package com.szakdoga.backend.courses.services;

import com.szakdoga.backend.auth.model.User;
import com.szakdoga.backend.auth.repositories.UserRepository;
import com.szakdoga.backend.courses.controllers.CourseController;
import com.szakdoga.backend.courses.dtos.AppliedGradeDTO;
import com.szakdoga.backend.courses.dtos.CourseTimetableEntityDto;
import com.szakdoga.backend.courses.dtos.GradeDTO;
import com.szakdoga.backend.courses.dtos.UserAppliedCourseDto;
import com.szakdoga.backend.courses.models.CourseApplicationEntity;
import com.szakdoga.backend.courses.models.CourseDateEntity;
import com.szakdoga.backend.courses.models.CourseDetailEntity;
import com.szakdoga.backend.courses.models.CourseTimetablePlannerEntity;
import com.szakdoga.backend.courses.repositories.CourseApplicationRepository;
import com.szakdoga.backend.courses.repositories.CourseDateRepository;
import com.szakdoga.backend.courses.repositories.CourseTimetablePlannerRepository;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class CourseApplicationService {
    private final CourseApplicationRepository courseApplicationRepository;
    private final UserRepository userRepository;
    private final CourseDateRepository courseDateRepository;
    private final CourseTimetablePlannerRepository courseTimetablePlannerRepository;
    private static final Logger log = LoggerFactory.getLogger(CourseApplicationService.class);

    public CourseApplicationService(
            CourseApplicationRepository courseApplicationRepository,
            UserRepository userRepository,
            CourseDateRepository courseDateRepository,
            CourseTimetablePlannerRepository courseTimetablePlannerRepository) {
        this.courseApplicationRepository = courseApplicationRepository;
        this.userRepository = userRepository;
        this.courseDateRepository = courseDateRepository;
        this.courseTimetablePlannerRepository = courseTimetablePlannerRepository;
    }
    @Transactional
    public void applyToCourse(String userId, Long courseDateId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("User not found"));
        // Fetch the course date entity
        CourseDateEntity courseDate = courseDateRepository.findById(courseDateId)
                .orElseThrow(() -> new RuntimeException("Course date not found"));

        // Check if the user has already applied
        Optional<CourseApplicationEntity> existingApplication =
                courseApplicationRepository.findByUserIdAndCourseDateEntity(userId, courseDate);

        if (existingApplication.isPresent()) {
            throw new RuntimeException("User has already applied to this course date.");
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

        courseApplicationRepository.save(application);
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
        courseTimetablePlannerRepository.save(course);
    }

    public void removeTimetableEntity(long id) {
        CourseTimetablePlannerEntity course = courseTimetablePlannerRepository.findById(id).orElseThrow();
        course.setUser(null);
        course.setCourseDateEntity(null);
        courseTimetablePlannerRepository.save(course);
        courseTimetablePlannerRepository.delete(course);
        courseTimetablePlannerRepository.deleteOrphanedCourseTimetableEntities();
    }
}