import { Component } from '@angular/core';
import { CourseApplicationService } from '../services/course-application.service';
import { AppliedCourse } from '../models/AppliedCourseDTO';
import { CommonModule } from '@angular/common';
interface Lesson {
  id: number;
  name: string;
  courseId: number;
  teacherIds: string[];
  dayOfWeek: string;
  startTime: string;
  endTime: string;
  currentlyApplied: number;
  maxLimit: number;
  location: string;
}
@Component({
  selector: 'app-timetable-planner',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './timetable-planner.component.html',
  styleUrl: './timetable-planner.component.scss'
})
export class TimetablePlannerComponent {
  timetableCoursesIds: any[] = [];
  appliedCourses: AppliedCourse[] = [];
  timeTableCourses: Lesson[] = [];
  constructor(private courseApplicationService: CourseApplicationService) {}

  ngOnInit(): void {
    this.loadTimetableCourses();
    this.fetchAppliedCourses();
  }

  loadTimetableCourses(): void {
    this.courseApplicationService.getTimetableCourses().subscribe({
      next: (courses) => {
        this.timetableCoursesIds = courses;
        console.log('Timetable loaded:', this.timetableCoursesIds);
      },
      error: (err) => {
        console.error('Failed to load timetable:', err);
      }

    });
  }
  fetchAppliedCourses(): void {
    this.courseApplicationService.getUserAppliedCourses().subscribe(
      (courses) => {
        this.appliedCourses = courses;
        console.log(this.appliedCourses);
      },
      (error) => {
        console.error('Error fetching applied courses:', error);
      }
    );
  }
  daysOfWeek = ['Monday', 'Tuesday', 'Wednesday', 'Thursday', 'Friday', 'Saturday', 'Sunday'];

  getMinutesSinceMidnight(time: string): number {
    const [hour, minute] = time.split(':').map(Number);
    return hour * 60 + minute;
  }

  getLessonStyle(lesson: Lesson) {
    const start = this.getMinutesSinceMidnight(lesson.startTime);
    const end = this.getMinutesSinceMidnight(lesson.endTime);
    const top = (start / 60) * 60; // 1 hour = 60px
    const height = ((end - start) / 60) * 60;
    const dayIndex = this.daysOfWeek.findIndex(d => d.toUpperCase() === lesson.dayOfWeek);
    const left = `${(dayIndex) * 100}% / 7`; // Even columns

    return {
      top: `${top}px`,
      height: `${height}px`,
      left: `${(dayIndex * 100) / 7}%`,
      width: `${100 / 7}%`
    };
  }

  getHourLabels(): string[] {
    return Array.from({ length: 24 }, (_, i) => `${i.toString().padStart(2, '0')}:00`);
  }
}
