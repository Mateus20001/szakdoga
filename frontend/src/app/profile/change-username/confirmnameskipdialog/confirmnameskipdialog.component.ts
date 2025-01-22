import { Component, Inject } from '@angular/core';
import { MatButton } from '@angular/material/button';
import { MAT_DIALOG_DATA, MatDialogActions, MatDialogContent, MatDialogRef } from '@angular/material/dialog';

@Component({
  selector: 'app-confirmnameskipdialog',
  standalone: true,
  imports: [MatDialogContent, MatDialogActions, MatButton],
  templateUrl: './confirmnameskipdialog.component.html',
  styleUrl: './confirmnameskipdialog.component.scss'
})
export class ConfirmnameskipdialogComponent {
  constructor(
    public dialogRef: MatDialogRef<ConfirmnameskipdialogComponent>,
    @Inject(MAT_DIALOG_DATA) public data: any
  ) {}

  onConfirm() {
    this.dialogRef.close('confirm'); 
  }

  onCancel() {
    this.dialogRef.close(); 
  }
}
