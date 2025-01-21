import { Component } from '@angular/core';
import { MajorService } from '../services/major.service';
import { MatCard, MatCardContent, MatCardHeader, MatCardSubtitle, MatCardTitle } from '@angular/material/card';
import { CommonModule } from '@angular/common';
import { MajorWithFacultyDTO } from '../models/majorWithFacultyDTO';

@Component({
  selector: 'app-majors',
  standalone: true,
  imports: [CommonModule, MatCard, MatCardHeader, MatCardTitle, MatCardSubtitle, MatCardContent],
  templateUrl: './majors.component.html',
  styleUrl: './majors.component.scss'
})
export class MajorsComponent {
  constructor(private majorService: MajorService) {}
  majors: MajorWithFacultyDTO[] = [];
  ngOnInit() {
    this.majorService.getMajorNamesAndDescriptions().subscribe(
      (response: MajorWithFacultyDTO[]) => {
        this.majors = response;
      },
      (error) => {
        console.error('Error fetching major descriptions', error);
      }
    )
  }
}
