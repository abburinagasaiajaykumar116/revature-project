import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { CartService } from '../../core/services/cart.service';
import { HttpClient } from '@angular/common/http';
import { environment } from '../../../environments/environment';
import { Router } from '@angular/router';

@Component({
  selector: 'app-checkout',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './checkout.component.html'
})
export class CheckoutComponent implements OnInit {

  cartItems: any[] = [];
  subtotal = 0;

  shippingAddress = '';
  billingAddress = '';
  paymentMethod = 'COD';

  constructor(
    private cartService: CartService,
    private http: HttpClient,
    private router: Router
  ) {}

  ngOnInit() {
    this.cartService.cart$.subscribe(items => {
      this.cartItems = items;
      this.calculateTotal();
    });

    this.cartService.loadCart();
  }

  calculateTotal() {
    this.subtotal = this.cartItems.reduce(
      (sum, item) => sum + (item.price * item.quantity),
      0
    );
  }

  placeOrder() {

  if (!this.shippingAddress || !this.billingAddress) {
    alert('Please fill all required fields');
    return;
  }

  const body = {
    shippingAddress: this.shippingAddress,
    billingAddress: this.billingAddress,
    paymentMethod: this.paymentMethod
  };

  this.http.post(
    `${environment.apiBaseUrl}/orders/checkout`,
    body
  ).subscribe({
    next: (res: any) => {

      alert(res.message + " #" + res.orderId);

      this.cartService.loadCart();
      this.router.navigate(['/home']);
    },
    error: (err) => {
      console.log(err);
      alert('Checkout failed');
    }
  });
}}
