import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { environment } from '../../../environments/environment';
import { Observable } from 'rxjs';

@Injectable({ providedIn: 'root' })
export class ProductService {

  constructor(private http: HttpClient) {}

  getAllProducts(): Observable<any[]> {
    return this.http.get<any[]>(`${environment.apiBaseUrl}/products`);
  }
  getProductById(id: number) {
  return this.http.get<any>(
    `${environment.apiBaseUrl}/products/${id}`
  );
}
}
