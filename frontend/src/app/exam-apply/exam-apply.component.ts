import { Component } from '@angular/core';
import { ExamService } from '../services/exam.service';

@Component({
  selector: 'app-exam-apply',
  standalone: true,
  imports: [],
  templateUrl: './exam-apply.component.html',
  styleUrl: './exam-apply.component.scss'
})
export class ExamApplyComponent {
  exams: any[] = [];
  errorMessage: string | undefined;
  constructor(private examService: ExamService) {}
  ngOnInit(): void {
    this.loadRelevantExams();
  }
  loadRelevantExams(): void {
    this.examService.getRelevantExams().subscribe(
      (data) => {
        this.exams = data;
        console.log(this.exams)
      },
      (error) => {
        this.errorMessage = 'Error fetching faculties: ' + error.message;
        console.error('Error fetching faculties:', error);
      }
    );
  }
}
