import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterModule, Router } from '@angular/router';
import { AuthService } from '../../core/services/auth.service';
import { CartService } from '../../core/services/cart.service';
import { HostListener } from '@angular/core';
import { ProductService } from '../../core/services/product.service';
import { FormsModule } from '@angular/forms';
@Component({
  selector: 'app-header',
  standalone: true,
  imports: [CommonModule, RouterModule,FormsModule],
  templateUrl: './header.component.html'
})
export class HeaderComponent {
   cartItems: any[] = [];
subtotal = 0;
showCart = false;

searchKeyword: string = '';


  constructor(private auth: AuthService, private router: Router,private cartService:CartService) {}
  ngOnInit() {

  this.cartService.cart$.subscribe(items => {
    this.cartItems = items;
    this.calculateSubtotal();
  });

  this.cartService.loadCart(); // initial load
}

toggleCart() {
  this.showCart = !this.showCart;
}

  isLoggedIn(): boolean {
    return !!localStorage.getItem('token');
  }

  logout() {
    localStorage.clear();
    this.router.navigate(['/my-account']);
  }
calculateSubtotal() {
  this.subtotal = this.cartItems.reduce(
  (sum, item) => sum + (item.price * item.quantity),
  0
);

}

getCartCount(): number {
  return this.cartItems.reduce(
    (sum, item) => sum + item.quantity,
    0
  );
}
@HostListener('document:click', ['$event'])
clickOutside(event: Event) {
  const clickedInside = (event.target as HTMLElement)
    .closest('.icon-item, .cart-dropdown');

  if (!clickedInside) {
    this.showCart = false;
  }
}
openCart() {
  this.showCart = true;
}

closeCart() {
  this.showCart = false;
}

clearCart() {
  this.cartService.clearCart().subscribe({
    next: () => this.cartService.loadCart(),
    error: () => alert('Failed to clear cart')
  });
}
goToCart() {
  this.showCart = false;
  this.router.navigate(['/cart']);
}

goToAccount() {

  const token = localStorage.getItem('token');
  const role = localStorage.getItem('role');

  
  if (!token) {
    this.router.navigate(['/my-account']);
    return;
  }


  if (role === 'SELLER') {
    this.router.navigate(['/seller/dashboard']);
  } else if (role === 'BUYER') {
    this.router.navigate(['/buyer/dashboard']);
  } else {
    // fallback safety
    this.router.navigate(['/my-account']);
  }
}
goToCheckout() {
  this.router.navigate(['/checkout']);
}
searchProducts() {
  if (!this.searchKeyword.trim()) return;

  this.router.navigate(['/shop'], {
    queryParams: { keyword: this.searchKeyword }
  });

  this.searchKeyword = '';
}

}





