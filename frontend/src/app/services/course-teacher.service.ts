import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class CourseTeacherService {

  private apiUrl = `http://localhost:8080/api/courses`; 

  constructor(private http: HttpClient) {}

  getCoursesByTeacher(): Observable<any[]> {
    const token = localStorage.getItem('loggedInUser'); 
    if (!token) {
      throw new Error('No authentication token found');
    }

    const headers = new HttpHeaders({
      Authorization: `Bearer ${token}`,
    });

    return this.http.get<any[]>(`${this.apiUrl}/by-teacher`, { headers });
  }
  getTeachersByCourse(courseId: number): Observable<any[]> {
    const token = localStorage.getItem('loggedInUser'); 
    if (!token) {
      throw new Error('No authentication token found');
    }

    const headers = new HttpHeaders({
      Authorization: `Bearer ${token}`,
    });

    return this.http.get<any[]>(`${this.apiUrl}/teachers/${courseId}`, { headers });
  }
}
