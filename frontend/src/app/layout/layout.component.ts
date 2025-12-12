import { Component, HostListener, Renderer2 } from '@angular/core';
import { Router, RouterOutlet } from '@angular/router';
import { DropdownMenuComponent } from '../dropdown-menu/dropdown-menu.component';
import { CommonModule } from '@angular/common';
import { AuthService } from '../auth/auth.service';
import { MatIconButton } from '@angular/material/button';
import { MenuDropdown, MenuDropdownMenuObjects, MenuItem } from '../shared/constants';
import { Role } from '../models/userSessionEntity';
import { TimetablePlannerComponent } from '../timetable-planner/timetable-planner.component';
import { MatIcon } from '@angular/material/icon';
import { ChatbotComponent } from "../chatbot/chatbot.component";
@Component({
  selector: 'app-layout',
  standalone: true,
  imports: [RouterOutlet, DropdownMenuComponent, CommonModule, MatIconButton, TimetablePlannerComponent, MatIcon, ChatbotComponent],
  templateUrl: './layout.component.html',
  styleUrl: './layout.component.scss',
})

export class LayoutComponent {
  showTimetable: boolean = false;

  profileName: string | null = null;
  roles: Role[] = [];
  menuDropdownItems: MenuDropdown[] = MenuDropdownMenuObjects;
  showBackToTop = 0; // Opacity value (0 or 1)
  remainingTime: number = 0;
  private timerInterval: any;
  
  constructor(public router: Router, private authService: AuthService, private renderer: Renderer2) {}

  ngOnInit() {
    /*this.adjustBackgroundHeight();*/
    this.setSeasonalBackground();
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
    this.timerInterval = setInterval(() => {
      this.updateRemainingTime();
      if (this.remainingTime <= 0) {
        clearInterval(this.timerInterval);
        this.authService.logout();
      }
    }, 1000);
  }
  updateRemainingTime() {
    const expirationTime = localStorage.getItem('expirationTime');
    if (expirationTime) {
      this.remainingTime = Math.max(0, parseInt(expirationTime) - Date.now());
    }
  }
ngAfterViewInit() {
  setTimeout(() => {
    this.setSeasonalBackground();
  });
}
  formatTime(milliseconds: number): string {
    const totalSeconds = Math.floor(milliseconds / 1000);
    const minutes = Math.floor(totalSeconds / 60);
    const seconds = totalSeconds % 60;
    return `${minutes}:${seconds.toString().padStart(2, '0')}`;
  }
  shouldDisplayMenuItem(menuItem: MenuItem): boolean {
    return menuItem.roles.length === 0 || this.roles.some(role => menuItem.roles.includes(role));
  }
  shouldDisplayMenuCategory(menuCategory: MenuDropdown): boolean {
    return menuCategory.menuDropdownList.some(menuItem => this.shouldDisplayMenuItem(menuItem));
  }
  logout() {
    this.authService.logout();
  }
  showNavbar: boolean = false;

  isLoginPage(): boolean {
    return this.router.url === '/login';
  }
  isCourseApplication(): boolean {
    return this.router.url === '/courseApplication';
  }
  @HostListener('window:scroll', ['$event'])
  onWindowScroll() {
    this.showBackToTop = window.scrollY > 300 ? 1 : 0;
  }
  scrollToTop() {
    window.scrollTo({ top: 0, behavior: 'smooth' });
  }
  navigateToProfile() {
    this.router.navigate(['/profile']);
  }
  navigateToMain() {
    this.router.navigate(['/main']);
  }
  /*adjustBackgroundHeight() {
    const contentHeight = document.documentElement.scrollHeight;
    const backgroundContainer = document.querySelector('.background-container') as HTMLElement;

    if (backgroundContainer) {
      this.renderer.setStyle(backgroundContainer, 'height', `${contentHeight}px`);
    }
  }
  ngAfterViewInit() {
    this.adjustBackgroundHeight();
    window.addEventListener('resize', () => this.adjustBackgroundHeight());
  } */
    setSeasonalBackground(): void {
      const month = new Date().getMonth(); // 0 = Jan, 11 = Dec
      let imageUrl = 'url("../../assets/mainbuilding3.jpg")'; // Default background
    
      if (month === 11 || month === 0 || month === 1) {
        // Winter: December, January, February
        imageUrl = 'url("../../assets/mainbuilding-winter.jpeg")';
      } else if (month >= 2 && month <= 4) {
        // Spring
        imageUrl = 'url("../../assets/mainbuilding-spring.jpg")';
      } else if (month >= 5 && month <= 7) {
        // Summer
        imageUrl = 'url("../../assets/mainbuilding3.jpg")';
      } else if (month >= 8 && month <= 10) {
        // Autumn
        imageUrl = 'url("../../assets/mainbuilding3.jpg")';
      }
    
      const bgElement = document.querySelector('.background-container') as HTMLElement;
      console.log(bgElement)
      if (bgElement) {
        this.renderer.setStyle(bgElement, 'backgroundImage', imageUrl);
      }
    }
    
  toggleTimetable(): void {
    this.showTimetable = !this.showTimetable;
  }
}
