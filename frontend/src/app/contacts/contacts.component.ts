import { Component, OnInit } from '@angular/core';
import { AuthService } from '../auth/auth.service';
import { NgFor, NgIf } from '@angular/common';
import { User } from '../models/user';

@Component({
  standalone: true,
  imports: [NgIf, NgFor],
  selector: 'app-contact',
  templateUrl: './contacts.component.html',
  styleUrls: ['./contacts.component.scss']
})
export class ContactsComponent implements OnInit {
  contacts: String[] = []; // Array to hold the retrieved contacts
  loading: boolean = false; // Loading state
  error: string | null = null; // Error message
  userId: string = "123456";
  users: User[] | undefined;
  constructor(private authService: AuthService) {}

  ngOnInit(): void {
    this.loadUsers();
    const userIdFromStorage = localStorage.getItem("loggedInUser");
    this.userId = userIdFromStorage !== null ? userIdFromStorage : "ERROR";
    if (this.userId === "ERROR") {
      throw Error("PRoblem")
    }

    this.loading = true; // Set loading to true
    
  }
  loadUsers(): void {
    this.authService.getAllUsers().subscribe(
      (data: User[]) => {
        this.users = data;
        console.log('Users loaded:', this.users);
      },
      (error) => {
        console.error('Error loading users:', error);
      }
    );
  }
}
