import { Component ,OnInit} from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Router } from '@angular/router';
import { FormsModule } from '@angular/forms';
import { CommonModule } from '@angular/common';
import { environment } from '../../../../environments/environment';
@Component({
  selector: 'app-buyer-dashboard',
  standalone: true,
  imports: [FormsModule,CommonModule],
  templateUrl: './buyer-dashboard.component.html',
  styleUrl: './buyer-dashboard.component.css'
})
export class BuyerDashboardComponent implements OnInit {
  successMessage: string = '';
errorMessage: string = '';
  activeTab = 'dashboard';
  newPassword: string = '';
  profile: any;
  orders: any[] = [];
  notifications: any[] = [];

  constructor(
    private http: HttpClient,
    private router: Router
  ) {}

  ngOnInit() {
    this.loadProfile();
  }

loadProfile() {
  this.http.get<any>(
    `${environment.apiBaseUrl}/user/profile`
  ).subscribe(res => this.profile = res);
}
loadOrders() {
  this.http.get<any[]>(
    `${environment.apiBaseUrl}/orders/history`
  ).subscribe(res => this.orders = res);
}

loadNotifications() {
  this.http.get<any[]>(
    `${environment.apiBaseUrl}/api/notifications`
  ).subscribe(res => {
    this.notifications = res;
  });
}
  logout() {
    localStorage.clear();
    this.router.navigate(['/my-account']);
  }
changePassword() {

  if (!this.newPassword || this.newPassword.length < 4) {
    this.errorMessage = "Password must be at least 4 characters.";
    return;
  }

this.http.put<{message: string}>(
  `${environment.apiBaseUrl}/user/change-password`,
  { newPassword: this.newPassword }
).subscribe({
  next: (res) => {
    this.successMessage = res.message;
    this.errorMessage = '';
    this.newPassword = '';

    setTimeout(() => {
      this.successMessage = '';
    }, 3000);
  },
  error: () => {
    this.errorMessage = "Failed to change password.";
    this.successMessage = '';
  }
});
}
}