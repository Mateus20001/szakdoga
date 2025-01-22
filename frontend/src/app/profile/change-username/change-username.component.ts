import { Component } from '@angular/core';
import { AuthService } from '../../auth/auth.service';
import { Router } from '@angular/router';
import { NgIf } from '@angular/common';
import { MatError, MatFormField, MatLabel } from '@angular/material/form-field';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { MatInput } from '@angular/material/input';
import { MatButton } from '@angular/material/button';
import { MatDialog } from '@angular/material/dialog';
import { ConfirmnameskipdialogComponent } from './confirmnameskipdialog/confirmnameskipdialog.component';

@Component({
  selector: 'app-change-username',
  standalone: true,
  imports: [NgIf, MatFormField, MatLabel, FormsModule, ReactiveFormsModule, MatInput, MatButton],
  templateUrl: './change-username.component.html',
  styleUrl: './change-username.component.scss'
})
export class ChangeUsernameComponent {
  newUsername: string = '';
  errorMessage: string = '';

  constructor(private authService: AuthService, private router: Router, private dialog: MatDialog) {}

  ngOnInit() {
  }
  
  usernamePattern = /^[a-zA-Z0-9._-]{3,30}$/;

  validateUsername(): boolean {
    if (!this.newUsername.match(this.usernamePattern)) {
      this.errorMessage = 'Invalid username. Only letters, numbers, ".", "_", and "-" are allowed. Length must be between 3-30 characters.';
      return false;
    }
    this.errorMessage = '';
    return true;
  }

  submit() {
    const authToken = localStorage.getItem('loggedInUser');
    if (authToken && this.validateUsername()) {
      this.authService.changeUsername(authToken, this.newUsername).subscribe({
        next: () => {
          this.router.navigate(['/main']);
        },
        error: (err) => {
          this.errorMessage = 'Error changing username. Please try again.';
          console.error(err);
        },
      });
    }
  }
  skip() {
    const dialogRef = this.dialog.open(ConfirmnameskipdialogComponent, {
      width: '600px',
      data: {
        message:
          'Ha skippeled akkor mindig a valós neved fog megjelenni. Biztos vagy benne?',
        confirmButtonText: 'OK',
        cancelButtonText: 'Mégse',
      },
    });

    dialogRef.afterClosed().subscribe((result) => {
      if (result === 'confirm') {
        this.router.navigate(['/main']);
      }
    });
  }
}
