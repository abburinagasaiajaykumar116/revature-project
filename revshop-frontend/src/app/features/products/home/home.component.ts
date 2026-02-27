import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ProductService } from '../../../core/services/product.service';
import { CartService } from '../../../core/services/cart.service';
import { Router } from '@angular/router';
import { FavoriteService } from '../../../core/services/favorite.service';
import { Observable } from 'rxjs';
@Component({
  selector: 'app-home',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './home.component.html',
  styleUrls: ['./home.component.css']
})
export class HomeComponent implements OnInit {

isLoading: boolean = true;
  products: any[] = [];
  groupedProducts: any[][] = [];
  quantityMap: { [key: number]: number } = {};
favorites$!: Observable<number[]>;


  constructor(
    private productService: ProductService,
    private cartService: CartService,
    private router: Router,
    private favoriteService: FavoriteService
  ) {
    this.favorites$ = this.favoriteService.favorites$;
  }

ngOnInit(): void {
  this.isLoading = true;
  this.favoriteService.loadFavorites();

  this.productService.getAllProducts()
    .subscribe(res => {
      this.products = res;
      this.products.forEach(p => {
        this.quantityMap[p.productId] = 1;
      });
      this.groupProducts();
      this.isLoading = false;
    });



}


  groupProducts() {

    this.groupedProducts = [];

    const chunkSize = 3;

    for (let i = 0; i < this.products.length; i += chunkSize) {
      this.groupedProducts.push(
        this.products.slice(i, i + chunkSize)
      );
    }

  }

 
  increaseQty(productId: number) {
    this.quantityMap[productId]++;
  }

  decreaseQty(productId: number) {
    if (this.quantityMap[productId] > 1) {
      this.quantityMap[productId]--;
    }
  }



toggleWishlist(productId: number) {

  const token = localStorage.getItem('token');
  if (!token) {
    this.router.navigate(['/my-account']);
    return;
  }

  if (this.favoriteService.isFavorite(productId)) {
    this.favoriteService.removeFavorite(productId);
  } else {
    this.favoriteService.addFavorite(productId);
  }
}

  
  addToCart(product: any) {

    const token = localStorage.getItem('token');

    if (!token) {
      this.router.navigate(['/my-account']);
      return;
    }

    const quantity = this.quantityMap[product.productId];

    this.cartService.addToCart(product.productId, quantity)
      .subscribe({
        next: () => {

          this.cartService.loadCart();

         
       if (this.favoriteService.isFavorite(product.productId)) {
  this.favoriteService.removeFavorite(product.productId);
}

        },
        error: () => alert('Failed to add to cart')
      });
  }

  viewProduct(productId: number) {
    this.router.navigate(['/product', productId]);
  }

}
