import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Router } from '@angular/router';
import { tap } from 'rxjs/operators';

@Injectable({ providedIn: 'root' })
export class AuthService {
  private tokenKey = 'bl_auth_token';

  constructor(private http: HttpClient, private router: Router) {}

  login(username: string, password: string) {
    const token = btoa(`${username}:${password}`);
    const headers = new HttpHeaders({ Authorization: `Basic ${token}` });
    return this.http.get('/api/auth/me', { headers }).pipe(
      tap(() => {
        localStorage.setItem(this.tokenKey, token);
      })
    );
  }

  logout() {
    localStorage.removeItem(this.tokenKey);
    this.router.navigate(['/login']);
  }

  get token(): string | null {
    return localStorage.getItem(this.tokenKey);
  }

  isAuthenticated() {
    return !!this.token;
  }
}
