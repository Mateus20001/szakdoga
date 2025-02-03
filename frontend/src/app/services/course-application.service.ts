import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { AppliedCourse } from '../models/AppliedCourseDTO';
import { environment } from '../../environments/environment';

@Injectable({
  providedIn: 'root'
})
export class CourseApplicationService {
  private apiUrl = `${environment.apiUrl}course-applications`;

  constructor(private http: HttpClient) {}

  applyToCourse(courseDateId: number): Observable<any> {
    console.log("ASD")
    const token = localStorage.getItem('loggedInUser');
    const headers = new HttpHeaders({
      Authorization: `Bearer ${token}`
    });
    return this.http.post(`${this.apiUrl}/apply`, {courseDateId}, { headers });
  }

  removeApplication(courseDateId: number): Observable<any> {
    const token = localStorage.getItem('loggedInUser');
    const headers = new HttpHeaders({
      Authorization: `Bearer ${token}`
    });
    return this.http.delete(`${this.apiUrl}/remove/${courseDateId}`, { headers });
  }

  getUserApplications(courseId: number): Observable<any[]> {
    const token = localStorage.getItem('loggedInUser');
    const headers = new HttpHeaders({
      Authorization: `Bearer ${token}`
    });
    return this.http.get<any[]>(`${this.apiUrl}/user-applications/${courseId}`, { headers });
  }
  getUserAppliedCourses(): Observable<AppliedCourse[]> {
    const token = localStorage.getItem('loggedInUser');
    const headers = new HttpHeaders({
      Authorization: `Bearer ${token}`
    });
    return this.http.get<AppliedCourse[]>(`${this.apiUrl}/applied-courses`, { headers });
  }
}
