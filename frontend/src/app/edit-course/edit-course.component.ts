import { Component } from '@angular/core';
import { FormArray, FormBuilder, FormGroup, FormsModule, ReactiveFormsModule, Validators } from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';
import { CourseService } from '../services/course.service';
import { CourseDetailDTO, CourseDetailListingDTO } from '../models/CourseDetailListingDTO';
import { NgFor, NgIf } from '@angular/common';
import { MajorService } from '../services/major.service';

@Component({
  selector: 'app-edit-course',
  standalone: true,
  imports: [FormsModule, ReactiveFormsModule, NgFor],
  templateUrl: './edit-course.component.html',
  styleUrl: './edit-course.component.scss'
})
export class EditCourseComponent {
  courseForm!: FormGroup;
  courseId!: number;
  courses: CourseDetailListingDTO[] = []; // For prerequisites
  majors: any[] = []; // Fetch majors for enrollment types

  constructor(
    private route: ActivatedRoute,
    private fb: FormBuilder,
    private courseService: CourseService,
    private majorService: MajorService,
    private router: Router
  ) {}

  ngOnInit(): void {
    this.courseId = +this.route.snapshot.paramMap.get('id')!;
    this.initializeForm();
    this.loadCourseDetails();
    this.loadCourses(this.courseId);
    this.loadMajors();
  }

  initializeForm() {
    this.courseForm = this.fb.group({
      name: [''],
      description: [''],
      credits: [0],
      requirementType: [''],
      requiredCourses: [[]],
      enrollmentTypes: this.fb.array([]), // Define dynamically
    });
  }

  loadCourseDetails() {
    this.courseService.getCourseById(this.courseId).subscribe((course) => {
      this.courseForm.patchValue({
        name: course.name,
        description: course.description,
        credits: course.credits,
        requirementType: course.requirementType,
        requiredCourses: course.requiredCourses,
      });

      // Set enrollmentTypes
      const enrollmentTypesArray = this.courseForm.get('enrollmentTypes') as any;
      course.enrollmentTypes.forEach((enrollmentType) => {
        enrollmentTypesArray.push(
          this.fb.group({
            majorId: [enrollmentType.majorId],
            enrollmentType: [enrollmentType.enrollmentType],
          })
        );
      });
    });
  }

  loadCourses(currentCourseId: number) {
    this.courseService.getAllCourses().subscribe((courses) => {
      this.courses = courses.filter((course) => course.id !== currentCourseId);
    });
  }

  loadMajors() {
    this.majorService.getMajors().subscribe((data) => {
      this.majors = data;
    });
  }

  submit() {
    this.courseService
      .updateCourse(this.courseId, this.courseForm.value)
      .subscribe(() => {
        this.router.navigate(['/courses']); // Redirect to course list after saving
      });
  }
  get enrollmentTypes(): FormArray {
    return this.courseForm.get('enrollmentTypes') as FormArray;
  }

  addEnrollmentType(): void {
    this.enrollmentTypes.push(
      this.fb.group({
        majorId: ['', Validators.required],
        enrollmentType: ['', Validators.required],
      })
    );
  }

  removeEnrollmentType(index: number): void {
    this.enrollmentTypes.removeAt(index);
  }

}
