import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { Quote } from '../models/Quote';
import { environment } from '../../environments/environment';

@Injectable({
  providedIn: 'root'
})
export class QuoteService {
  private apiUrl =`${environment.apiUrl}quote`;
  
  constructor(private http: HttpClient) {}

  getDailyQuote(): Observable<Quote[]> {
    return this.http.get<Quote[]>(`${this.apiUrl}/daily-quote`);
  }
}
