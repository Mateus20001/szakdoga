import {Route } from '@angular/router';
import { MainPageComponent } from '../main-page/main-page.component';
import { DashboardComponent } from '../dashboard/dashboard.component';
import { LoginComponent } from '../login/login.component';
import { authGuard } from '../guards/auth.guard';
import { ContactsComponent } from '../contacts/contacts.component';
import { ProfileComponent } from '../profile/profile.component';
import { AddProfileComponent } from '../add-profile/add-profile.component';
import { adminGuard } from '../guards/admin.guard';
import { MajorsComponent } from '../majors/majors.component';
import { AddnewsComponent } from '../addnews/addnews.component';
import { NewsComponent } from '../news/news.component';
import { AddFacultyComponent } from '../add-faculty/add-faculty.component';
import { FacultiesComponent } from '../faculties/faculties.component';
import { AddCourseComponent } from '../add-course/add-course.component';
import { CoursesComponent } from '../courses/courses.component';
import { EditCourseComponent } from '../edit-course/edit-course.component';
import { ChangeUsernameComponent } from '../profile/change-username/change-username.component';
import { ChangePasswordComponent } from '../profile/change-password/change-password.component';
import { TeachedCoursesComponent } from '../teached-courses/teached-courses.component';
import { EditTeacherCourseComponent } from '../edit-teacher-course/edit-teacher-course.component';
import { CourseApplicationComponent } from '../course-application/course-application.component';
import { AppliedCoursesComponent } from '../applied-courses/applied-courses.component';
import { StudentStatisticComponent } from '../student-statistic/student-statistic.component';
import { GradingStudentsComponent } from '../grading-students/grading-students.component';
import { TimetablePlannerComponent } from '../timetable-planner/timetable-planner.component';


export const routes: Route[] = [
  { path: 'main', component: MainPageComponent, canActivate: [authGuard]},
  { path: 'dashboard', component: DashboardComponent},
  { path: 'login', component: LoginComponent},
  { path: 'contacts', component: ContactsComponent, canActivate: [authGuard]},
  { path: 'profile', component: ProfileComponent, canActivate: [authGuard]},
  { path: 'aboutMajors', component: MajorsComponent, canActivate: [authGuard]},
  { path: 'addProfile', component: AddProfileComponent, canActivate: [authGuard, adminGuard]},
  { path: 'addNews', component: AddnewsComponent, canActivate: [authGuard, adminGuard]},
  { path: 'addFaculties', component: AddFacultyComponent, canActivate: [authGuard, adminGuard]},
  { path: 'news', component: NewsComponent, canActivate: [authGuard]},
  { path: 'aboutFaculties', component: FacultiesComponent, canActivate: [authGuard]},
  { path: 'addCourse', component: AddCourseComponent, canActivate: [authGuard]},
  { path: 'courses', component: CoursesComponent, canActivate: [authGuard]},
  { path: 'teachedCourses', component: TeachedCoursesComponent, canActivate: [authGuard]},
  { path: 'edit-course/:id', component: EditCourseComponent, canActivate: [authGuard]},
  { path: 'edit-teacher-course/:id', component: EditTeacherCourseComponent, canActivate: [authGuard]},
  { path: 'changeUsername', component: ChangeUsernameComponent, canActivate: [authGuard]},
  { path: 'changePassword', component: ChangePasswordComponent, canActivate: [authGuard]},
  { path: 'courseApplication', component: CourseApplicationComponent, canActivate: [authGuard]},
  { path: 'appliedCourses', component: AppliedCoursesComponent, canActivate: [authGuard]},
  { path: 'studentStatistic', component: StudentStatisticComponent, canActivate: [authGuard]},
  { path: 'gradingStudents', component: GradingStudentsComponent, canActivate: [authGuard]},
  { path: 'timetable-planner', component: TimetablePlannerComponent, canActivate: [authGuard]},
  { path: '**', redirectTo: 'login' }
];
