import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class NewsService {
  private apiUrl = 'http://localhost:8080/api/news';

  constructor(private http: HttpClient) {}

  getNews(): Observable<any[]> {
    return this.http.get<any[]>(this.apiUrl);
  }
  getImage(id: number): Observable<Blob> {
    return this.http.get(`${this.apiUrl}/image/${id}`, { responseType: 'blob' });
  }
  addNews(formData: FormData): Observable<any> {
    return this.http.post(this.apiUrl, formData);
  }
}
