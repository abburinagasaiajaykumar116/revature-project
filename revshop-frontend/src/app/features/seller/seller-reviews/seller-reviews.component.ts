import { Component, OnInit } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { CommonModule } from '@angular/common';
import { environment } from '../../../../environments/environment';

export interface SellerReview {
  productName: string;
  customerName: string; // Ensure this matches the JSON key from Java
  rating: number;
  comment: string;
}

@Component({
  selector: 'app-seller-reviews',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './seller-reviews.component.html',
  styleUrl: './seller-reviews.component.css'
})
export class SellerReviewsComponent implements OnInit {
  // Initializing as an empty array to prevent HTML errors before data arrives
  reviews: SellerReview[] = [];
  isLoading: boolean = true;

  constructor(private http: HttpClient) {}

  ngOnInit(): void {
    this.loadReviews();
  }

  loadReviews(): void {
    this.isLoading = true;
    this.http.get<SellerReview[]>(`${environment.apiBaseUrl}/seller/reviews`).subscribe({
      next: (data) => {
        console.log('Reviews received from Java:', data); // Check this in F12 Console
        this.reviews = data;
        this.isLoading = false;
      },
      error: (err) => {
        console.error('API Error:', err);
        this.isLoading = false;
      }
    });
  }
}
