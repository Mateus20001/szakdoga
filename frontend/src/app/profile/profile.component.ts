import { Component } from '@angular/core';
import { AuthService } from '../auth/auth.service';
import { UserDetailsDTO } from '../models/userDetailsDTO';

@Component({
  selector: 'app-profile',
  standalone: true,
  imports: [],
  templateUrl: './profile.component.html',
  styleUrl: './profile.component.scss'
})
export class ProfileComponent {
  userDetails: UserDetailsDTO | null = null;

  constructor(private authService: AuthService) {}

  ngOnInit(): void {
    const authToken = localStorage.getItem('loggedInUser'); // Assuming the token is stored in localStorage

    if (authToken) {
      this.authService.getUserDetails(authToken).subscribe(
        (data: UserDetailsDTO) => {
          this.userDetails = data;
        },
        (error) => {
          console.error('Error fetching user details:', error);
        }
      );
    } else {
      console.error('No authentication token found');
    }
  }
}
