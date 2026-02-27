import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { BehaviorSubject } from 'rxjs';
import { environment } from '../../../environments/environment';

@Injectable({
  providedIn: 'root'
})
export class FavoriteService {

  private favoriteSubject = new BehaviorSubject<number[]>([]);
  favorites$ = this.favoriteSubject.asObservable();

  constructor(private http: HttpClient) {}


  loadFavorites() {
    const token = localStorage.getItem('token');
    if (!token) {
      this.favoriteSubject.next([]);
      return;
    }

    this.http.get<any[]>(`${environment.apiBaseUrl}/favorites`)
      .subscribe(res => {
        const ids = res.map((f: any) => f.productId);
        this.favoriteSubject.next([...ids]); 
      });
  }

  addFavorite(productId: number) {

    const current = this.favoriteSubject.value;

   
    this.favoriteSubject.next([...current, productId]);

    this.http.post(
      `${environment.apiBaseUrl}/favorites/${productId}`, {}
    ).subscribe({
      error: () => {
        
        this.favoriteSubject.next(current);
      }
    });
  }

  removeFavorite(productId: number) {

    const current = this.favoriteSubject.value;
    const updated = current.filter(id => id !== productId);

  
    this.favoriteSubject.next([...updated]);

    this.http.delete(
      `${environment.apiBaseUrl}/favorites/${productId}`
    ).subscribe({
      error: () => {
      
        this.favoriteSubject.next(current);
      }
    });
  }

  isFavorite(productId: number): boolean {
    return this.favoriteSubject.value.some(id => Number(id) === Number(productId));
  }
}
