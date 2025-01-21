import { NgFor, NgForOf, NgIf } from '@angular/common';
import { Component } from '@angular/core';
import { FormBuilder, FormGroup, FormsModule, ReactiveFormsModule, Validators } from '@angular/forms';
import { MatFormField, MatLabel } from '@angular/material/form-field';
import { FacultyService } from '../services/faculty.service';
import { MatInputModule } from '@angular/material/input';
import { MatOption } from '@angular/material/core';
import { MatSelectModule } from '@angular/material/select';
import { MajorService } from '../services/major.service';

@Component({
  selector: 'app-add-faculty',
  standalone: true,
  imports: [MatFormField, ReactiveFormsModule, MatLabel, NgIf, MatInputModule, FormsModule, MatSelectModule, NgFor, NgForOf],
  templateUrl: './add-faculty.component.html',
  styleUrl: './add-faculty.component.scss'
})
export class AddFacultyComponent {
  isAlternateForm = false;
  facultyForm: FormGroup;
  majorForm: FormGroup;
  faculties: { id: number; name: string }[] = [];
  selectedFile: File | null = null;
  filePreview: string | ArrayBuffer | null = null;

  constructor(private fb: FormBuilder, private facultyService: FacultyService, private majorService: MajorService) {
    this.facultyForm = new FormGroup("");
    this.majorForm = new FormGroup("");
  }

  ngOnInit(): void {
    this.facultyForm = this.fb.group({
      name: ['', Validators.required],
      description: ['', Validators.required]
    });
    this.majorForm = this.fb.group({
      name: ['', Validators.required],
      description: ['', Validators.required],
      facultyId: ['', Validators.required]
    });
    this.loadFaculties();
  }
  loadFaculties(): void {
    this.facultyService.getFaculties().subscribe(
      (data: { id: number; name: string }[]) => {
        this.faculties = data;
      },
      (error) => {
        console.error('Error loading faculties:', error);
      }
    );
  }

  onFileSelect(event: any): void {
    const file: File = event.target.files[0];
    if (file) {
      this.selectedFile = file;
      const reader = new FileReader();
      reader.onload = () => {
        this.filePreview = reader.result;
      };
      reader.readAsDataURL(file);
    }
  }

  onSubmit(): void {
    if (this.facultyForm.invalid || !this.selectedFile) {
      return; 
    }

    const name = this.facultyForm.get('name')?.value;
    const description = this.facultyForm.get('description')?.value;

    this.facultyService.addFaculty(name, description, this.selectedFile).subscribe(
      (response: any) => {
        console.log('Faculty added successfully', response);
      },
      (error: any) => {
        console.error('Error adding faculty', error);
      }
    );
  }
  onMajorSubmit() {
    if (this.majorForm.valid) {
      this.majorService.addMajor(this.majorForm.value).subscribe(
        (response) => {
          console.log('Major added successfully:', response);
        },
        (error) => {
          console.error('Error adding major:', error);
        }
      );
    }
  }
}
