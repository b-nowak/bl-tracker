import { Component } from '@angular/core';
import { RouterModule } from '@angular/router';
import { AuthService } from './auth.service';

@Component({
  selector: 'app-root',
  standalone: true,
  imports: [RouterModule],
  template: `
  <div class="app-shell">
    <aside class="sidebar">
      <h2>BL Tracker</h2>
      <nav>
        <a routerLink="/">Contractors</a>
        <a routerLink="/login" (click)="logout()">Logout</a>
      </nav>
    </aside>
    <main class="content">
      <router-outlet></router-outlet>
    </main>
  </div>
  `
})
export class AppComponent {
  constructor(private authService: AuthService) {}

  logout() {
    this.authService.logout();
  }
}
