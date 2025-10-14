import { Component } from '@angular/core';
import { ExamService } from '../services/exam.service';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { AddExamRequest } from '../models/AddExamRequest';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { MatSelectModule } from '@angular/material/select';
import { MatButtonModule } from '@angular/material/button';
import { MatCardModule } from '@angular/material/card';
import { ReactiveFormsModule } from '@angular/forms';
import { CommonModule } from '@angular/common';
import { LocationEnum } from '../models/LocationEnum';

@Component({
  selector: 'app-exam-announcement',
  standalone: true,
  imports: [MatFormFieldModule, MatInputModule, MatSelectModule, MatButtonModule, MatCardModule, ReactiveFormsModule, CommonModule],
  templateUrl: './exam-announcement.component.html',
  styleUrl: './exam-announcement.component.scss'
})
export class ExamAnnouncementComponent {
  examForm!: FormGroup;
  isSubmitting = false;
  successMessage = '';
  errorMessage = '';
  locations = Object.values(LocationEnum);
  types = ['ONLINE', 'ATTENDANCE'];
  constructor(private examService: ExamService, private fb: FormBuilder) {}
  ngOnInit(): void {
    this.examForm = this.fb.group({
      examDate: ['', Validators.required],
      location: ['', Validators.required],
      longevity: [90, [Validators.required, Validators.min(1)]],
      type: ['', Validators.required],
      courseDateId: ['', Validators.required]
    });
  }
  onSubmit(): void {
    if (this.examForm.invalid) return;

    const formValue = this.examForm.value;
    const request: AddExamRequest = {
      examDate: formValue.examDate + ':00',
      location: formValue.location,
      longevity: formValue.longevity,
      type: formValue.type,
      courseDateId: formValue.courseDateId
    };

    this.isSubmitting = true;
    this.successMessage = '';
    this.errorMessage = '';

    this.examService.addExam(request).subscribe({
      next: (response) => {
        this.successMessage = '✅ Exam created successfully!';
        this.isSubmitting = false;
        this.examForm.reset();
      },
      error: (err) => {
        this.errorMessage = '❌ Failed to create exam: ' + (err.error?.message || err.statusText);
        this.isSubmitting = false;
      }
    });
  }
}
