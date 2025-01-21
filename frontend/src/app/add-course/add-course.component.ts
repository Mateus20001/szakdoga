import { Component, NgModule } from '@angular/core';
import { CourseService } from '../services/course.service';
import { NgFor, NgIf } from '@angular/common';
import { MatFormField, MatInputModule } from '@angular/material/input';
import { FormArray, FormBuilder, FormGroup, FormsModule, ReactiveFormsModule, Validators } from '@angular/forms';
import { CourseDetailDTO } from '../models/CourseDetailListingDTO';
import { MajorService } from '../services/major.service';

@Component({
  selector: 'app-add-course',
  standalone: true,
  imports: [NgFor, MatInputModule, ReactiveFormsModule, FormsModule],
  templateUrl: './add-course.component.html',
  styleUrl: './add-course.component.scss'
})
export class AddCourseComponent {
  courseForm: FormGroup;
  majors: any[] = [];
  courses: any[] = [];

  constructor(private fb: FormBuilder, private courseService: CourseService, private majorService: MajorService) {
    this.courseForm = this.fb.group({
      name: ['', Validators.required],
      description: ['', Validators.required],
      credits: [0, [Validators.required, Validators.min(1)]],
      requirementType: ['', Validators.required],
      requiredCourses: [[]], // Array of course IDs
      enrollmentTypes: this.fb.array([]), // Enrollment types array
    });
  }

  ngOnInit(): void {
    this.loadMajors();
    this.loadCourses();
  }

  get enrollmentTypes() {
    return this.courseForm.get('enrollmentTypes') as FormArray;
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

  submit(): void {
    if (this.courseForm.valid) {
      const courseDetail: CourseDetailDTO = this.courseForm.value;
      this.courseService.addCourseDetail(courseDetail).subscribe({
        next: (response) => {
          alert('Course added successfully!');
          this.courseForm.reset();
          this.enrollmentTypes.clear();
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
