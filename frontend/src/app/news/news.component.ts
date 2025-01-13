import { CommonModule } from '@angular/common';
import { Component } from '@angular/core';
import { MatCard, MatCardContent, MatCardTitle } from '@angular/material/card';

@Component({
  selector: 'app-news',
  standalone: true,
  imports: [CommonModule, MatCard, MatCardTitle, MatCardContent],
  templateUrl: './news.component.html',
  styleUrl: './news.component.scss'
})
export class NewsComponent {
  news = [
    {
      imageUrl: 'https://via.placeholder.com/150',
      title: 'Hír 1 címe',
      description: 'Ez egy rövid leírás az első hírről.'
    },
    {
      imageUrl: 'https://via.placeholder.com/150',
      title: 'Hír 2 címe',
      description: 'Ez egy rövid leírás a második hírről.'
    },
    {
      imageUrl: 'https://via.placeholder.com/150',
      title: 'Hír 3 címe',
      description: 'Ez egy rövid leírás a harmadik hírről.'
    }
  ];
}
