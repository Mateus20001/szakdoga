import { Component, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { AppliedCourse, StudentStatistic } from '../models/AppliedCourseDTO';
import { CourseApplicationService } from '../services/course-application.service';
import { CommonModule } from '@angular/common';
import { NgxChartsModule, ScaleType } from '@swimlane/ngx-charts';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { MatCard } from '@angular/material/card';
import { MatList, MatListItem } from '@angular/material/list';
import { MatFormField, MatLabel } from '@angular/material/form-field';
import { MatOption } from '@angular/material/core';
import { MatInputModule } from '@angular/material/input';
import { MatSelectModule } from '@angular/material/select';

@Component({
  selector: 'app-student-statistic',
  standalone: true,
  imports: [
    CommonModule, 
    NgxChartsModule, 
    FormsModule, 
    MatCard, 
    MatList, 
    MatListItem, 
    MatFormField, 
    MatLabel, 
    MatOption, 
    ReactiveFormsModule, 
    MatInputModule, 
    MatSelectModule
  ],
  templateUrl: './student-statistic.component.html',
  styleUrls: ['./student-statistic.component.scss'],
  schemas: [CUSTOM_ELEMENTS_SCHEMA]  // Add this line to allow custom elements
})
export class StudentStatisticComponent {
  appliedCourses: StudentStatistic[] = [];
  chartData: any[] = [];
  totalCredit: number = 0;
  selectedSemester: string | null = null;
  filteredGrades: StudentStatistic[] = [];
  allSemesters: string[] = [];
  summaryCredit: number = 0;
  creditChartData: any[] = [];
  creditColorScheme = {
    name: 'cool',
    selectable: true,
    domain: ['#4CAF50', '#E0E0E0'],  // Green for acquired, Light Gray for not acquired
    group: ScaleType.Ordinal
  };
  colorScheme = {
    name: 'cool',
    selectable: true,
    group: ScaleType.Ordinal,
    domain: ['#5AA454', '#A10A28', '#C7B42C', '#AAAAAA']
  };

  constructor(private courseApplicationService: CourseApplicationService) {}

  ngOnInit(): void {
    this.fetchAppliedCourses();
  }

  fetchAppliedCourses(): void {
    this.courseApplicationService.getStudentStatistics().subscribe(
      (courses) => {
        this.appliedCourses = courses;
        this.generateChartData();
      },
      (error) => {
        console.error('Error fetching applied courses:', error);
      }
    );
  }

  generateChartData(): void {
    const grouped = new Map<string, number[]>();

    // Group by semester
    this.appliedCourses.forEach(stat => {
      const key = stat.semester; // Use the semester value as the key

      // Initialize the group if it doesn't exist
      if (!grouped.has(key)) {
        grouped.set(key, []);
      }

      // Push grade value for this semester
      grouped.get(key)?.push(stat.bestGradeValue);
    });

    // Sort semesters based on their year and semester (if needed)
    const sortedSemesters = Array.from(grouped.keys()).sort((a, b) => {
      const [aYear, aSem] = a.split(' ').map(Number);
      const [bYear, bSem] = b.split(' ').map(Number);
      return aYear !== bYear ? aYear - bYear : aSem - bSem;
    });

    // Prepare chart data: Only one line, connecting all average grades
    this.chartData = [
      {
        name: 'Átlag',  // Only one series (line) connecting all averages
        series: sortedSemesters.map(semester => {
          const grades = grouped.get(semester)!;  // Get all grades for this semester
          const averageGrade = grades.reduce((sum, grade) => sum + grade, 0) / grades.length;  // Calculate average
          return {
            name: semester,  // x-axis label: semester
            value: averageGrade  // y-axis value: average grade
          };
        })
      }
    ];

    this.allSemesters = ['Összes félév', ...sortedSemesters]; // Add all semesters to the dropdown
    this.selectedSemester = 'Összes félév'; // Set the default selection

    this.updateSummaryCredit();
  }

  onSemesterChange(): void {
    if (this.selectedSemester === 'Összes félév') {
      this.filteredGrades = [...this.appliedCourses];  // Show all semesters
    } else {
      this.filteredGrades = this.appliedCourses.filter(stat => stat.semester === this.selectedSemester);
    }

    this.updateSummaryCredit();  // Recalculate the summary credit and update chart
  }

  updateSummaryCredit(): void {
    const acquired = this.filteredGrades
      .filter(stat => stat.bestGradeValue > 1)
      .reduce((sum, stat) => sum + stat.creditScore, 0);

    const total = this.filteredGrades.reduce((sum, stat) => sum + stat.creditScore, 0);

    this.summaryCredit = acquired;
    this.totalCredit = total;
    this.creditChartData = [
      {
        name: 'Teljesített',
        value: acquired
      },
      {
        name: 'Hiányzó',
        value: total - acquired
      }
    ];
  }

  customTooltip({ name, value, extra }: any): string {
    return `${name}\nÁtlag: ${value.toFixed(2)}\nJegyek száma: ${extra?.count || 0}`;
  }
}
