import { Component } from '@angular/core';
import { AbstractControl, FormArray, FormBuilder, FormGroup, FormsModule, ReactiveFormsModule, ValidatorFn, Validators } from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';
import { CourseService } from '../services/course.service';
import { CourseDetailDTO, CourseDetailListingDTO } from '../models/CourseDetailListingDTO';
import { NgFor, NgForOf, NgIf } from '@angular/common';
import { MajorService } from '../services/major.service';
import { AuthService } from '../auth/auth.service';
import { MatInputModule } from '@angular/material/input';
import { MatCheckbox } from '@angular/material/checkbox';

@Component({
  selector: 'app-edit-course',
  standalone: true,
  imports: [FormsModule, ReactiveFormsModule, NgFor, MatInputModule, NgForOf, MatCheckbox],
  templateUrl: './edit-course.component.html',
  styleUrl: './edit-course.component.scss'
})
export class EditCourseComponent {
  courseForm!: FormGroup;
  courseId!: number;
  courses: CourseDetailListingDTO[] = []; // For prerequisites
  majors: any[] = []; // Fetch majors for enrollment types
  allTeachers: any[] = [];

  constructor(
    private route: ActivatedRoute,
    private fb: FormBuilder,
    private authService:AuthService,
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
    this.loadTeachers();
    console.log(this.courseForm)
  }

  initializeForm() {
    this.courseForm = this.fb.group({
      name: [''],
      description: [''],
      credits: [0],
      recommendedHalfYear: [0, [Validators.min(0)]],
      requirementType: [''],
      requiredCourses: [[]],
      enrollmentTypes: this.fb.array([], this.duplicateEnrollmentTypeValidator), // Define dynamically
      teachers: this.fb.array([]),
    });
  }

  loadCourseDetails() {
    this.courseService.getCourseById(this.courseId).subscribe((course) => {
      console.log(course)
      this.courseForm.patchValue({
        name: course.name,
        description: course.description,
        credits: course.credits,
        recommendedHalfYear: course.recommendedHalfYear,
        requirementType: course.requirementType,
        requiredCourses: course.requiredCourses,
        teachers: course.teachers,
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
      const teacherArray = this.courseForm.get('teachers') as FormArray;
      course.teachers.forEach((teacher : any) => {
        teacherArray.push(this.fb.group({
          teacherId: [teacher.teacherId, Validators.required],
          responsible: [teacher.responsible]  
        }));
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
    console.log(this.courseForm)
    this.courseService
      .updateCourse(this.courseId, this.courseForm.value)
      .subscribe(() => {
        this.router.navigate(['/courses']); // Redirect to course list after saving
      });
  }
  get enrollmentTypes(): FormArray {
    return this.courseForm.get('enrollmentTypes') as FormArray;
  }
  get teachers(): FormArray {
    return this.courseForm.get('teachers') as FormArray;
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
  addTeacher(): void {
    this.teachers.push(
      this.fb.group({
        teacherId: ['', Validators.required],
        responsible: [false],
      })
    );
  }

  removeTeacher(index: number): void {
    this.teachers.removeAt(index);
  }
  loadTeachers() {
    const authToken = localStorage.getItem("loggedInUser"); 

  if (authToken) {
    this.authService.getTeachers(authToken).subscribe(
      (data: any) => {
        this.allTeachers = data;
      },
      (error: any) => {
        console.error("Error fetching teachers:", error);
      }
    );
    } else {
      console.error("Auth token not found!");
    }
  }
  
  duplicateEnrollmentTypeValidator: ValidatorFn = (control: AbstractControl): { [key: string]: boolean } | null => {
    // Cast the AbstractControl to FormArray
    const formArray = control as FormArray;
  
    // Extract values from the FormArray
    const enrollmentTypes = formArray.controls.map((group: AbstractControl) => {
      const majorId = group.get('majorId')?.value;
      const enrollmentType = group.get('enrollmentType')?.value;
      return `${majorId}-${enrollmentType}`;
    });
  
    // Check for duplicates
    const hasDuplicates = new Set(enrollmentTypes).size !== enrollmentTypes.length;
  
    // Return validation result
    return hasDuplicates ? { duplicateEnrollmentType: true } : null;
  };
  
}
