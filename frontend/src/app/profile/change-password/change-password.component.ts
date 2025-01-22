import { Component } from '@angular/core';
import { AuthService } from '../../auth/auth.service';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { MatFormField, MatFormFieldModule, MatLabel } from '@angular/material/form-field';
import { NgIf } from '@angular/common';
import { MatInputModule } from '@angular/material/input';
import { MatButton, MatIconButton } from '@angular/material/button';
import { Router } from '@angular/router';

@Component({
  selector: 'app-change-password',
  standalone: true,
  imports: [FormsModule, ReactiveFormsModule, MatFormField, MatLabel, NgIf, MatFormFieldModule, MatInputModule, MatIconButton, MatButton],
  templateUrl: './change-password.component.html',
  styleUrl: './change-password.component.scss'
})
export class ChangePasswordComponent {
  firstLogin: boolean | null = null;
  newPassword: string = '';
  confirmPassword: string = '';
  errorMessage: string = '';
  passwordVisible: boolean = false;

  constructor(private authService: AuthService, private router: Router) {}

  ngOnInit() {
    const authToken = localStorage.getItem('loggedInUser');
    if (authToken) {
      this.authService.getFirstLoginStatus(authToken).subscribe({
        next: (status) => {
          this.firstLogin = status;
        },
        error: (err) => {
          console.error('Error fetching first login status:', err);
        }
      });
    }
  }

  changePassword() {
    const authToken = localStorage.getItem('loggedInUser');
    if (authToken && this.newPassword === this.confirmPassword) {
      this.authService.changePassword(authToken, this.newPassword, this.confirmPassword).subscribe({
        next: () => {
          this.newPassword = '';
          this.confirmPassword = '';
          if (this.firstLogin) {
            this.router.navigate(['/changeUsername'])
          } else {
            this.router.navigate(['/main'])
          }
        },
        error: (err) => {
          this.errorMessage = 'Error changing password. Please try again.';
        }
      });
    } else {
      this.errorMessage = 'Passwords do not match.';
    }
  }
  
  togglePasswordVisibility(): void {
    this.passwordVisible = !this.passwordVisible;
  }
}