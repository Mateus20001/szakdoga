import { CommonModule } from '@angular/common';
import { Component } from '@angular/core';
import { MatCard, MatCardContent, MatCardImage, MatCardTitle } from '@angular/material/card';
import { NewsService } from '../services/news.service';
import { MatDialog } from '@angular/material/dialog';
import { NewsDetailDialogComponent } from '../news-detail-dialog/news-detail-dialog.component';

@Component({
  selector: 'app-news',
  standalone: true,
  imports: [CommonModule, MatCard, MatCardTitle, MatCardContent, MatCardImage],
  templateUrl: './news.component.html',
  styleUrl: './news.component.scss'
})
export class NewsComponent {
  news: any[] = [];
  constructor(private newsService: NewsService, public dialog: MatDialog) {}
  ngOnInit(): void {
    this.loadNews();
  }

  loadNews(): void {
    this.newsService.getNews().subscribe(
      (data) => {
        this.news = data;
        this.news.forEach((article) => {
          if (article.id) {
            this.loadImage(article);
          }
        });
      },
      (error) => {
        console.error('Error fetching news:', error);
      }
    );
  }

  loadImage(article: any): void {
    this.newsService.getImage(article.id).subscribe(
      (imageBlob) => {
        article.imageUrl = URL.createObjectURL(imageBlob);
      },
      (error) => {
        console.error('Error fetching image:', error);
      }
    );
  }
  openDialog(article: any): void {
    this.dialog.open(NewsDetailDialogComponent, {
      data: article,
      minWidth: '1000px',
      minHeight: '470px',
      panelClass: 'custom-dialog-container'
    });
  }
}
