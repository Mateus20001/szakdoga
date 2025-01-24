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
@Component({
  selector: 'app-root',
  standalone: true,
  imports: [RouterOutlet, LayoutComponent, HttpClientModule, CommonModule], 
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.scss'],
  providers: [AuthService, MajorService, NewsService, FacultyService, CourseService, CourseTeacherService]
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
