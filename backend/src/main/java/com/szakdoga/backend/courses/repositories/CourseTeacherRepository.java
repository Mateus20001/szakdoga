package com.szakdoga.backend.courses.repositories;

import com.szakdoga.backend.auth.model.User;
import com.szakdoga.backend.courses.models.CourseDetailEntity;
import com.szakdoga.backend.courses.models.CourseTeacherEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface CourseTeacherRepository extends JpaRepository<CourseTeacherEntity, Long> {
    Optional<CourseTeacherEntity> findByCourseDetailAndTeacher(CourseDetailEntity course, User teacher);

    List<CourseTeacherEntity> findByCourseDetail(CourseDetailEntity course);

    @Query("SELECT ct.courseDetail FROM CourseTeacherEntity ct WHERE ct.teacher.id = :teacherId")
    List<CourseDetailEntity> findCoursesByTeacherId(@Param("teacherId") String teacherId);

    List<CourseTeacherEntity> findByCourseDetailId(Long courseId);

    boolean existsByCourseDetailIdAndTeacherIdAndResponsible(Long courseId, String userId, boolean b);

    boolean existsByCourseDetailAndTeacher(CourseDetailEntity courseDetail, User teacher);

    @Modifying
    @Query("DELETE FROM CourseTeacherEntity c WHERE c.courseDetail IS NULL OR c.teacher IS NULL")
    void deleteOrphanedCourseTeacherEntities();


    Optional<CourseTeacherEntity> findByCourseDetailIdAndTeacherId(Long courseId, String userId);
}
