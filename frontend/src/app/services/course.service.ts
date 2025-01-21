import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { CourseDetailDTO, CourseDetailListingDTO } from '../models/CourseDetailListingDTO';

@Injectable({
  providedIn: 'root'
})
export class CourseService {
  

  private apiUrl = 'http://localhost:8080/api/course-details'; // Backend API endpoint

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
  
}
