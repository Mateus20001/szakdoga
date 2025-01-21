import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class FacultyService {

  constructor(private http: HttpClient) { }
  private baseUrl = 'http://localhost:8080/api/faculties';
  
  addFaculty(name: string, description: string, image: File): Observable<any> {
    const authToken = localStorage.getItem("loggedInUser");
    const headers = new HttpHeaders({
          Authorization: `Bearer ${authToken}`,
    });
    const formData = new FormData();
    formData.append('name', name);
    formData.append('description', description);
    formData.append('image', image, image.name);

    return this.http.post(this.baseUrl, formData, { headers });
  }
  getFaculties(): Observable<any[]> {
    return this.http.get<any[]>(this.baseUrl);
  }
  getFacultyImage(facultyId: number): Observable<Blob> {
    return this.http.get(`${this.baseUrl}/${facultyId}/image`, { responseType: 'blob' });
  }
}
