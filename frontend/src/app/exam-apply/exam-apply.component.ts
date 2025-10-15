import { Component } from '@angular/core';
import { ExamService } from '../services/exam.service';
import { ExamResponse } from '../models/ExamResponse';
import { MatCard, MatCardActions, MatCardContent, MatCardHeader, MatCardSubtitle, MatCardTitle } from '@angular/material/card';
import { CommonModule } from '@angular/common';
import { MatSpinner } from '@angular/material/progress-spinner';
import { MatSnackBar } from '@angular/material/snack-bar';
import { LocationEnum } from '../models/LocationEnum';

@Component({
  selector: 'app-exam-apply',
  standalone: true,
  imports: [MatCard, MatCardActions, MatCardContent, MatCardHeader, MatCardTitle, MatCardSubtitle, CommonModule, MatSpinner],
  templateUrl: './exam-apply.component.html',
  styleUrl: './exam-apply.component.scss'
})
export class ExamApplyComponent {
  groupedExams: { courseDateName: string, exams: ExamResponse[] }[] = [];
  
  exams: ExamResponse[] = [];
  loading = false;
  errorMessage: string | undefined;
  constructor(private examService: ExamService, private snackBar: MatSnackBar) {}
  ngOnInit(): void {
    this.loadRelevantExams();
  }
  loadRelevantExams(): void {
    this.loading = true;
    this.examService.getRelevantExams().subscribe(
      (data) => {
        this.exams = data;
        this.exams = data.map(exam => ({
          ...exam,
          location: LocationEnum[exam.location as keyof typeof LocationEnum]
        }));
        const groups: { [courseDateName: string]: ExamResponse[] } = {};
        this.exams.forEach(exam => {
          const name = exam.courseDateName ?? 'N/A'; // ensure string key
          if (!groups[name]) {
            groups[name] = [];
          }
          groups[name].push(exam);
        });

        this.groupedExams = Object.keys(groups).map(key => ({
          courseDateName: key,
          exams: groups[key]
        }));

        this.loading = false;
      },
      (error) => {
        this.loading = false;
        this.errorMessage = 'Error fetching faculties: ' + error.message;
        console.error('Error fetching faculties:', error);
      }
    );
  }
  applyForExam(examId: number): void {
    this.examService.applyToExam(examId).subscribe({
      next: (message) => {
      const appliedExam = this.exams.find(exam => exam.id === examId);
      if (appliedExam && message == "Sikeresen jelentkeztél a vizsgára.") {
        appliedExam.applied = true;
      }

      this.snackBar.open(message, 'Bezárás', { duration: 3000 });
    },
      error: (err) => {
        const errorMsg = err.error || 'Hiba történt a jelentkezés során.';
        this.snackBar.open(errorMsg, 'Bezárás', { duration: 3000 });
      }
    });
  }
  removeExamApplication(examId: number): void {
  this.examService.removeApplication(examId).subscribe({
    next: (message) => {
      // Update local state
      const exam = this.exams.find(e => e.id === examId);
      if (exam) {
        exam.applied = false;
      }
      this.snackBar.open(message, 'Bezárás', { duration: 3000 });
    },
    error: (err) => {
      const errorMsg = err.error || 'Hiba történt a visszavonás során.';
      this.snackBar.open(errorMsg, 'Bezárás', { duration: 3000 });
    }
  });
}
}
