import { Component } from '@angular/core';
import { FacultyService } from '../services/faculty.service';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-faculties',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './faculties.component.html',
  styleUrl: './faculties.component.scss'
})
export class FacultiesComponent {
  faculties: any[] = [];
  errorMessage: string = '';
  facultyImages: { [facultyId: number]: string } = {};

  constructor(private facultyService: FacultyService) {}

  ngOnInit(): void {
    this.loadFaculties();
  }
  loadFaculties(): void {
    this.facultyService.getFaculties().subscribe(
      (data) => {
        this.faculties = data;
        this.loadFacultyImages();
      },
      (error) => {
        this.errorMessage = 'Error fetching faculties: ' + error.message;
        console.error('Error fetching faculties:', error);
      }
    );
  }
  loadFacultyImages(): void {
    this.faculties.forEach(faculty => {
      this.facultyService.getFacultyImage(faculty.id).subscribe(
        (imageBlob) => {
          const imageUrl = URL.createObjectURL(imageBlob);
          this.facultyImages[faculty.id] = imageUrl;
        },
        (error) => {
          console.error('Error fetching faculty image:', error);
        }
      );
    });
  }
}
