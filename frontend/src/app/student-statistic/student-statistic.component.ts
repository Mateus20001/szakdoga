import { Component } from '@angular/core';
import { AppliedCourse } from '../models/AppliedCourseDTO';
import { CourseApplicationService } from '../services/course-application.service';

@Component({
  selector: 'app-student-statistic',
  standalone: true,
  imports: [],
  templateUrl: './student-statistic.component.html',
  styleUrl: './student-statistic.component.scss'
})
export class StudentStatisticComponent {
  appliedCourses: AppliedCourse[] = [];

  constructor(private courseApplicationService: CourseApplicationService) {}

  fetchAppliedCourses(): void {
    this.courseApplicationService.getUserAppliedCourses().subscribe(
      (courses) => {
        this.appliedCourses = courses;
        console.log(this.appliedCourses);
      },
      (error) => {
        console.error('Error fetching applied courses:', error);
      }
    );
  }
}
