import { Component, OnInit } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { CommonModule } from '@angular/common';
import { environment } from '../../../environments/environment';
import { RouterModule, ActivatedRoute, Router } from '@angular/router';
import { FavoriteService } from '../../core/services/favorite.service';
import { CartService } from '../../core/services/cart.service';
import { Observable } from 'rxjs';
@Component({
  selector: 'app-shop',
  standalone: true,
  imports: [CommonModule, RouterModule],
  templateUrl: './shop.component.html',
  styleUrl: './shop.component.css'
})
export class ShopComponent implements OnInit {
   favorites$!: Observable<number[]>;
  categories: any[] = [];
  products: any[] = [];
  selectedCategory: number | null = null;

  quantityMap: { [key: number]: number } = {};
 

  constructor(
    private http: HttpClient,
    private route: ActivatedRoute,
    private router: Router,
    private favoriteService: FavoriteService,
    private cartService: CartService
  ) {
   this.favorites$ = this.favoriteService.favorites$;
  }

ngOnInit() {

  this.loadCategories();

  


  this.route.queryParams.subscribe(params => {

    const keyword = params['keyword'];

    if (keyword && keyword.trim() !== '') {

      this.selectedCategory = null;

      this.http.get<any[]>(
        `${environment.apiBaseUrl}/products/search`,
        { params: { keyword: keyword.trim() } }
      ).subscribe(res => {
        this.products = res;
        this.initQuantities();
      });

    } else {
      this.loadAllProducts();
    }
  });
}

  // ================= FAVORITES =================



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

  // ================= PRODUCTS =================

  loadCategories() {
    this.http.get<any[]>(`${environment.apiBaseUrl}/categories`)
      .subscribe(res => this.categories = res);
  }

  loadAllProducts() {
    this.selectedCategory = null;

    this.http.get<any[]>(`${environment.apiBaseUrl}/products`)
      .subscribe(res => {
        this.products = res;
        this.initQuantities();
      });
  }

  loadByCategory(categoryId: number) {
    this.selectedCategory = categoryId;

    this.http.get<any[]>(
      `${environment.apiBaseUrl}/products/category/${categoryId}`
    ).subscribe(res => {
      this.products = res;
      this.initQuantities();
    });
  }

  // ================= QUANTITY =================

  initQuantities() {
    this.products.forEach(p => {
      if (!this.quantityMap[p.productId]) {
        this.quantityMap[p.productId] = 1;
      }
    });
  }

  increaseQty(productId: number) {
    this.quantityMap[productId]++;
  }

  decreaseQty(productId: number) {
    if (this.quantityMap[productId] > 1) {
      this.quantityMap[productId]--;
    }
  }

  // ================= CART =================

  addToCart(product: any) {

    const token = localStorage.getItem('token');

    if (!token) {
      this.router.navigate(['/my-account']);
      return;
    }

    const quantity = this.quantityMap[product.productId] || 1;

    this.cartService.addToCart(product.productId, quantity)
      .subscribe({
        next: () => {
          this.cartService.loadCart();
        },
        error: () => alert('Failed to add to cart')
      });
  }

  // ================= VIEW PRODUCT =================

  viewProduct(productId: number) {
    this.router.navigate(['/product', productId]);
  }
}