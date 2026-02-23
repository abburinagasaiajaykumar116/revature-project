import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { CartService } from '../../../core/services/cart.service';
import { Router } from '@angular/router';

@Component({
  selector: 'app-cart',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './cart.component.html'
})
export class CartComponent implements OnInit {

  cartItems: any[] = [];
  subtotal = 0;

  constructor(private cartService: CartService,
              private router: Router) {}

  ngOnInit() {
    this.cartService.cart$.subscribe(items => {
      this.cartItems = items;
      this.calculateTotals();
    });

    this.cartService.loadCart();
  }

  calculateTotals() {
    this.subtotal = this.cartItems.reduce(
      (sum, item) => sum + (item.price * item.quantity),
      0
    );
  }

  increaseQty(item: any) {
    this.cartService.updateQuantity(item.productId, item.quantity + 1)
      .subscribe(() => this.cartService.loadCart());
  }

  decreaseQty(item: any) {
    if (item.quantity > 1) {
      this.cartService.updateQuantity(item.productId, item.quantity - 1)
        .subscribe(() => this.cartService.loadCart());
    }
  }

 removeItem(item: any) {
  this.cartService.removeItem(item.productId)
    .subscribe(() => this.cartService.loadCart());
}


  clearCart() {
    this.cartService.clearCart()
      .subscribe(() => this.cartService.loadCart());
  }

 proceedCheckout() {
  this.router.navigate(['/checkout']);
}

}
