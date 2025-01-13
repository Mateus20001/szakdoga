import { Component, HostListener } from '@angular/core';
import { Router, RouterOutlet } from '@angular/router';
import { DropdownMenuComponent } from '../dropdown-menu/dropdown-menu.component';
import { CommonModule } from '@angular/common';
import { AuthService } from '../auth/auth.service';
import { MatIconButton } from '@angular/material/button';
import { MatIconAnchor } from '@angular/material/button';
import { MenuDropdown, MenuDropdownMenuObjects, MenuItem } from '../shared/constants';
import { Role } from '../models/userSessionEntity';
@Component({
  selector: 'app-layout',
  standalone: true,
  imports: [RouterOutlet, DropdownMenuComponent, CommonModule, MatIconButton],
  templateUrl: './layout.component.html',
  styleUrl: './layout.component.scss',
})

export class LayoutComponent {
  profileName: string | null = null;
  roles: Role[] = [];
  menuDropdownItems: MenuDropdown[] = MenuDropdownMenuObjects;
  showBackToTop = 0; // Opacity value (0 or 1)
  ngOnInit() {
    if (localStorage.getItem("loggedInUser") !== null) {
      this.authService.getUserNameAndRoles(localStorage.getItem("loggedInUser")).subscribe(
        data => {
          console.log(data)
          this.profileName = data.identifier;
          this.roles = data.roles;
        },
        error => {
        }
      );
    } else {
      this.authService.logout();
    }
  }
  shouldDisplayMenuItem(menuItem: MenuItem): boolean {
    return menuItem.roles.length === 0 || this.roles.some(role => menuItem.roles.includes(role));
  }
  shouldDisplayMenuCategory(menuCategory: MenuDropdown): boolean {
    return menuCategory.menuDropdownList.some(menuItem => this.shouldDisplayMenuItem(menuItem));
  }
  constructor(public router: Router, private authService: AuthService) {}
  logout() {
    this.authService.logout();
  }
  showNavbar: boolean = false;

  isLoginPage(): boolean {
    return this.router.url === '/login';
  }

  // Listen for scroll events to toggle the "Back to Top" button visibility
  @HostListener('window:scroll', ['$event'])
  onWindowScroll() {
    // If scroll position is more than 300px, show the button with opacity 1; otherwise, opacity 0
    this.showBackToTop = window.scrollY > 300 ? 1 : 0;
  }
  // Scroll back to the top of the page
  scrollToTop() {
    window.scrollTo({ top: 0, behavior: 'smooth' });
  }
}
