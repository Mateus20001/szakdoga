import { Component } from '@angular/core';
import { LayoutComponent } from "./layout/layout.component";
import { HttpClientModule } from '@angular/common/http';
import { AuthService } from './auth/auth.service';
import { Router, RouterOutlet } from '@angular/router';
import { CommonModule } from '@angular/common';
@Component({
  selector: 'app-root',
  standalone: true,
  imports: [RouterOutlet, LayoutComponent, HttpClientModule, CommonModule], 
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.scss'],
  providers: [AuthService]
})
export class AppComponent {
  title = 'frontend';
  constructor(public router: Router) {};
  isLoginPage(): boolean {
    return this.router.url === '/login';
  }
}
