import { Component } from '@angular/core';
import { GradingService } from '../services/grading.service';
import { TeacherStudentGradingDTO } from '../models/TeacherStudentGradingDTO';
import { BrowserModule } from '@angular/platform-browser';
import { HttpClientModule } from '@angular/common/http';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { MatExpansionModule } from '@angular/material/expansion';
import { MatListModule } from '@angular/material/list';
import { MatButton, MatButtonModule } from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';
import { MatCardModule } from '@angular/material/card';
import { CommonModule, NgFor } from '@angular/common';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInput, MatInputModule } from '@angular/material/input';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { MatAutocompleteModule } from '@angular/material/autocomplete';

@Component({
  selector: 'app-grading-students',
  standalone: true,
  imports: [NgFor,
    CommonModule,
    HttpClientModule,
    MatExpansionModule,
    MatListModule,
    MatButtonModule,
    MatIconModule,
    MatCardModule,
    MatFormFieldModule,
    MatInputModule,
    ReactiveFormsModule,
    MatInput,
    MatButton,
    MatAutocompleteModule,
    FormsModule],
  templateUrl: './grading-students.component.html',
  styleUrl: './grading-students.component.scss'
})
export class GradingStudentsComponent {
  gradingData: TeacherStudentGradingDTO[] = [];
  groupedCourses: { [key: string]: TeacherStudentGradingDTO[] } = {}; // Grouped by courseDetailName
  grades: { [identifier: string]: number } = {};
  constructor(private gradingService: GradingService) {}
  gradeOptions = [
    { value: -1, display: 'nem értékelhető' },
    { value: 0, display: 'nem jelent meg' },
    { value: 1, display: '1' },
    { value: 2, display: '2' },
    { value: 3, display: '3' },
    { value: 4, display: '4' },
    { value: 5, display: '5' }
  ];
  ngOnInit(): void {
    this.gradingService.getGradingData().subscribe(data => {
      this.gradingData = data;
      this.groupCoursesByDetailName();
    });
  }

  groupCoursesByDetailName(): void {
    this.groupedCourses = this.gradingData.reduce((acc, course) => {
      if (!acc[course.courseDetailName]) {
        acc[course.courseDetailName] = [];
      }
      acc[course.courseDetailName].push(course);
      return acc;
    }, {} as { [key: string]: TeacherStudentGradingDTO[] });
  }

  
  saveGrades(): void {
    const gradesToSave = Object.keys(this.grades).map(identifier => ({
      identifier,
      gradeValue: this.grades[identifier]
    }));

    console.log("Saving grades:", gradesToSave);
  }
}
