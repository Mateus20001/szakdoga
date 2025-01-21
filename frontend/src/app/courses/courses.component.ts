import { Component } from '@angular/core';
import { CourseDetailDTO, CourseDetailListingDTO } from '../models/CourseDetailListingDTO';
import { CourseService } from '../services/course.service';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { NgFor, NgIf } from '@angular/common';
import { Router } from '@angular/router';

@Component({
  selector: 'app-courses',
  standalone: true,
  imports: [FormsModule, ReactiveFormsModule, NgFor],
  templateUrl: './courses.component.html',
  styleUrl: './courses.component.scss'
})
export class CoursesComponent {
  courses: CourseDetailListingDTO[] = [];
  selectedCourse: CourseDetailDTO | null= null;

  constructor(private courseService: CourseService, private router: Router) {}

  ngOnInit(): void {
    this.courseService.getAllCourses().subscribe((data) => {
      this.courses = data;
    });
  }

  editCourse(id: number) {
    this.router.navigate(['/edit-course', id]);
  }
}
