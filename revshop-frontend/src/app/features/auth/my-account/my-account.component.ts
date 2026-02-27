import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { AuthService } from '../../../core/services/auth.service';
import { Router } from '@angular/router';

@Component({
  selector: 'app-my-account',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './my-account.component.html',
  styleUrls: ['./my-account.component.css']
})
export class MyAccountComponent {

 activeTab: 'login' | 'register' | 'forgot' = 'login';

forgotStep = 1;
forgotEmail = '';
forgotAnswer = '';
newPassword = '';
confirmPassword = '';
securityQuestionFromDB = '';

  // Login
  loginEmail = '';
  loginPassword = '';

  // Register Fields
name = '';
email = '';
password = '';
role = 'BUYER';

businessDetails = '';
securityQuestion = '';
securityAnswer = '';

successMessage = '';
errorMessage = '';
  constructor(private auth: AuthService, private router: Router) {}

  switchTab(tab: 'login' | 'register') {
    this.activeTab = tab;
    this.errorMessage = '';
  }

  login() {
    this.auth.login({
      email: this.loginEmail.trim(),
      password: this.loginPassword.trim()
    }).subscribe({
      next: (res) => {
        this.auth.saveAuth(res);
        this.router.navigate(['/home']);
      },
      error: () => {
        this.errorMessage = 'Invalid email or password';
      }
    });
  }

  register() {

  this.errorMessage = '';
  this.successMessage = '';

  if (!this.name || !this.email || !this.password ||
      !this.securityQuestion || !this.securityAnswer) {
    this.errorMessage = 'Please fill all required fields';
    return;
  }

  if (this.role === 'SELLER' && !this.businessDetails) {
    this.errorMessage = 'Business details required for seller';
    return;
  }

  const payload = {
    name: this.name,
    email: this.email,
    password: this.password,
    role: this.role,
    businessDetails: this.role === 'SELLER' ? this.businessDetails : null,
    securityQuestion: this.securityQuestion,
    securityAnswer: this.securityAnswer
  };

  this.auth.register(payload).subscribe({
    next: () => {

      this.successMessage = 'Registration successful! Logging you in...';

      // Auto login
      this.loginEmail = this.email;
      this.loginPassword = this.password;

      setTimeout(() => {
        this.login();
      }, 1000);
    },
    error: () => {
      this.errorMessage = 'Registration failed';
    }
  });
}
startForgotPassword() {
  this.activeTab = 'forgot';
  this.forgotStep = 1;
  this.errorMessage = '';
  this.successMessage = '';
}
fetchSecurityQuestion() {

  if (!this.forgotEmail) {
    this.errorMessage = 'Please enter email';
    return;
  }

  this.auth.getSecurityQuestion(this.forgotEmail)
    .subscribe({
      next: (res) => {
        this.securityQuestionFromDB = res.question;
        this.forgotStep = 2;
        this.errorMessage = '';
      },
      error: () => {
        this.errorMessage = 'Email not found';
      }
    });
}
verifyAnswer() {

  if (!this.forgotAnswer) {
    this.errorMessage = 'Please enter answer';
    return;
  }

  this.forgotStep = 3;
  this.errorMessage = '';
}
resetPassword() {

  if (!this.newPassword || !this.confirmPassword) {
    this.errorMessage = 'Please fill all fields';
    return;
  }

  if (this.newPassword !== this.confirmPassword) {
    this.errorMessage = 'Passwords do not match';
    return;
  }

  this.auth.forgotPassword(
      this.forgotEmail,
      this.forgotAnswer,
      this.newPassword
  ).subscribe({
    next: (res) => {
      this.successMessage = res.message;
      this.errorMessage = '';

      setTimeout(() => {
        this.activeTab = 'login';
        this.successMessage = '';
      }, 2000);
    },
    error: () => {
      this.errorMessage = 'Incorrect answer';
    }
  });
}
}
