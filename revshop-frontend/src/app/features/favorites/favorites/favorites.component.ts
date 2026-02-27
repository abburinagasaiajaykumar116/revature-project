import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { HttpClient } from '@angular/common/http';
import { Router, RouterModule } from '@angular/router';
import { FavoriteService } from '../../../core/services/favorite.service';
import { CartService } from '../../../core/services/cart.service';
import { environment } from '../../../../environments/environment';

export interface FavoriteView {
  productId: number;
  productName: string; 
  description: string; 
  price: number;
  imageUrl: string;
}
@Component({
  selector: 'app-favorites',
  standalone: true,
  
  imports: [CommonModule, RouterModule],
  templateUrl: './favorites.component.html',
  styleUrls: ['./favorites.component.css']
})
export class FavoritesComponent implements OnInit {

  favorites: any[] = [];

  constructor(
    private favoriteService: FavoriteService,
    private cartService: CartService,
    private http: HttpClient,
    private router: Router
  ) {}

  ngOnInit() {
   
    this.favoriteService.favorites$
      .subscribe(ids => {
        this.loadFavoriteProducts(ids);
      });

    this.favoriteService.loadFavorites();
  }

  
  loadFavoriteProducts(ids: number[]) {
    this.http.get<any[]>(`${environment.apiBaseUrl}/favorites`)
      .subscribe({
        next: (res) => this.favorites = res,
        error: (err) => console.error('Could not load favorites', err)
      });
  }

  
  addToCart(productId: number) {
    this.cartService.addToCart(productId, 1).subscribe({
      next: () => {
       
        this.favorites = this.favorites.filter(
          item => item.productId !== productId
        );
     
        this.cartService.loadCart();
      },
      error: () => alert('Failed to add to cart')
    });
  }

 
  remove(productId: number) {
   
    this.favorites = this.favorites.filter(
      item => item.productId !== productId
    );
    
    this.favoriteService.removeFavorite(productId);
  }

  
  viewProduct(productId: number) {
    this.router.navigate(['/product', productId]);
  }

  clearAllFavorites() {
    if (this.favorites.length > 0 && confirm('Clear all items from wishlist?')) {
      this.favorites.forEach(item => {
        this.favoriteService.removeFavorite(item.productId);
      });
      this.favorites = [];
    }
  }
}
