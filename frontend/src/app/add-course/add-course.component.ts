import { Component, NgModule } from '@angular/core';
import { CourseService } from '../services/course.service';
import { NgFor, NgIf } from '@angular/common';
import { MatFormField, MatInputModule } from '@angular/material/input';
import { FormArray, FormBuilder, FormGroup, FormsModule, ReactiveFormsModule, Validators } from '@angular/forms';
import { CourseDetailDTO } from '../models/CourseDetailListingDTO';
import { MatCheckboxModule } from '@angular/material/checkbox';
import { MajorService } from '../services/major.service';
import { AuthService } from '../auth/auth.service';

@Component({
  selector: 'app-add-course',
  standalone: true,
  imports: [NgFor, MatInputModule, ReactiveFormsModule, FormsModule, MatFormField, MatCheckboxModule],
  templateUrl: './add-course.component.html',
  styleUrl: './add-course.component.scss'
})
export class AddCourseComponent {
  courseForm: FormGroup;
  majors: any[] = [];
  courses: any[] = [];
  teachers: any[] = [];

  constructor(
    private fb: FormBuilder,
    private courseService: CourseService,
    private majorService: MajorService,
    private authService: AuthService
  ) {
    this.courseForm = this.fb.group({
      name: ['', Validators.required],
      description: ['', Validators.required],
      credits: [0, [Validators.required, Validators.min(1)]],
      recommendedHalfYear: [0, [Validators.min(0)]],
      requirementType: ['', Validators.required],
      requiredCourses: [[]], // Array of course IDs
      enrollmentTypes: this.fb.array([]), // Enrollment types array
      teachers: this.fb.array([]), // Array for teachers with responsibility
    });
  }

  ngOnInit(): void {
    this.loadMajors();
    this.loadCourses();
    this.loadTeachers();
  }

  get enrollmentTypes() {
    return this.courseForm.get('enrollmentTypes') as FormArray;
  }

  get teacherArray() {
    return this.courseForm.get('teachers') as FormArray;
  }

  addEnrollmentType(): void {
    const enrollmentTypeGroup = this.fb.group({
      majorId: ['', Validators.required],
      enrollmentType: ['', Validators.required],
    });
    this.enrollmentTypes.push(enrollmentTypeGroup);
  }

  removeEnrollmentType(index: number): void {
    this.enrollmentTypes.removeAt(index);
  }

  addTeacher(): void {
    const teacherGroup = this.fb.group({
      teacherId: ['', Validators.required],
      responsible: [false, Validators.required],
    });
    this.teacherArray.push(teacherGroup);
    console.log(this.courseForm)
  }

  removeTeacher(index: number): void {
    this.teacherArray.removeAt(index);
  }

  loadMajors(): void {
    this.majorService.getMajors().subscribe((data) => {
      this.majors = data;
    });
  }

  loadCourses(): void {
    this.courseService.getAllCourses().subscribe((data) => {
      this.courses = data;
    });
  }

  loadTeachers(): void {
    const authToken = localStorage.getItem('loggedInUser');

    if (authToken) {
      this.authService.getTeachers(authToken).subscribe(
        (data: any) => {
          this.teachers = data;
          console.log(this.teachers);
        },
        (error: any) => {
          console.error('Error fetching teachers:', error);
        }
      );
    } else {
      console.error('Auth token not found!');
    }
  }

  submit(): void {
    if (this.courseForm.valid) {
      const courseData = { ...this.courseForm.value };
      courseData.teachers = this.teacherArray.value.map((teacher: any) => {
        return {
          teacherId: teacher.teacherId.toString(),
          responsible: teacher.responsible,
        };
      });

      this.courseService.addCourseDetail(courseData).subscribe({
        next: (response) => {
          alert('Course added successfully!');
          this.courseForm.reset();
          this.enrollmentTypes.clear();
          this.teacherArray.clear();
          this.loadCourses();
        },
        error: (err) => {
          console.error(err);
          alert('Failed to add course.');
        },
      });
    }
  }
}
