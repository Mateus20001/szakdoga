import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Router } from '@angular/router';
import { Observable } from 'rxjs';
import { MajorWithFacultyDTO } from '../models/majorWithFacultyDTO';

@Injectable({
  providedIn: 'root'
})
export class MajorService {
  private baseUrl = 'http://localhost:8080/api/majors';

  constructor(private router: Router, private http: HttpClient) {}

  getMajors(): Observable<{ id: string; name: string }[]> {
    return this.http.get<{ id: string; name: string }[]>(this.baseUrl); 
  }
  getMajorNamesAndDescriptions(): Observable<MajorWithFacultyDTO[]> {
    return this.http.get<MajorWithFacultyDTO[]>(`${this.baseUrl}/descriptions`);
  }
  addMajor(major: { name: string; description: string; facultyId: string }): Observable<any> {
    const authToken = localStorage.getItem("loggedInUser");
    const headers = new HttpHeaders({
          Authorization: `Bearer ${authToken}`,
    });
    return this.http.post<any>(this.baseUrl + '/add', major, { headers });
  }
}
