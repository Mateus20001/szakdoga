import { Component } from '@angular/core';
import { LayoutComponent } from "./layout/layout.component";
import { HttpClientModule } from '@angular/common/http';
import { AuthService } from './auth/auth.service';
import { Router, RouterOutlet } from '@angular/router';
import { CommonModule } from '@angular/common';
import { MajorService } from './services/major.service';
import { NewsService } from './services/news.service';
import { FacultyService } from './services/faculty.service';
import { CourseService } from './services/course.service';
import { CourseTeacherService } from './services/course-teacher.service';
import { CourseDateService } from './services/course-date.service';
import { CourseApplicationService } from './services/course-application.service';
import { GradingService } from './services/grading.service';
import { MessageService } from './services/message.service';
import { CalendarModule, CalendarUtils, DateAdapter } from 'angular-calendar';

import { adapterFactory } from 'angular-calendar/date-adapters/date-fns';
import { GlobalSettingsService } from './services/global-settings.service';
import { QuoteService } from './services/quoteservice';
import { ExamService } from './services/exam.service';
@Component({
  selector: 'app-root',
  standalone: true,
  imports: [RouterOutlet, LayoutComponent, HttpClientModule, CommonModule], 
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.scss'],
  providers: [QuoteService, GlobalSettingsService,CalendarUtils, AuthService, MessageService, MajorService, NewsService, FacultyService, CourseService, CourseTeacherService, CourseDateService, CourseApplicationService, GradingService, ExamService]
})
export class AppComponent {
  title = 'frontend';
  constructor(public router: Router, private authService: AuthService) {};
  isLoginPage(): boolean {
    return this.router.url === '/login';
  }
  ngOnInit() {
    setInterval(() => this.authService.checkTokenExpiration(), 10000); // Check every minute
  }
}
