import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { environment } from '../../environments/environment';
import { GlobalSettings } from '../global-settings-changer/global-settings-changer.component';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class GlobalSettingsService {

  private baseUrl = `${environment.apiUrl}global-settings`;
  constructor(private http: HttpClient) { }

  getSettings(): Observable<GlobalSettings[]> {
    return this.http.get<GlobalSettings[]>(this.baseUrl);
  }

  updateSetting(setting: GlobalSettings): Observable<void> {
    return this.http.put<void>(`${this.baseUrl}/${setting.id}`, setting);
  }
}
