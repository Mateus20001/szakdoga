import { Injectable } from '@angular/core';
import { environment } from '../../environments/environment';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Observable } from 'rxjs';
import { AddExamRequest } from '../models/AddExamRequest';
import { AppliedExamResponse } from '../models/AppliedExamResponse';

@Injectable({
  providedIn: 'root'
})
export class ExamService {

  private apiUrl = `${environment.apiUrl}exams`
  
  constructor(private http: HttpClient) { }

  getRelevantExams(): Observable<any[]> {
      const authToken = localStorage.getItem("loggedInUser");
              const headers = new HttpHeaders({
                    Authorization: `Bearer ${authToken}`,
              });
      return this.http.get<any[]>(`${this.apiUrl}`, { headers });
  }
  addExam(request: AddExamRequest): Observable<any> {
      const authToken = localStorage.getItem("loggedInUser");
      const headers = new HttpHeaders({
            'Content-Type': 'application/json',
            Authorization: `Bearer ${authToken}`,
            
      });
      return this.http.post<any>(`${this.apiUrl}/add`, request, { headers });
  }
  applyToExam(examId: number): Observable<string> {
    const authToken = localStorage.getItem("loggedInUser"); // JWT token
    const headers = new HttpHeaders({
      Authorization: `Bearer ${authToken}`,
    });

    return this.http.post(`${this.apiUrl}/${examId}/apply`, null, {
      headers,
      responseType: 'text',
    });
  }
  removeApplication(examId: number): Observable<string> {
    const authToken = localStorage.getItem("loggedInUser"); // JWT token
    const headers = new HttpHeaders({
      Authorization: `Bearer ${authToken}`,
    });

    return this.http.delete(`${this.apiUrl}/${examId}/remove`, { headers, responseType: 'text' });
  }
  getAllAppliedExams(): Observable<AppliedExamResponse[]> {
    const authToken = localStorage.getItem('loggedInUser');
    const headers = new HttpHeaders({
      Authorization: `Bearer ${authToken}`
    });
    return this.http.get<AppliedExamResponse[]>(`${this.apiUrl}/me`, { headers });
  }
}
