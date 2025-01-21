import { CommonModule } from '@angular/common';
import { Component } from '@angular/core';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { MatFormField, MatLabel } from '@angular/material/form-field';
import { AuthService } from '../auth/auth.service';
import { MatInputModule } from '@angular/material/input';
import { MatOption } from '@angular/material/core';
import { MajorService } from '../services/major.service';
import { MatIconButton } from '@angular/material/button';
import { MatSelect } from '@angular/material/select';

@Component({
  selector: 'app-add-profile',
  standalone: true,
  imports: [CommonModule, MatFormField, MatLabel, FormsModule, MatInputModule, ReactiveFormsModule, MatIconButton, MatOption, MatSelect],
  templateUrl: './add-profile.component.html',
  styleUrl: './add-profile.component.scss'
})
export class AddProfileComponent {
  currentmajors = 0;
  currentemailscount = 0;
  currentphonenumberscount = 0;
  majors: { id: string; name: string }[] = [];
  user: any = {
    firstName: '',
    lastName: '',
    emails: [],
    majors: [],
    phone_numbers: [],
    roles: []
  };
  message: string = '';
  messageType: string = ''; 
  ngOnInit() {
    this.majorService.getMajors().subscribe(
      (data) => {
        this.majors = data; 
      },
      (error) => {
        console.error('Error fetching major data:', error); 
      }
    );
  }
  onRoleChange(event: Event): void {
    const checkbox = event.target as HTMLInputElement;
    const roleId = parseInt(checkbox.value, 10);

    const roleNames: { [key: number]: string } = {
      0: 'STUDENT',
      1: 'ADMIN',
      2: 'TEACHER'
    };
  
    const roleName = roleNames[roleId];
  
    if (checkbox.checked && roleName) {
      this.user.roles.push(roleName);
    } else {
      this.user.roles = this.user.roles.filter((role: string) => role !== roleName);
    }
  }
  roles: any[] = [
    { name: 'STUDENT', id: 0 },
    { name: 'ADMIN', id: 1 },
    { name: 'TEACHER', id: 2 }
  ];
  constructor(private userService: AuthService, private majorService: MajorService) {
  }

  onSubmit() {
    console.log('User data:', this.user);
    if (!this.user.emails[0]) {
      this.message = 'Please provide at least one email.';
      this.messageType = 'error';
      return;
    }
    this.userService.saveUser(localStorage.getItem("loggedInUser"), this.user).subscribe(
      (response: any) => {
        console.log('User submitted successfully:', response);
        this.message = 'User created successfully! ID: ' + response.userId;
        this.messageType = 'success'; 
      },
      error => {
        console.error('Error submitting user:', error);
        this.message = 'Error submitting user: ' + error.error.message;
        this.messageType = 'error'; 
      }
    );
  }
  getRange(count: number): any[] {
    return new Array(count);
  }
  onMajorChange(index: number) {
    if (this.user.majors[index] && this.currentmajors < this.user.majors.length) {
      this.currentmajors++;
    }
  }
  onEmailChange(index: number) {
    if (this.user.emails[index] && this.currentemailscount < this.user.emails.length && this.user.emails.length < 5) {
      this.currentemailscount++;
    }
  }
  emailPattern = '^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+(?<![.-])\\.[a-zA-Z]{2,}$'; //will throw error: \ => in HTML, \\ => in angular is the escape character

  // Method to validate email format
  isEmailValid(index: number): boolean {
    const email = this.user.emails[index];
    return email ? new RegExp(this.emailPattern).test(email) : true;
  }

  onPhoneNumberChange(index: number) {
    if (this.user.phone_numbers[index] && this.currentphonenumberscount < this.user.phone_numbers.length && this.user.phone_numbers.length < 5) {
      this.currentphonenumberscount++;
    }
  }
  phoneNumberPattern = '^[0-9]{10}$';

  isPhoneNumberValid(index: number): boolean {
    const phoneNumber = this.user.phone_numbers[index];
    return phoneNumber ? new RegExp(this.phoneNumberPattern).test(phoneNumber) : true;
  }
  removeMajor(index: number): void {
    this.user.majors.splice(index, 1);
    this.currentmajors--; 
  }

  removePhoneNumber(index: number): void {
    this.user.phone_numbers.splice(index, 1); 
    this.currentphonenumberscount--; 
  }
  removeEmail(index: number): void {
    this.user.emails.splice(index, 1);
    if (this.currentemailscount > 0) {
      this.currentemailscount--; 
    }
  }
  onEmailBlur(index: number): void {
    if (!this.user.emails[index] && this.currentemailscount > 0) {
      this.removeEmail(index);
    }
  }
  onPhoneBlur(index: number): void {
    if (!this.user.phone_numbers[index] && this.currentphonenumberscount > 0) {
      this.removePhoneNumber(index);
    }
  }
}
