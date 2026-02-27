import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { environment } from '../../../environments/environment';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class AuthService {

  private baseUrl = environment.apiBaseUrl + '/auth';

  constructor(private http: HttpClient) {}

  login(data: any): Observable<any> {
    return this.http.post(`${this.baseUrl}/login`, data);
  }

 register(data: any) {
  return this.http.post(
    `${environment.apiBaseUrl}/auth/register`,
    data
  );
}


  saveAuth(response: any) {
    localStorage.setItem('token', response.token);
    localStorage.setItem('role', response.role);
    localStorage.setItem('userId', response.userId); 
  }

  logout() {
    localStorage.clear();
  }

  getToken() {
    return localStorage.getItem('token');
  }

  getRole() {
    return localStorage.getItem('role');
  }

  isLoggedIn(): boolean {
    return !!this.getToken();
  }
 getSecurityQuestion(email: string) {
  return this.http.get<{question:string}>(
    `${environment.apiBaseUrl}/user/security-question`,
    { params: { email } }
  );
}

forgotPassword(email: string, answer: string, newPassword: string) {
  return this.http.put<{message:string}>(
    `${environment.apiBaseUrl}/user/forgot-password`,
    null,
    {
      params: {
        email,
        answer,
        newPassword
      }
    }
  );
}
}
