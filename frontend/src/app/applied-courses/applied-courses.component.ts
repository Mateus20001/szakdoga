import { Component } from '@angular/core';
import { AppliedCourse } from '../models/AppliedCourseDTO';
import { CourseApplicationService } from '../services/course-application.service';
import { MatSnackBar } from '@angular/material/snack-bar';
import { NgFor, NgIf } from '@angular/common';
import { MatCard } from '@angular/material/card';
import { CommonModule } from '@angular/common';
import { HttpClientModule } from '@angular/common/http';
import { MatTableModule } from '@angular/material/table';
import { MatButtonModule } from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';
import { MatCardModule } from '@angular/material/card';
import { MatExpansionModule } from '@angular/material/expansion';
import { BrowserModule } from '@angular/platform-browser';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { LocationEnum } from '../models/LocationEnum';

@Component({
  selector: 'app-applied-courses',
  standalone: true,
  imports: [NgIf, NgFor,
    MatTableModule,   
    MatButtonModule, 
    MatIconModule,
    MatCardModule,
    MatExpansionModule,
    CommonModule ],
  templateUrl: './applied-courses.component.html',
  styleUrl: './applied-courses.component.scss'
})
export class AppliedCoursesComponent {
  displayedColumns: string[] = ['courseDateName', 'location', 'teacherIds', 'actions'];
  appliedCourses: AppliedCourse[] = [];
  expandedElement: AppliedCourse | null = null;
  constructor(private courseApplicationService: CourseApplicationService,
    private snackBar: MatSnackBar) {}

    ngOnInit(): void {
      this.fetchAppliedCourses();
    }

    highestGrade(grades: { gradeValue: number }[]): number {
      return Math.max(...grades.map(grade => grade.gradeValue));
    }
    getLocationString(locationKey: string): string {
      return LocationEnum[locationKey as keyof typeof LocationEnum] || 'Unknown Location';
    }

    fetchAppliedCourses(): void {
      this.courseApplicationService.getUserAppliedCourses().subscribe(
        (courses) => {
          this.appliedCourses = courses;
          console.log(this.appliedCourses);
        },
        (error) => {
          console.error('Error fetching applied courses:', error);
          this.snackBar.open('Failed to load applied courses.', 'Close', { duration: 3000 });
        }
      );
    }
}
