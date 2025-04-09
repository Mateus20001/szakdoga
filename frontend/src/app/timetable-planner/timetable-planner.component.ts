import { Component } from '@angular/core';
import { CourseApplicationService } from '../services/course-application.service';
import { AppliedCourse } from '../models/AppliedCourseDTO';
import { CommonModule } from '@angular/common';
import { CalendarView, CalendarEvent, CalendarModule } from 'angular-calendar';
import { startOfWeek, addHours, parseISO, setHours, setMinutes } from 'date-fns';
import { MatDialogModule, MatDialog } from '@angular/material/dialog';
import { EventDetailsDialogComponent } from './event-details-dialog/event-details-dialog.component';

export interface Lesson {
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
  imports: [CommonModule, CalendarModule, MatDialogModule],
  templateUrl: './timetable-planner.component.html',
  styleUrl: './timetable-planner.component.scss'
})
export class TimetablePlannerComponent {
  selectedEvent: CalendarEvent | null = null;
  hourFormat: string = "";
  view: CalendarView = CalendarView.Week;
  viewDate: Date = new Date(); // Your current view date
  CalendarView = CalendarView;
  timetableCoursesIds: any[] = [];
  appliedCourses: AppliedCourse[] = [];
  timeTableCourses: Lesson[] = [];
  constructor(private courseApplicationService: CourseApplicationService, private dialog: MatDialog) {}
  events: CalendarEvent[] = [
  ];
  ngOnInit(): void {
    this.loadTimetableCourses();
  }

  convertLessonsToEvents(lessons: Lesson[]): CalendarEvent[] {
    console.log(lessons);
    return lessons.map((lesson) => ({
      title: `${lesson.name}`,
      start: this.getDateTime(lesson.dayOfWeek, lesson.startTime),
      end: this.getDateTime(lesson.dayOfWeek, lesson.endTime),
      meta: {
        location: lesson.location,
        teacherIds: lesson.teacherIds
      }
    }));
  }
  
  loadTimetableCourses(): void {
    this.courseApplicationService.getTimetableCourses().subscribe({
      next: (courses) => {
        this.timeTableCourses = courses;
        this.events = this.convertLessonsToEvents(courses); // 👈 map lessons to calendar
        console.log(this.events)
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
  getDateTime(day: string, time: string): Date {
    const days = ['MONDAY', 'TUESDAY', 'WEDNESDAY', 'THURSDAY', 'FRIDAY', 'SATURDAY', 'SUNDAY'];
    const base = startOfWeek(new Date(), { weekStartsOn: 1 });
    const dayIndex = days.indexOf(day.toUpperCase());

    const [hour, minute] = time.split(':').map(Number);
    const date = addHours(base, dayIndex * 24);
    return setMinutes(setHours(date, hour), minute);
  }
  handleEventClick(event: CalendarEvent): void {
    this.dialog.open(EventDetailsDialogComponent, {
      data: event,
      width: '400px'
    });
  }
}
