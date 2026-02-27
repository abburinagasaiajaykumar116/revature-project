import { Component,OnInit } from '@angular/core';
import { CommonModule } from '@angular/common'; // Required for *ngFor
import { HttpClient } from '@angular/common/http';
import { environment } from '../../../../environments/environment';

export interface SellerOrderView {
  orderId: number;
  productName: string;
  customerName: string; // New field from your updated Java DTO
  quantity: number;
  price: number;
  status: string;
  shippingAddress: string;
}
@Component({
  selector: 'app-seller-orders',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './seller-orders.component.html',
  styleUrl: './seller-orders.component.css'
})
export class SellerOrdersComponent implements OnInit {
  orders: SellerOrderView[] = [];
  isLoading: boolean = true;

  constructor(private http: HttpClient) {}

  ngOnInit(): void {
    this.fetchOrders();
  }

  fetchOrders(): void {
    this.isLoading = true;
    // Calling your @GetMapping("/seller/orders") endpoint
    this.http.get<SellerOrderView[]>(`${environment.apiBaseUrl}/seller/orders`).subscribe({
      next: (data) => {
        this.orders = data;
        this.isLoading = false;
      },
      error: (err) => {
        console.error('Error fetching seller orders:', err);
        this.isLoading = false;
      }
    });
  }
}
