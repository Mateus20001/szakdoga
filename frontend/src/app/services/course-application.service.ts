import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class CourseApplicationService {
  private apiUrl = 'http://localhost:8080/api/course-applications';

  constructor(private http: HttpClient) {}

  applyToCourse(courseDateId: number): Observable<any> {
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
}
