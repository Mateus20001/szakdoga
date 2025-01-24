import { Component } from '@angular/core';
import { CourseTeacherService } from '../services/course-teacher.service';
import { NgFor, NgIf } from '@angular/common';
import { Router } from '@angular/router';

@Component({
  selector: 'app-teached-courses',
  standalone: true,
  imports: [NgIf, NgFor],
  templateUrl: './teached-courses.component.html',
  styleUrl: './teached-courses.component.scss'
})
export class TeachedCoursesComponent {
  courses: any[] = [];
  errorMessage: string = '';

  constructor(private courseService: CourseTeacherService, private router: Router) {}

  ngOnInit(): void {
    this.loadCourses();
  }

  // Load courses for the authenticated teacher
  loadCourses(): void {
    this.courseService.getCoursesByTeacher().subscribe(
      (data) => {
        this.courses = data; // Assign the response to the courses array
      },
      (error) => {
        console.error('Error fetching courses:', error);
        this.errorMessage = 'An error occurred while fetching courses. Please try again later.';
      }
    );
  }
  editCourse(courseId: number) {
    this.router.navigate([`/edit-teacher-course/`, courseId]);
  }
}
