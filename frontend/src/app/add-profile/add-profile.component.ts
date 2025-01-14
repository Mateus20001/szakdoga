import { CommonModule } from '@angular/common';
import { Component } from '@angular/core';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { MatFormField, MatLabel } from '@angular/material/form-field';
import { AuthService } from '../auth/auth.service';
import { MatInputModule } from '@angular/material/input';
import { MatOption } from '@angular/material/core';
import { MajorService } from '../services/major.service';

@Component({
  selector: 'app-add-profile',
  standalone: true,
  imports: [CommonModule, MatFormField, MatLabel, FormsModule, MatInputModule, ReactiveFormsModule],
  templateUrl: './add-profile.component.html',
  styleUrl: './add-profile.component.scss'
})
export class AddProfileComponent {
  currentmajors = 0;
  currentemailscount = 0;
  majors: { id: string; name: string }[] = [];
  user: any = {
    firstName: '',
    lastName: '',
    emails: [],
    majors: [],
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
    if (this.user.emails[index] && this.currentemailscount < this.user.emails.length) {
      this.currentemailscount++;
      console.log(this.currentemailscount);
    }
  }
}
