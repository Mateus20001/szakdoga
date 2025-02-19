package com.szakdoga.backend.courses.repositories;

import com.szakdoga.backend.auth.model.MajorEntity;
import com.szakdoga.backend.courses.models.CourseDetailEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface CourseDetailRepository extends JpaRepository<CourseDetailEntity, Long> {


    @Query("SELECT c.id FROM CourseDetailEntity c")
    List<Long> findCourseIds();

}
