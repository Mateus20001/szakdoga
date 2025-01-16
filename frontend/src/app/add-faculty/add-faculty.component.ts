import { NgIf } from '@angular/common';
import { Component } from '@angular/core';
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { MatFormField, MatLabel } from '@angular/material/form-field';
import { FacultyService } from '../services/faculty.service';
import { MatInputModule } from '@angular/material/input';

@Component({
  selector: 'app-add-faculty',
  standalone: true,
  imports: [MatFormField, ReactiveFormsModule, MatLabel, NgIf, MatInputModule],
  templateUrl: './add-faculty.component.html',
  styleUrl: './add-faculty.component.scss'
})
export class AddFacultyComponent {
  facultyForm: FormGroup;
  selectedFile: File | null = null;
  filePreview: string | ArrayBuffer | null = null;

  constructor(private fb: FormBuilder, private facultyService: FacultyService) {
    this.facultyForm = new FormGroup("");
  }

  ngOnInit(): void {
    this.facultyForm = this.fb.group({
      name: ['', Validators.required],
      description: ['', Validators.required]
    });
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
}
