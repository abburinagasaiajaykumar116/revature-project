import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { HttpClient } from '@angular/common/http';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { environment } from '../../../../environments/environment';
import { FavoriteService } from '../../../core/services/favorite.service';
import { CartService } from '../../../core/services/cart.service';
import { Observable } from 'rxjs';

@Component({
  selector: 'app-product-detail',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './product-card.component.html'
})
export class ProductCardComponent implements OnInit {

  product: any;
  quantity: number = 1;

  favorites$!: Observable<number[]>;

  
  isAddingToCart: boolean = false;
  cartSuccess = '';
  cartError = '';

  
  rating: number = 5;
  comment: string = '';
  reviews: any[] = [];

  constructor(
    private route: ActivatedRoute,
    private router: Router,
    private http: HttpClient,
    private favoriteService: FavoriteService,
    private cartService: CartService
  ) {
    this.favorites$ = this.favoriteService.favorites$;
  }

  ngOnInit(): void {

    const productId = Number(this.route.snapshot.paramMap.get('id'));

    if (!productId) {
      this.router.navigate(['/']);
      return;
    }

    this.loadProduct(productId);
    this.loadReviews(productId);
  }


  loadProduct(id: number) {
    this.http.get(`${environment.apiBaseUrl}/products/${id}`)
      .subscribe(res => this.product = res);
  }



  toggleWishlist() {

    const token = localStorage.getItem('token');

    if (!token) {
      this.router.navigate(['/my-account']);
      return;
    }

    const productId = this.product.productId;

    if (this.favoriteService.isFavorite(productId)) {
      this.favoriteService.removeFavorite(productId);
    } else {
      this.favoriteService.addFavorite(productId);
    }
  }

  isFavorite(productId: number): boolean {
    return this.favoriteService.isFavorite(productId);
  }



  addToCart() {

    const token = localStorage.getItem('token');

    if (!token) {
      this.router.navigate(['/my-account']);
      return;
    }

    this.isAddingToCart = true;
    this.cartSuccess = '';
    this.cartError = '';

    this.cartService.addToCart(
      this.product.productId,
      this.quantity
    ).subscribe({
      next: () => {

        this.isAddingToCart = false;
        this.cartSuccess = "Product added to cart successfully!";

        setTimeout(() => this.cartSuccess = '', 3000);

        if (this.favoriteService.isFavorite(this.product.productId)) {
          this.favoriteService.removeFavorite(this.product.productId);
        }
      },
      error: () => {
        this.isAddingToCart = false;
        this.cartError = "Failed to add to cart.";
        setTimeout(() => this.cartError = '', 3000);
      }
    });
  }

submitReview() {

  if (!this.comment.trim()) return;

  this.http.post<any>(
    `${environment.apiBaseUrl}/reviews/${this.product.productId}`,
    {
      rating: this.rating,
      comment: this.comment.trim()
    }
  ).subscribe({
    next: (savedReview) => {


      this.reviews = [savedReview, ...this.reviews];

      this.comment = '';
      this.rating = 5;

    },
    error: (err) => {
      console.error(err);
    }
  });
}

loadReviews(productId: number) {
  this.http.get<any[]>(
    `${environment.apiBaseUrl}/reviews/product/${productId}`
  ).subscribe(res => this.reviews = res);
}

}