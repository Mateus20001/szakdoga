import { Component } from '@angular/core';
import { MajorService } from '../services/major.service';
import { MatCard, MatCardContent, MatCardHeader, MatCardSubtitle, MatCardTitle } from '@angular/material/card';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-majors',
  standalone: true,
  imports: [CommonModule, MatCard, MatCardHeader, MatCardTitle, MatCardSubtitle, MatCardContent],
  templateUrl: './majors.component.html',
  styleUrl: './majors.component.scss'
})
export class MajorsComponent {
  constructor(private majorService: MajorService) {}
  majors: { id: string; name: string; description: string; facultyName: string }[] = [];
  ngOnInit() {
    this.majorService.getmajorNameAndDescription().subscribe(
      data => {
        this.majors = data;
        console.log(data)
      },
      error => {
        
      }
    )
  }
}
