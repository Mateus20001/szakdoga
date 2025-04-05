import { Injectable } from '@angular/core';
import { environment } from '../../environments/environment';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class MessageService {
  private baseUrl = `${environment.apiUrl}user-messages`;
  constructor(private http: HttpClient) { }
  addMessage(message: { text: string; to: string; }): Observable<any> {
      const authToken = localStorage.getItem("loggedInUser");
      const headers = new HttpHeaders({
            Authorization: `Bearer ${authToken}`,
      });
      return this.http.post<any>(this.baseUrl + '/create-new', message, { headers });
    }
}
