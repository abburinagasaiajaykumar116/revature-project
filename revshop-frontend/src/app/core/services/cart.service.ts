import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { environment } from '../../../environments/environment';
import { BehaviorSubject } from 'rxjs';
import { tap } from 'rxjs/operators';

@Injectable({
  providedIn: 'root'
})
export class CartService {

  private cartSubject = new BehaviorSubject<any[]>([]);
  cart$ = this.cartSubject.asObservable();

  constructor(private http: HttpClient) {}

  loadCart() {
    this.http.get<any[]>(`${environment.apiBaseUrl}/cart`)
      .subscribe(cart => {
        this.cartSubject.next(cart);
      });
  }
addToCart(productId: number, quantity: number = 1) {
  return this.http.post(
    `${environment.apiBaseUrl}/cart`,
    { productId, quantity }
  ).pipe(
    tap(() => this.loadCart()) 
  );
}
  
updateQuantity(productId: number, quantity: number) {
  return this.http.put(
    `${environment.apiBaseUrl}/cart/${productId}`,
    { quantity },
    { responseType: 'text' }
  );
}

clearCart() {
  return this.http.delete(
    `${environment.apiBaseUrl}/cart/clear`,
    { responseType: 'text' }
  );
}
removeItem(productId: number) {
  return this.http.delete(
    `${environment.apiBaseUrl}/cart/${productId}`,
    { responseType: 'text' }
  );
}


}
