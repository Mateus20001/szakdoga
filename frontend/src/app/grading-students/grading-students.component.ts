import { Component } from '@angular/core';
import { GradingService } from '../services/grading.service';
import { TeacherStudentGradingDTO, GradeDTO } from '../models/TeacherStudentGradingDTO';
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
  groupedCourses: { [key: string]: TeacherStudentGradingDTO[] } = {};
  grades: { [key: string]: number } = {}; // Key: courseDateId-studentId -> grade value

  gradeOptions = [
    { value: -1, display: 'nem értékelhető' },
    { value: 0, display: 'nem jelent meg' },
    { value: 1, display: '1' },
    { value: 2, display: '2' },
    { value: 3, display: '3' },
    { value: 4, display: '4' },
    { value: 5, display: '5' },
  ];

  constructor(private gradingService: GradingService) {}

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

  generateKey(courseDateId: number, studentId: string): string {
    return `${courseDateId}-${studentId}`;
  }

  getGradeDisplay(value: number): string {
    const found = this.gradeOptions.find(opt => opt.value === value);
    return found ? found.display : 'N/A';
  }

  getGradesForStudent(course: TeacherStudentGradingDTO, studentId: string): GradeDTO[] {
    return course.grades.filter(g => g.studentId === studentId);
  }

  saveGrades(): void {
    const gradesToSave = this.gradingData.flatMap(course =>
      course.students
        .filter(student => this.grades[this.generateKey(course.courseDateId, student.identifier)] !== undefined)
        .map(student => ({
          identifier: student.identifier,
          courseDateId: course.courseDateId,
          gradeValue: this.grades[this.generateKey(course.courseDateId, student.identifier)],
        }))
    );

    this.gradingService.saveGrades(gradesToSave).subscribe({
      next: res => console.log('Saved:', res),
      error: err => console.error('Error:', err),
    });
  }
}
