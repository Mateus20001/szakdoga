import { Component } from '@angular/core';
import { ExamService } from '../services/exam.service';
import { AppliedExamResponse } from '../models/AppliedExamResponse';
import { MatSnackBar } from '@angular/material/snack-bar';
import { MatCard, MatCardActions, MatCardContent, MatCardHeader, MatCardSubtitle, MatCardTitle } from '@angular/material/card';
import { MatSpinner } from '@angular/material/progress-spinner';
import { CommonModule } from '@angular/common';
import { LocationEnum } from '../models/LocationEnum';

@Component({
  selector: 'app-exam-applied',
  standalone: true,
  imports: [MatCard, MatCardActions, MatSpinner, MatCardHeader, MatCardContent, MatCardTitle, MatCardSubtitle, CommonModule],
  templateUrl: './exam-applied.component.html',
  styleUrl: './exam-applied.component.scss'
})
export class ExamAppliedComponent {
  exams: AppliedExamResponse[] = [];
  loading = true;

  constructor(private examService: ExamService, private snackBar: MatSnackBar) {}

  ngOnInit(): void {
    this.loadAppliedExams();
  }

  loadAppliedExams(): void {
    this.loading = true;
    this.examService.getAllAppliedExams().subscribe({
      next: (data) => {
        this.exams = data;
        this.exams = data.map(exam => ({
                  ...exam,
                  location: LocationEnum[exam.location as keyof typeof LocationEnum]
                }));
        this.loading = false;
      },
      error: () => {
        this.snackBar.open('Hiba történt a vizsgák betöltésekor.', 'Bezárás', { duration: 3000 });
        this.loading = false;
      }
    });
  }

  removeApplication(examId: number): void {
    this.examService.removeApplication(examId).subscribe({
      next: (message) => {
        this.exams = this.exams.filter(e => e.id !== examId);
        this.snackBar.open(message, 'Bezárás', { duration: 3000 });
      },
      error: (err) => {
        const errorMsg = err.error || 'Hiba történt a visszavonás során.';
        this.snackBar.open(errorMsg, 'Bezárás', { duration: 3000 });
      }
    });
  }
}
