import { Component, Input } from '@angular/core';
import { CourseDateService } from '../../services/course-date.service';
import { NgClass, NgFor, NgIf, NgStyle } from '@angular/common';
import { MatTooltipModule } from '@angular/material/tooltip';
import { MatSnackBar } from '@angular/material/snack-bar';
import { MatButton } from '@angular/material/button';
import { CourseApplicationService } from '../../services/course-application.service';
import { ConfirmDialogComponent } from './confirm-dialog/confirm-dialog.component';
import { MatDialog } from '@angular/material/dialog';
import { LocationEnum } from '../../models/LocationEnum';
import { DayOfWeek } from '../../models/DayOfWeek';
import { AuthService } from '../../auth/auth.service';
import { UserShowDTO } from '../../models/user';
import { MessageWritingComponent } from '../../message-writing/message-writing.component';
import { MessageService } from '../../services/message.service';
import { Lesson } from '../../timetable-planner/timetable-planner.component';

@Component({
  selector: 'app-course-details',
  standalone: true,
  imports: [NgFor, NgIf, MatTooltipModule, MatButton, NgStyle],
  templateUrl: './course-details.component.html',
  styleUrl: './course-details.component.scss'
})
export class CourseDetailsComponent {
  @Input() courseId!: number;
  @Input() semester!: string;
  @Input() enrollment!: string;
  teacherDetails: UserShowDTO | null = null;
  selectedTeacherId: string | null = null;
  hoveringTooltip = false;
  tooltipPosition = { top: 0, left: 0 };
  timetableCoursesLoaded = false;
  allUsers: any[] = [];
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
    semester: string;
  }[] = [];
  userApplications: Set<number> = new Set();
  timeTableCourses: Lesson[] = [];
  constructor(private courseDateService: CourseDateService, private snackBar: MatSnackBar, private courseApplicationService: CourseApplicationService,
    private dialog: MatDialog, private authService: AuthService, private messageService: MessageService
  ) {}
  ngOnInit(): void {
    this.fetchCourseDates();
    this.fetchUsers();
    this.loadUserApplications();
    this.loadTimetableCourses();
  }

  isInTimetable(courseId: number): boolean {
    return this.timeTableCourses.some(lesson => lesson.id === courseId);
  }
  fetchCourseDates(): void {
    this.courseDateService.getCourseDatesByCourseIdSemesterEnrollment(this.courseId, this.semester, this.enrollment).subscribe(
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
            location: cd.location,
            semester: cd.semester
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
  getTeacherDetails(teacherId: string, event: MouseEvent): void {
    this.authService.getUserPublicDetails(teacherId).subscribe(
      (teacher: UserShowDTO) => {
        this.teacherDetails = teacher;
        this.selectedTeacherId = teacher.identifier;
        this.hoveringTooltip = true;
        const rect = (event?.target as HTMLElement).getBoundingClientRect();
        this.tooltipPosition = {
          top: rect.top - 10,  // or adjust to your liking
          left: rect.left,
        };
        console.log(teacher)
      },
      (error) => {
        console.error('Error fetching user:', error);
      }
    );
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
  onMouseLeave() {
    setTimeout(() => {
      if (!this.hoveringTooltip) {
        this.teacherDetails = null;
        this.selectedTeacherId = null;
      }
      this.hoveringTooltip = false;
    }, 200);
  }
  fetchUsers() {
    this.authService.getUserListing().subscribe(
      users => {
        this.allUsers = users;
      },
      error => {
        console.error('Error fetching user listing', error);
      }
    );
  }
  openMessageWritingDialog() {
    const dialogRef = this.dialog.open(MessageWritingComponent, {
      width: '1000px',  // You can customize the width and other properties
      data: {
        teacherId: this.selectedTeacherId,
        users: this.allUsers,
        messageService: this.messageService
      }
    });

    dialogRef.afterClosed().subscribe(result => {
      console.log('The dialog was closed');
      // You can access the result here if needed
    });
  }
  addToTimetable(courseDateId: number): void {
    this.courseApplicationService.addTimetableCourse(courseDateId).subscribe({
      next: () => {
        this.snackBar.open('Course added to timetable!', 'Close', { duration: 3000 });
      },
      error: (err) => {
        console.error('Failed to add to timetable:', err);
        this.snackBar.open('Failed to add course to timetable.', 'Close', { duration: 3000 });
      }
    });
  }
  loadTimetableCourses(): void {
    this.courseApplicationService.getTimetableCourses().subscribe({
      next: (courses) => {
        console.log(courses)
        this.timeTableCourses = courses;
        this.timetableCoursesLoaded = true;
        console.log( this.timeTableCourses)
      },
      error: (err) => {
        console.error('Failed to load timetable:', err);
      }
    });
  }
  removeFromTimetable(arg0: number) {
    this.courseApplicationService.removeTimetableCourse(arg0).subscribe({
      next: () => {
        this.snackBar.open('Course removed from the timetable!', 'Close', { duration: 3000 });
        this.timeTableCourses = this.timeTableCourses.filter(course => course.id !== arg0);
      },
      error: (err) => {
        console.error('Failed to add to timetable:', err);
        this.snackBar.open('Failed to remove course to timetable.', 'Close', { duration: 3000 });
      }
    });
  }
}
