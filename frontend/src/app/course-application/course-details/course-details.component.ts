import { Component, Input } from '@angular/core';
import { CourseDateService } from '../../services/course-date.service';
import { NgFor, NgIf } from '@angular/common';
import { MatTooltipModule } from '@angular/material/tooltip';
import { MatSnackBar } from '@angular/material/snack-bar';
import { MatButton } from '@angular/material/button';
import { CourseApplicationService } from '../../services/course-application.service';
import { ConfirmDialogComponent } from './confirm-dialog/confirm-dialog.component';
import { MatDialog } from '@angular/material/dialog';
import { LocationEnum } from '../../models/LocationEnum';
import { DayOfWeek } from '../../models/DayOfWeek';

@Component({
  selector: 'app-course-details',
  standalone: true,
  imports: [NgFor, NgIf, MatTooltipModule, MatButton],
  templateUrl: './course-details.component.html',
  styleUrl: './course-details.component.scss'
})
export class CourseDetailsComponent {
  @Input() courseId!: number;
  
  constructor(private courseDateService: CourseDateService, private snackBar: MatSnackBar, private courseApplicationService: CourseApplicationService,
    private dialog: MatDialog
  ) {}
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
  userApplications: Set<number> = new Set();

  ngOnInit(): void {
    this.fetchCourseDates();
    this.loadUserApplications();
    console.log(this.userApplications)
  }


  fetchCourseDates(): void {
    this.courseDateService.getCourseDatesByCourseId(this.courseId).subscribe(
      (courseDates) => {
        if (courseDates && courseDates.length > 0) {
          this.courseDates = courseDates.map(cd => ({
            id: cd.id,
            name: cd.name,
            dayOfWeek: cd.dayOfWeek,
            startTime: cd.startTime,
            endTime: cd.endTime,
            teacherIds: cd.teacherIds,
            maxLimit: cd.maxLimit,
            currentlyApplied: cd.currentlyApplied,
            location: cd.location
          }));
        } else {
          this.courseDates = [];
        }
      },
      (error) => {
        console.error('Error fetching course dates:', error);
      }
    );
  }
  private teacherDetails: { [key: string]: string } = {
    "2CDRIC": "John Doe - Math Department, 5 years of experience.",
    "2": "Jane Smith - Science Department, 10 years of experience.",
    // Add more teacher details as needed
  };
  getTeacherDetails(teacherId: string): string {
    return this.teacherDetails[teacherId] || 'No details available for this teacher.';
  }
  loadUserApplications(): void {
    this.courseApplicationService.getUserApplications(this.courseId).subscribe(
      (applications) => {
        applications.forEach((application) => {
          this.userApplications.add(application.courseDateId); 
        });
      },
      (error) => {
        console.error('Error fetching user applications:', error);
      }
    );
  }
  openConfirmationDialog(courseDateId: number, isRemoving: boolean): void {
    const dialogRef = this.dialog.open(ConfirmDialogComponent);

    dialogRef.afterClosed().subscribe(result => {
      if (result) {
        if (isRemoving) {
          this.removeCourse(courseDateId);
        } else {
          this.applyToCourse(courseDateId);
        }
      }
    });
  }

  // Method to apply to course
  applyToCourse(courseDateId: number): void {
    this.courseApplicationService.applyToCourse(courseDateId).subscribe(
      (response) => {
        const userApplicationsList = [...this.userApplications];
          if (userApplicationsList.length > 0) {
            const firstAppliedCourseDateId = userApplicationsList[0];
            console.log(firstAppliedCourseDateId);
            const courseDate = this.courseDates.find(date => date.id === firstAppliedCourseDateId);
            if (courseDate) {
              courseDate.currentlyApplied -= 1;
            }
            this.userApplications.delete(firstAppliedCourseDateId);
          }
        const successMessage = response.message;
        const courseDate = this.courseDates.find(date => date.id === courseDateId);
        if (courseDate) {
          courseDate.currentlyApplied += 1;
        }
        this.userApplications.add(courseDateId);
        this.snackBar.open(successMessage, 'Close', { duration: 3000 });
      },
      (error) => {
        const errorMessage = error.error?.message || 'Error applying to course!';
        console.error('Error applying to course:', error);
        this.snackBar.open(errorMessage, 'Close', { duration: 3000 });
      }
    );
  }

  // Method to remove course application
  removeCourse(courseDateId: number): void {
    this.courseApplicationService.removeApplication(courseDateId).subscribe(
      () => {
        const courseDate = this.courseDates.find(date => date.id === courseDateId);
        if (courseDate) {
          courseDate.currentlyApplied -= 1;
        }
        this.userApplications.delete(courseDateId);
        this.snackBar.open('Successfully removed the course!', 'Close', { duration: 3000 });
      },
      (error) => {
        console.error('Error removing application:', error);
        this.snackBar.open('Error removing course application!', 'Close', { duration: 3000 });
      }
    );
  }
  onApplyRemoveClick(courseDateId: number): void {
    if (this.userApplications.has(courseDateId)) {
      this.openConfirmationDialog(courseDateId, true);  // Confirm removal
    } else {
      this.openConfirmationDialog(courseDateId, false); // Confirm apply
    }
  }
  getLocationString(locationKey: string): string {
      return LocationEnum[locationKey as keyof typeof LocationEnum] || 'Unknown Location';
  }
  getDayString(dayKey: string): string {
    return DayOfWeek[dayKey as keyof typeof DayOfWeek] || 'Unknown Day';
  }
}
