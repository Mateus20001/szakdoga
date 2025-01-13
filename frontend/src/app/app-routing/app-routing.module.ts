import {Route } from '@angular/router';
import { MainPageComponent } from '../main-page/main-page.component';
import { DashboardComponent } from '../dashboard/dashboard.component';
import { LoginComponent } from '../login/login.component';
import { authGuard } from '../guards/auth.guard';
import { ContactsComponent } from '../contacts/contacts.component';


export const routes: Route[] = [
  { path: 'main', component: MainPageComponent, canActivate: [authGuard]},
  { path: 'dashboard', component: DashboardComponent},
  { path: 'login', component: LoginComponent},
  {path: 'contacts', component: ContactsComponent, canActivate: [authGuard]},
  { path: '**', redirectTo: 'login' }
];
