import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Observable, throwError } from 'rxjs';
import { catchError } from 'rxjs/operators';
import { Router } from '@angular/router';
import { User } from '../models/user';
import { SignupDTO } from '../models/signupDTO';
import { UserSessionEntity } from '../models/userSessionEntity';
import { UserDetailsDTO } from '../models/userDetailsDTO';

@Injectable({
  providedIn: 'root',
})
export class AuthService {
  private baseUrl = 'http://localhost:8080/api/users';

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
}
