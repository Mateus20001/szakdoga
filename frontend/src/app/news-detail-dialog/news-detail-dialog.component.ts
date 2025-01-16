import { NgIf } from '@angular/common';
import { Component, Inject } from '@angular/core';
import { MAT_DIALOG_DATA } from '@angular/material/dialog';

@Component({
  selector: 'app-news-detail-dialog',
  standalone: true,
  imports: [NgIf],
  templateUrl: './news-detail-dialog.component.html',
  styleUrl: './news-detail-dialog.component.scss'
})
export class NewsDetailDialogComponent {
  constructor(@Inject(MAT_DIALOG_DATA) public data: any) {}
}
