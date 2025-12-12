import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Observable, throwError } from 'rxjs';
import { catchError } from 'rxjs/operators';
import { Router } from '@angular/router';
import { User, UserShowDTO } from '../models/user';
import { SignupDTO } from '../models/signupDTO';
import { UserSessionEntity } from '../models/userSessionEntity';
import { UserDetailsDTO } from '../models/userDetailsDTO';
import { MessageDto } from '../models/email';
import { environment } from '../../environments/environment';
import { UserListingDTO } from '../models/TeacherStudentGradingDTO';
import { Quote } from '../models/Quote';

@Injectable({
  providedIn: 'root',
})
export class AuthService {
  private baseUrl = `${environment.apiUrl}users`;

  constructor(private router: Router, private http: HttpClient) {}

  getAllUsers(): Observable<User[]> {
    return this.http.get<User[]>(`${this.baseUrl}`, {
      headers: new HttpHeaders({
        
      }),
    }).pipe(catchError(this.handleError));
  }

  login(user: User): Observable<any> {
    if (user && user.id && user.password) {
      return this.http.post(`${this.baseUrl}/login`, user, {
        headers: new HttpHeaders({
          'Content-Type': 'application/json',
        }),
      }).pipe(catchError(this.handleError));
    } else {
      return throwError("Invalid user input");
    }
  }
  signup(signupData: SignupDTO): Observable<any> {
    return this.http.post<any>(`${this.baseUrl}/signup`, signupData);
  }
  private handleError(error: any) {
    console.error('An error occurred:', error);
    return throwError(error);
  }

  logout() {
    localStorage.removeItem('loggedInUser');
    localStorage.removeItem('expirationTime');
    this.router.navigate(['/login']);
  }


  getContacts(authToken: String) {
    const headers = new HttpHeaders({
      'Content-Type': 'application/json',
      Authorization: `Bearer ${authToken}`,
    });

    return this.http.get<User>(`${this.baseUrl}/contacts`, { headers }).pipe(
      catchError(this.handleError)
    );
  }

  getUserNameAndRoles(authToken: string | null): Observable<UserSessionEntity> {
  if (!authToken) {
    throw new Error('No authentication token provided');
  }

  const headers = new HttpHeaders({
    'Content-Type': 'application/json',
    Authorization: `Bearer ${authToken}`,
  });

  return this.http.get<UserSessionEntity>(`${this.baseUrl}/me`, { headers }).pipe(
    catchError((error) => {
      console.error('Error fetching user info', error);
      return throwError(() => new Error('Failed to fetch user info'));
    })
  );
}

  saveUser(authToken: string | null, user: any) {
    const headers = new HttpHeaders({
      'Content-Type': 'application/json',
      Authorization: `Bearer ${authToken}`,
    });
  
    return this.http.post(`${this.baseUrl}/save`, user, { headers });
  }
  
  getUserDetails(authToken: string | null): Observable<UserDetailsDTO> {
    if (!authToken) {
      throw new Error('No authentication token provided');
    }

    const headers = new HttpHeaders({
      'Content-Type': 'application/json',
      Authorization: `Bearer ${authToken}`,
    });

    return this.http.get<UserDetailsDTO>(`${this.baseUrl}/me/details`, { headers }).pipe(
      catchError((error) => {
        console.error('Error fetching user details', error);
        return throwError(() => new Error('Failed to fetch user details'));
      })
    );
  }
  isAuthenticated(): boolean {
    const expirationTime = localStorage.getItem('expirationTime');
    return expirationTime ? Date.now() < parseInt(expirationTime) : false;
  }
  checkTokenExpiration() {
    if (!this.isAuthenticated()) {
      this.logout();
    }
  }
  getFirstLoginStatus(authToken: string | null): Observable<boolean> {
    if (!authToken) {
      throw new Error('No authentication token provided');
    }
  
    const headers = new HttpHeaders({
      'Content-Type': 'application/json',
      Authorization: `Bearer ${authToken}`,
    });
  
    return this.http.get<boolean>(`${this.baseUrl}/me/first-login`, { headers }).pipe(
      catchError((error) => {
        console.error('Error fetching first login status', error);
        return throwError(() => new Error('Failed to fetch first login status'));
      })
    );
  }

  changePassword(authToken: string | null, newPassword: string, confirmPassword: string): Observable<void> {
    if (!authToken) {
      throw new Error('No authentication token provided');
    }
  
    const headers = new HttpHeaders({
      'Content-Type': 'application/json',
      Authorization: `Bearer ${authToken}`,
    });
  
    return this.http.post<void>(
      `${this.baseUrl}/me/change-password`,
      { newPassword, confirmPassword },
      { headers }
    ).pipe(
      catchError((error) => {
        console.error('Error changing password', error);
        return throwError(() => new Error('Failed to change password'));
      })
    );
  }
  
  changeUsername(authToken: string, newUsername: string): Observable<void> {
    const headers = new HttpHeaders({
      'Content-Type': 'application/json',
      Authorization: `Bearer ${authToken}`,
    });
  
    return this.http.post<void>(`${this.baseUrl}/me/change-username`, { newUsername }, { headers }).pipe(
      catchError((error) => {
        console.error('Error changing username:', error);
        return throwError(() => new Error('Failed to change username'));
      })
    );
  }
  getTeachers(authToken: string): Observable<any[]> {
    const headers = new HttpHeaders({
      'Content-Type': 'application/json',
      Authorization: `Bearer ${authToken}`,
    });
    return this.http.get<any[]>(`${this.baseUrl}/teachers`, { headers });
  }

  getEmails(): Observable<any[]> {
    const authToken = localStorage.getItem("loggedInUser");
    const headers = new HttpHeaders({
      'Content-Type': 'application/json',
      Authorization: `Bearer ${authToken}`,
    });
    return this.http.get<any[]>(`${this.baseUrl}/me/emails`, { headers });
  }
  getUserMessages(): Observable<MessageDto[]> {
    const token = localStorage.getItem('loggedInUser');  // Assuming the token is saved in localStorage
    const headers = new HttpHeaders({
      'Authorization': `Bearer ${token}`  // Include token in Authorization header
    });
    return this.http.get<MessageDto[]>(`${environment.apiUrl}user-messages/me/notifications`, { headers });
  }
  getUserPublicDetails(teacherId: string): Observable<UserShowDTO> {
    const token = localStorage.getItem('loggedInUser');  // Assuming the token is saved in localStorage
    const headers = new HttpHeaders({
      'Authorization': `Bearer ${token}`  // Include token in Authorization header
    });
    return this.http.get<UserShowDTO>(`${this.baseUrl}/${teacherId}`, { headers });
  }
  getUserListing(): Observable<UserListingDTO[]> {
    const token = localStorage.getItem('loggedInUser');  // Assuming the token is saved in localStorage
    const headers = new HttpHeaders({
      'Authorization': `Bearer ${token}`  // Include token in Authorization header
    });
    return this.http.get<UserListingDTO[]>(`${this.baseUrl}/all-users-listing`, { headers });
  }
}
