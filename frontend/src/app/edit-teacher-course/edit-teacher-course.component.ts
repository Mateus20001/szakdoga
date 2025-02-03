import { Component } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { CourseTeacherService } from '../services/course-teacher.service';
import { NgFor, NgIf } from '@angular/common';
import { MatInputModule } from '@angular/material/input';
import { FormBuilder, FormGroup, FormsModule, ReactiveFormsModule, Validators } from '@angular/forms';
import { CourseDateService } from '../services/course-date.service';
import { LocationEnum } from '../models/LocationEnum';
import { DayOfWeek } from '../models/DayOfWeek';

@Component({
  selector: 'app-edit-teacher-course',
  standalone: true,
  imports: [NgIf, NgFor, MatInputModule, FormsModule, ReactiveFormsModule],
  templateUrl: './edit-teacher-course.component.html',
  styleUrl: './edit-teacher-course.component.scss'
})
export class EditTeacherCourseComponent {
  courseId!: number;
  teachers: { teacherId: string, responsible: boolean }[] = [];
  isUserResponsible: boolean = false;
  addCourseDateForm: FormGroup;
  successMessage = '';
  errorMessage = '';
  courseDates: {
    id: number;
    name: string;
    dayOfWeek: string;
    startTime: string;
    endTime: string;
    teacherIds: string[];
    maxLimit: number;
    currentlyApplied: number;
    location: string; // Now includes location
  }[] = [];

  newTeacherId: string = '';
  isResponsible: boolean = false;
  isEditingCourse: boolean = false;
  isEditing: boolean = false;
  editingTeacherId: string | null = null;
  selectedCourseDate: any;
  availableLocations = Object.values(LocationEnum);

  constructor(
    private fb: FormBuilder,
    private courseTeacherService: CourseTeacherService,
    private route: ActivatedRoute,
    private courseDateService: CourseDateService
  ) {
    this.addCourseDateForm = this.fb.group({
      name: ['', Validators.required],
      dayOfWeek: ['', Validators.required],
      startTime: ['', Validators.required],
      endTime: ['', Validators.required],
      teacherIds: [[], Validators.required],
      maxLimit: ['', Validators.required],
      location: [this.availableLocations[0], Validators.required]
    });
  }

  ngOnInit(): void {
    this.courseId = +this.route.snapshot.paramMap.get('id')!;
    this.fetchCourseDates();
    this.fetchTeachers();
    this.checkUserResponsibility(this.courseId);
  }

  fetchTeachers(): void {
    this.courseTeacherService.getTeachersByCourse(this.courseId).subscribe(
      (teachers) => {
        this.teachers = teachers.map((teacher) => ({
          teacherId: teacher.teacherId,
          responsible: teacher.responsible,
        }));
        // Check if the current user is responsible for the course
        this.isUserResponsible = this.teachers.some((teacher) => teacher.responsible);
      },
      (error) => {
        console.error('Error fetching teachers:', error);
      }
    );
  }

  fetchCourseDates(): void {
    this.courseDateService.getCourseDatesByCourseId(this.courseId).subscribe(
      (courseDates) => {
        this.courseDates = courseDates.map(cd => ({
          id: cd.id,
          name: cd.name,
          dayOfWeek: cd.dayOfWeek,
          startTime: cd.startTime,
          endTime: cd.endTime,
          teacherIds: cd.teacherIds,
          maxLimit: cd.maxLimit,
          currentlyApplied: cd.currentlyApplied, // Added
          location: cd.location // Added
        }));
      },
      (error) => {
        console.error('Error fetching course dates:', error);
      }
    );
  }

  editCourseDate(courseDate: any): void {
    this.isEditingCourse = true;
    this.selectedCourseDate = courseDate;
    const locationValue = LocationEnum[courseDate.location as keyof typeof LocationEnum]; 
    this.addCourseDateForm.setValue({
      name: courseDate.name,
      dayOfWeek: courseDate.dayOfWeek,
      startTime: courseDate.startTime,
      endTime: courseDate.endTime,
      teacherIds: courseDate.teacherIds,
      maxLimit: courseDate.maxLimit,
      location: locationValue// Added location
    });
  }

  onSubmit(): void {
    if (this.addCourseDateForm.invalid) {
      return;
    }

    if (this.isEditingCourse) {
      // Update course date
      this.courseDateService.updateCourseDate(this.selectedCourseDate.id, this.addCourseDateForm.value, this.courseId).subscribe(
        () => {
          this.successMessage = 'Course date updated successfully';
          this.errorMessage = '';
          this.fetchCourseDates();
        },
        () => {
          this.errorMessage = 'Error updating course date';
          this.successMessage = '';
        }
      );
    } else {
      // Add new course date
      console.log(this.addCourseDateForm.value)
      this.courseDateService.addCourseDate(this.addCourseDateForm.value, this.courseId).subscribe(
        () => {
          this.successMessage = 'Course date added successfully';
          this.errorMessage = '';
          this.fetchCourseDates();
        },
        () => {
          this.errorMessage = 'Error adding course date';
          this.successMessage = '';
        }
      );
    }
  }

  removeCourseDate(courseDateId: number): void {
    if (confirm('Are you sure you want to remove this course date?')) {
      this.courseDateService.deleteCourseDate(courseDateId).subscribe({
        next: () => {
          this.courseDates = this.courseDates.filter(cd => cd.id !== courseDateId);
          alert('Course date removed successfully.');
        },
        error: (err) => {
          console.error('Error removing course date:', err);
          alert('Failed to remove course date.');
        }
      });
    }
  }
  checkUserResponsibility(courseId: number): void {
    this.courseTeacherService.getCurrentTeacherByCourseId(courseId).subscribe(
      (response) => {
        this.isUserResponsible = response.responsible; // Set based on API response
      },
      (error) => {
        console.error('Error checking responsibility:', error);
        this.isUserResponsible = false; // Set to false if the call fails
      }
    );
  }
  
  startEditing(teacherId: string, responsible: boolean): void {
    this.isEditing = true;
    this.editingTeacherId = teacherId;
    this.newTeacherId = teacherId;
    this.isResponsible = responsible;
  }

  saveTeacher(): void {
    if (!this.newTeacherId) {
      alert('Please enter a valid teacher ID');
      return;
    }

    const teacherData = {
      teacherId: this.newTeacherId,
      responsible: this.isResponsible,
    };

    if (this.isEditing) {
      // Call update teacher API
      this.courseTeacherService.updateTeacherInCourse(this.courseId, teacherData).subscribe(
        () => {
          alert('Teacher updated successfully!');
          this.resetForm();
          this.fetchTeachers(); // Refresh the teacher list
        },
        (error) => {
          console.error('Error updating teacher:', error);
          alert('Failed to update teacher. Please try again.');
        }
      );
    } else {
      // Call add teacher API
      this.courseTeacherService.addTeacherToCourse(this.courseId, teacherData).subscribe(
        () => {
          alert('Teacher added successfully!');
          this.resetForm();
          this.fetchTeachers();
        },
        (error) => {
          console.error('Error adding teacher:', error);
          alert('Failed to add teacher. Please try again.');
        }
      );
    }
  }

  deleteTeacher(teacherId: string): void {
    if (confirm('Are you sure you want to remove this teacher?')) {
      this.courseTeacherService.removeTeacherFromCourse(this.courseId, teacherId).subscribe(
        () => {
          alert('Teacher removed successfully!');
          this.fetchTeachers();
        },
        (error) => {
          console.error('Error removing teacher:', error);
          alert('Failed to remove teacher. Please try again.');
        }
      );
    }
  }
  resetCourseDateForm() {
    this.isEditingCourse = false;
    this.addCourseDateForm = this.fb.group({
      name: ['', Validators.required],
      dayOfWeek: ['', Validators.required],
      startTime: ['', Validators.required],
      endTime: ['', Validators.required],
      teacherIds: [[], Validators.required],
      maxLimit: [0]
    });
  }
  resetForm(): void {
    this.isEditing = false;
    this.editingTeacherId = null;
    this.newTeacherId = '';
    this.isResponsible = false;
  }  
  getLocationString(locationKey: string): string {
    return LocationEnum[locationKey as keyof typeof LocationEnum] || 'Unknown Location';
  }
  getDayString(dayKey: string): string {
    return DayOfWeek[dayKey as keyof typeof DayOfWeek] || 'Unknown Day';
  }
}
