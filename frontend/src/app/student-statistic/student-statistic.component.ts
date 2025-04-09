import { Component } from '@angular/core';
import { AppliedCourse, StudentStatistic } from '../models/AppliedCourseDTO';
import { CourseApplicationService } from '../services/course-application.service';
import { CommonModule } from '@angular/common';
import { NgxChartsModule, ScaleType } from '@swimlane/ngx-charts';

@Component({
  selector: 'app-student-statistic',
  standalone: true,
  imports: [CommonModule, NgxChartsModule],
  templateUrl: './student-statistic.component.html',
  styleUrls: ['./student-statistic.component.scss']
})
export class StudentStatisticComponent {
  appliedCourses: StudentStatistic[] = [];
  chartData: any[] = [];

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

    this.appliedCourses.forEach(stat => {
      const date = new Date(stat.creationDate);
      const year = date.getFullYear();
      const month = date.getMonth();
      const semester = month >= 6 ? 2 : 1;
      const studyYear = semester === 1 ? `${year}/${year - 1}` : `${year + 1}/${year}`;
      const key = `${studyYear} ${semester}. félév`;

      if (!grouped.has(key)) {
        grouped.set(key, []);
      }

      grouped.get(key)?.push(stat.bestGradeValue);
    });

    // Prepare data for the chart
    this.chartData = Array.from(grouped.entries()).map(([key, grades]) => ({
      name: key,
      series: [
        {
          name: 'Átlag',
          value: grades.reduce((a, b) => a + b, 0) / grades.length,
          extra: { count: grades.length }
        }
      ]
    }));
  }

  // Custom Tooltip function to format the tooltip content
  customTooltip({ name, value, extra }: any): string {
    return `${name}\nÁtlag: ${value.toFixed(2)}\nJegyek száma: ${extra?.count || 0}`;
  }
}
