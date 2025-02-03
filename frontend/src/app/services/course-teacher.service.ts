import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { environment } from '../../environments/environment';

@Injectable({
  providedIn: 'root'
})
export class CourseTeacherService {

  private apiUrl = `${environment.apiUrl}courses`; 

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
  addTeacherToCourse(courseId: number, teacher: { teacherId: string, responsible: boolean }): Observable<void> {
    const token = localStorage.getItem('loggedInUser'); 
    if (!token) {
      throw new Error('No authentication token found');
    }
  
    const headers = new HttpHeaders({
      Authorization: `Bearer ${token}`,
    });
  
    return this.http.post<void>(`${this.apiUrl}/teachers/${courseId}`, teacher, { headers });
  }
  updateTeacherInCourse(courseId: number, teacher: { teacherId: string, responsible: boolean }): Observable<void> {
    const token = localStorage.getItem('loggedInUser');
    if (!token) {
      throw new Error('No authentication token found');
    }
  
    const headers = new HttpHeaders({
      Authorization: `Bearer ${token}`,
    });
  
    return this.http.put<void>(`${this.apiUrl}/teachers/${courseId}`, teacher, { headers });
  }
  
  removeTeacherFromCourse(courseId: number, teacherId: string): Observable<void> {
    const token = localStorage.getItem('loggedInUser');
    if (!token) {
      throw new Error('No authentication token found');
    }
  
    const headers = new HttpHeaders({
      Authorization: `Bearer ${token}`,
    });
  
    return this.http.delete<void>(`${this.apiUrl}/teachers/${courseId}/${teacherId}`, { headers });
  }
  
  getCurrentTeacherByCourseId(courseId: number): Observable<any> {
    const token = localStorage.getItem('loggedInUser');
    const headers = new HttpHeaders({
      Authorization: `Bearer ${token}`,
      'Content-Type': 'application/json',
    });
  
    return this.http.get<any>(`${this.apiUrl}/current-teacher/${courseId}`, { headers });
  }
}
