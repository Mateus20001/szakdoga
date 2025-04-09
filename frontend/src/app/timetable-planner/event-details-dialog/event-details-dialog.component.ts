import { Component, Inject } from '@angular/core';
import { MAT_DIALOG_DATA, MatDialogModule } from '@angular/material/dialog';
import { CommonModule } from '@angular/common';
import { CalendarEvent } from 'angular-calendar';
import { MatButton } from '@angular/material/button';

@Component({
  selector: 'app-event-details-dialog',
  standalone: true,
  imports: [CommonModule, MatDialogModule, MatButton],
  template: `
    <h2 mat-dialog-title>Óra részletei</h2>
    <mat-dialog-content class="text-sm space-y-2">
      <p><strong>Név:</strong> {{ data.title }}</p>
      <p><strong>Helyszín:</strong> {{ data.meta?.location || 'N/A' }}</p>
      <p><strong>Tanár(ok):</strong> {{ data.meta?.teacherIds?.join(', ') || 'N/A' }}</p>
      <p><strong>Kezdés:</strong> {{ data.start | date: 'fullDate' }} - {{ data.start | date: 'shortTime' }}</p>
      <p><strong>Befejezés:</strong> {{ data.end | date: 'shortTime' }}</p>
    </mat-dialog-content>
    <mat-dialog-actions align="end">
      <button mat-button mat-dialog-close>Bezárás</button>
    </mat-dialog-actions>
  `,
})
export class EventDetailsDialogComponent {
  constructor(@Inject(MAT_DIALOG_DATA) public data: CalendarEvent) {}
}
