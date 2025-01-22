import { Component } from '@angular/core';
import { FormBuilder, FormControl, FormGroup, Validators } from '@angular/forms';
import { ReactiveFormsModule } from '@angular/forms';
import { MatInputModule } from '@angular/material/input';
import { MatButtonModule, MatIconAnchor, MatIconButton } from '@angular/material/button';
import { MatFormFieldModule } from '@angular/material/form-field';
import { CommonModule } from '@angular/common';
import { AuthService } from '../auth/auth.service';
import { MatCardModule} from '@angular/material/card';

import { Router } from '@angular/router';
import { NewsComponent } from "../news/news.component";
import { SignupDTO } from '../models/signupDTO';
@Component({
  selector: 'app-login',
  standalone: true,
  imports: [ReactiveFormsModule,
    NewsComponent,
    MatInputModule,
    MatButtonModule,
    MatFormFieldModule,
    MatCardModule,
    CommonModule,
    MatIconButton],
  templateUrl: './login.component.html',
  styleUrl: './login.component.scss',
})
export class LoginComponent {

  form!: FormGroup;
  backgroundColor!: string;
  paper2Color!: string;
  private formSubmitAttempt: boolean | undefined
  errorMessage: string | null = null;
  passwordVisible: boolean = false;
  passwordControl: FormControl = new FormControl('', [Validators.required, Validators.minLength(8)]);

  constructor(private fb: FormBuilder, private authService: AuthService, private router: Router) {}

  ngOnInit(): void {
    this.initializeForms();
    this.resetBackground();
    this.paper2Color= this.getRandomColor();
  }

  initializeForms(): void {
    this.form = this.fb.group({
      id: ['', [
        Validators.required,
        Validators.minLength(6),
        Validators.maxLength(6),
        this.identifierValidator
      ]],
      password: this.passwordControl,
    });

  }

  identifierValidator(control: any) {
    const value = control.value.toLowerCase();
    const regex = /^[a-z0-9]+$/i;
    return regex.test(value) ? null : { invalidIdentifier: true };
  }

  getRandomColor(): string {
    const letters = '0123456789ABCDEF';
    let color = '#';
    for (let i = 0; i < 6; i++) {
      color += letters[Math.floor(Math.random() * 16)];
    }
    return color;
  }

  resetBackground(): void {
    this.backgroundColor = this.getRandomColor();
    const container = document.querySelector('.form-container');
    if (container) {
      container.classList.remove('animate');
      setTimeout(() => container.classList.add('animate'), 50);
    }
  }

  onSubmit(): void {
      if (this.form.valid) {
        console.log(this.form.value)
        this.authService.login(this.form.value).subscribe(
          data => {
            console.log(data.token)
            localStorage.setItem('loggedInUser', data.token);
            const expirationTime = Date.now() + data.expiresIn * 1000;
            localStorage.setItem('expirationTime', expirationTime.toString());
            if (data.firstlogin == true) {
              this.router.navigate(['/changePassword'])
            } else {
              this.router.navigate(['/main']);
            }
          },
          error => {
            this.errorMessage = error.error.message;
          }
        );
      }
      this.formSubmitAttempt = true;
    }
    togglePasswordVisibility(): void {
      this.passwordVisible = !this.passwordVisible;
    }
  }
