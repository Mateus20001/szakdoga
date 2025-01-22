import { Component } from '@angular/core';
import { AuthService } from '../auth/auth.service';
import { UserDetailsDTO } from '../models/userDetailsDTO';
import { Router } from '@angular/router';
import { MatButton, MatIconButton } from '@angular/material/button';
import { MatCard, MatCardContent, MatCardTitle } from '@angular/material/card';

@Component({
  selector: 'app-profile',
  standalone: true,
  imports: [MatIconButton, MatButton, MatCard, MatCardTitle, MatCardContent],
  templateUrl: './profile.component.html',
  styleUrl: './profile.component.scss'
})
export class ProfileComponent {
  userDetails: UserDetailsDTO | null = null;

  constructor(private authService: AuthService, private router: Router) {}

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
  navigateToChangeUsername() {
    this.router.navigate(['/changeUsername']);
  }
  navigateToChangePassword() {
    this.router.navigate(['/changePassword']).then(() => {
      window.scrollTo({ top: 0, behavior: 'smooth' });
    });
  }
  
}
