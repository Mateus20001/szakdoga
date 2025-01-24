import { HttpClient } from '@angular/common/http';
import { Component } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { CourseTeacherService } from '../services/course-teacher.service';
import { NgFor, NgIf } from '@angular/common';

@Component({
  selector: 'app-edit-teacher-course',
  standalone: true,
  imports: [NgIf, NgFor],
  templateUrl: './edit-teacher-course.component.html',
  styleUrl: './edit-teacher-course.component.scss'
})
export class EditTeacherCourseComponent {
  courseId!: number;
  teachers: { teacherId: string, responsible: boolean }[] = [];
  isUserResponsible: boolean = false;

  constructor(
    private courseTeacherService: CourseTeacherService,
    private route: ActivatedRoute
  ) {}

  ngOnInit(): void {
    this.courseId = +this.route.snapshot.paramMap.get('id')!;

    this.fetchTeachers();
  }

  fetchTeachers(): void {
    this.courseTeacherService.getTeachersByCourse(this.courseId).subscribe(
      (teachers) => {
        this.teachers = teachers.map(teacher => ({
          teacherId: teacher.teacherId,
          responsible: teacher.responsible
        }));
        // Check if the current user is responsible for the course
        this.isUserResponsible = this.teachers.some(teacher => teacher.responsible);
      },
      (error) => {
        console.error('Error fetching teachers:', error);
      }
    );
  }
  addNewTeacher(): void {
    console.log('Adding new teacher to the course');
    // Implement the logic to add a new teacher (e.g., show a modal or navigate to a new page)
  }
}
