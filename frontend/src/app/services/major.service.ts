import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Router } from '@angular/router';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class MajorService {
  private baseUrl = 'http://localhost:8080/api/majors';

  constructor(private router: Router, private http: HttpClient) {}

  getMajors(): Observable<{ id: string; name: string }[]> {
    return this.http.get<{ id: string; name: string }[]>(this.baseUrl); 
  }
  getmajorNameAndDescription(): Observable<{ id: string; name: string; description: string; facultyName: string }[]> {
    return this.http.get<{ id: string; name: string; description: string; facultyName: string }[]>(this.baseUrl + "/descriptions");
  }
}
