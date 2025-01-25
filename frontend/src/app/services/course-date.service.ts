import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class CourseDateService {
  private apiUrl = 'http://localhost:8080/api/course-dates'; // Replace with your actual endpoint

  constructor(private http: HttpClient) {}

  addCourseDate(courseDate: any, courseId: number): Observable<any> {
    const token = localStorage.getItem('loggedInUser'); // Assuming you're using a token for auth
    const headers = new HttpHeaders({
      Authorization: `Bearer ${token}`,
      'Content-Type': 'application/json',
    });
    const payload = {
      courseId,
      ...courseDate 
    };
    return this.http.post<any>(
      `${this.apiUrl}`,
      payload,
      { headers }
    );
  }
  updateCourseDate(id: number, courseDate: any, courseId: number): Observable<any> {
    const token = localStorage.getItem('loggedInUser');
    const headers = new HttpHeaders({
      Authorization: `Bearer ${token}`,
      'Content-Type': 'application/json',
    });
    const payload = {
      id,
      courseId,
      ...courseDate 
    };
    return this.http.put<any>(
      `${this.apiUrl}`, 
      payload,
      { headers }
    );
  }

  getCourseDatesByCourseId(courseId: number): Observable<any[]> {
    const token = localStorage.getItem('loggedInUser'); // Assuming you're using a token for auth
    const headers = new HttpHeaders({
      Authorization: `Bearer ${token}`,
    });

    return this.http.get<any[]>(`${this.apiUrl}/${courseId}/all`, { headers });
  }
  deleteCourseDate(courseDateId: number): Observable<void> {
    const token = localStorage.getItem('loggedInUser');
    const headers = new HttpHeaders({
      Authorization: `Bearer ${token}`,
      'Content-Type': 'application/json',
    });

    return this.http.delete<void>(`${this.apiUrl}/${courseDateId}`, { headers });
  }
}