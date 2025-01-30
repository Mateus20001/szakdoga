import { Component } from '@angular/core';
import { CourseDetailStudentListingDTO } from '../models/CourseDetailListingDTO';
import { CourseService } from '../services/course.service';
import { NgFor, NgIf } from '@angular/common';
import { CourseDetailsComponent } from "./course-details/course-details.component";
import { MatButtonModule } from '@angular/material/button';
import { MatExpansionModule } from '@angular/material/expansion';

@Component({
  selector: 'app-course-application',
  standalone: true,
  imports: [NgIf, NgFor, CourseDetailsComponent, MatExpansionModule, MatButtonModule,],
  templateUrl: './course-application.component.html',
  styleUrl: './course-application.component.scss'
})
export class CourseApplicationComponent {
  courses: CourseDetailStudentListingDTO[] = [];
  errorMessage: string = '';
  expandedCourseId: number | null = null;
  constructor(private courseService: CourseService) {}

  ngOnInit(): void {
    this.loadCourses();
  }

  loadCourses(): void {
    this.courseService.getAllStudentCourses().subscribe({
      next: (data) => {
        console.log('Fetched Courses:', data); // Log response to console
        this.courses = data;
      },
      error: (err) => {
        console.error('Error fetching courses:', err); // Log error to console
        this.errorMessage = 'Failed to load courses';
      }
    });
  }
  loadCourseDetails(courseId: number) {
      this.expandedCourseId = courseId;
  }
  translateEnrollmentType(enrollmentType: string): string {
    switch (enrollmentType) {
      case 'MANDATORY':
        return 'Kötelező';
      case 'MANDATORY_OPTIONAL':
        return 'Kötelezően választható';
      case 'OPTIONAL':
        return 'Szabadon választható';
      default:
        return enrollmentType;
    }
  }
  translateRequirementType(requirementType: string): string {
    switch (requirementType) {
      case 'COLLOQUIUM':
        return 'Kollokvium';
      case 'PRACTICE':
        return 'Gyakorlat';
      case 'PE':
        return 'Testnevelés';
      default:
        return requirementType;
    }
  }
}
