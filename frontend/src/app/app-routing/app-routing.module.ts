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
  { path: '**', redirectTo: 'login' }
];
