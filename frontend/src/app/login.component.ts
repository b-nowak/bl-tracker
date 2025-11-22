import { Component } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { Router } from '@angular/router';
import { AuthService } from './auth.service';
import { CommonModule } from '@angular/common';

@Component({
  standalone: true,
  imports: [FormsModule, CommonModule],
  template: `
  <div class="content">
    <div class="card" style="max-width:420px;margin:2rem auto;">
      <h2>Login</h2>
      <form (ngSubmit)="login()">
        <label>Username</label>
        <input [(ngModel)]="username" name="username" required>
        <label>Password</label>
        <input [(ngModel)]="password" type="password" name="password" required>
        <button class="button" type="submit">Login</button>
        <div *ngIf="error" style="color:red;margin-top:0.5rem;">{{error}}</div>
      </form>
    </div>
  </div>
  `
})
export class LoginComponent {
  username = '';
  password = '';
  error = '';

  constructor(private authService: AuthService, private router: Router) {}

  login() {
    this.authService.login(this.username, this.password).subscribe({
      next: () => this.router.navigate(['/']),
      error: () => this.error = 'Invalid credentials'
    });
  }
}
