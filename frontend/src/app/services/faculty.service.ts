import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class FacultyService {

  constructor(private http: HttpClient) { }
  private baseUrl = 'http://localhost:8080/api/faculties';
  
  addFaculty(name: string, description: string, image: File): Observable<any> {
    const formData = new FormData();
    formData.append('name', name);
    formData.append('description', description);
    formData.append('image', image, image.name);

    return this.http.post(this.baseUrl, formData);
  }
}
