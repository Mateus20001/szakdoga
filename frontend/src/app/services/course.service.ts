import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { CourseDetailDTO, CourseDetailListingDTO, CourseDetailStudentListingDTO } from '../models/CourseDetailListingDTO';
import { environment } from '../../environments/environment';

@Injectable({
  providedIn: 'root'
})
export class CourseService {
  

  private apiUrl = `${environment.apiUrl}course-details`; // Backend API endpoint

  constructor(private http: HttpClient) { }

  // Get all course details
  getCourseDetails(courseId: number): Observable<CourseDetailDTO> {
    return this.http.get<CourseDetailDTO>(`${this.apiUrl}/${courseId}`);
  }

  // Add a new course detail
  addCourseDetail(courseDetail: CourseDetailDTO): Observable<CourseDetailDTO> {
    return this.http.post<CourseDetailDTO>(`${this.apiUrl}/add`, courseDetail);
  }

  getAllCourses(): Observable<CourseDetailListingDTO[]> {
    return this.http.get<CourseDetailListingDTO[]>(`${this.apiUrl}/all`);
  }

  updateCourse(courseId: number, course: CourseDetailDTO): Observable<void> {
    return this.http.put<void>(`${this.apiUrl}/${courseId}`, course);
  }
  getCourseById(id: number): Observable<CourseDetailDTO> {
    return this.http.get<CourseDetailDTO>(`${this.apiUrl}/${id}`);
  }
  getAllStudentCourses(): Observable<CourseDetailStudentListingDTO[]> {
    const token = localStorage.getItem('loggedInUser');
    const headers = new HttpHeaders({
      Authorization: `Bearer ${token}`,
      'Content-Type': 'application/json',
    });
    return this.http.get<CourseDetailStudentListingDTO[]>(`${this.apiUrl}/student-courses`, { headers });
  }
}
