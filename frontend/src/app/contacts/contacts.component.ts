import { Component, OnInit } from '@angular/core';
import { AuthService } from '../auth/auth.service';
import { CommonModule, NgFor, NgIf } from '@angular/common';
import { User } from '../models/user';
import { Contact } from '../models/email';
import { MatIconModule } from '@angular/material/icon';  
import { MatCardModule } from '@angular/material/card'; 
import { MatListModule } from '@angular/material/list'; 
import { MatProgressSpinnerModule } from '@angular/material/progress-spinner';

@Component({
  standalone: true,
  imports: [NgIf, NgFor, MatIconModule, MatIconModule,
    MatCardModule,
    MatListModule,
    MatProgressSpinnerModule, CommonModule],
  selector: 'app-contact',
  templateUrl: './contacts.component.html',
  styleUrls: ['./contacts.component.scss']
})
export class ContactsComponent implements OnInit {
  contacts: Contact[] = []; // Array to hold the retrieved contacts
  loading: boolean = false; // Loading state
  error: string | null = null; // Error message
  constructor(private authService: AuthService) {}

  ngOnInit(): void {

    this.loading = true; // Set loading to true
    
    this.fetchEmails();
  }
  
  fetchEmails(): void {
    this.authService.getEmails().subscribe(
      (emails) => {
        this.contacts = emails;
        console.log(this.contacts);
        this.loading = false; 
      },
      (error) => {
        console.error('Error fetching applied courses:', error);
      }
    );
  }
}
