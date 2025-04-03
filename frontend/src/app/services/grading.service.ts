import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { TeacherStudentGradingDTO } from '../models/TeacherStudentGradingDTO';
import { environment } from '../../environments/environment';

@Injectable({
  providedIn: 'root'
})
export class GradingService {
  private apiUrl = `${environment.apiUrl}grading`;

  constructor(private http: HttpClient) {}

  getGradingData(): Observable<TeacherStudentGradingDTO[]> {
    const authToken = localStorage.getItem("loggedInUser");
        const headers = new HttpHeaders({
              Authorization: `Bearer ${authToken}`,
        });
    return this.http.get<TeacherStudentGradingDTO[]>(`${this.apiUrl}/grades`, { headers });
  }
  saveGrades(grades: { identifier: string; courseDateId: number; gradeValue: number }[]): Observable<any> {
    const authToken = localStorage.getItem("loggedInUser");
        const headers = new HttpHeaders({
              Authorization: `Bearer ${authToken}`,
        });
    return this.http.post(`${this.apiUrl}/save`, grades, { headers });
  }
}
