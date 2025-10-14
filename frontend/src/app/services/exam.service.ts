import { Injectable } from '@angular/core';
import { environment } from '../../environments/environment';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Observable } from 'rxjs';
import { AddExamRequest } from '../models/AddExamRequest';

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
}
