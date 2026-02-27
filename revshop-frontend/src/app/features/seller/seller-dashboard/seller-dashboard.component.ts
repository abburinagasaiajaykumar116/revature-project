import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { HttpClient } from '@angular/common/http';
import { Router } from '@angular/router';
import { environment } from '../../../../environments/environment';
import { SellerReviewsComponent } from '../seller-reviews/seller-reviews.component';
import { SellerOrdersComponent } from '../seller-orders/seller-orders.component';


@Component({
  selector: 'app-seller-dashboard',
  standalone: true,
  imports: [CommonModule, FormsModule,SellerReviewsComponent,SellerOrdersComponent],
  templateUrl: './seller-dashboard.component.html',
  styleUrls: ['./seller-dashboard.component.css']
})
export class SellerDashboardComponent implements OnInit {

  activeTab: string = 'dashboard';
  // Add this
isAddingProduct = false;

  profile: any;
  orders: any[] = [];
  inventory: any[] = [];
  notifications: any[] = [];
  categories = [
  { id: 1, name: 'Tshirt' },
  { id: 2, name: 'Shirt' },
  { id: 3, name: 'Jacket' },
  { id: 4, name: 'Mobile' },
  { id: 5, name: 'Lower' },
  { id: 6, name: 'Accessories' }
];

  editingProduct: any = null;

 newProduct: any = {
  name: '',
  description: '',
  price: 0,
  mrp: 0,
  discount: 0,
  stockQuantity: 0,
  stockThreshold: 0,
  categoryId: null,
  image: null
};
  newPassword: string = '';
// Add Product
addSuccess = '';
addError = '';

// Update Product
updateSuccess = '';
updateError = '';

// Delete Product
deleteSuccess = '';
deleteError = '';

// Change Password
passwordSuccess = '';
passwordError = '';

  constructor(
    private http: HttpClient,
    private router: Router
  ) {}

  ngOnInit(): void {
    this.loadProfile();
  }

  // ================= PROFILE =================

  loadProfile() {
    this.http.get<any>(`${environment.apiBaseUrl}/user/profile`)
      .subscribe(res => {
        this.profile = res;
      });
  }

  // ================= SELLER ORDERS =================

loadOrders() {
  this.http.get<any[]>(
    `${environment.apiBaseUrl}/seller/orders`
  ).subscribe(res => {
    this.orders = res;
  });
}

  // ================= INVENTORY =================
loadInventory() {
  this.http.get<any[]>(
    `${environment.apiBaseUrl}/seller/inventory`
  ).subscribe(res => {
    this.inventory = res;
  });
}
  // ================= ADD PRODUCT =================

  onFileSelected(event: any) {
    this.newProduct.image = event.target.files[0];
  }

addProduct() {

  this.isAddingProduct = true;   // 🔥 START LOADING
  this.addSuccess = '';
  this.addError = '';

  const formData = new FormData();
  formData.append('name', this.newProduct.name);
  formData.append('description', this.newProduct.description);
  formData.append('price', String(this.newProduct.price));
  formData.append('mrp', String(this.newProduct.mrp));
  formData.append('discount', String(this.newProduct.discount));
  formData.append('stockQuantity', String(this.newProduct.stockQuantity));
  formData.append('stockThreshold', String(this.newProduct.stockThreshold));
  formData.append('categoryId', String(this.newProduct.categoryId));

  if (this.newProduct.image) {
    formData.append('image', this.newProduct.image);
  }

  this.http.post<{message:string}>(
    `${environment.apiBaseUrl}/seller`,
    formData
  )
  .subscribe({
    next: (res) => {
      this.isAddingProduct = false;   // ✅ STOP LOADING
      this.addSuccess = res.message;
      this.resetProductForm();
      this.loadInventory();

      setTimeout(() => this.addSuccess = '', 3000);
    },
    error: () => {
      this.isAddingProduct = false;   // ✅ STOP LOADING
      this.addError = "Failed to add product.";
      setTimeout(() => this.addError = '', 3000);
    }
  });
}
  resetProductForm() {
    this.newProduct = {
      name: '',
      description: '',
      price: 0,
      mrp: 0,
      discount: 0,
      stockQuantity: 0,
        stockThreshold: 0,
      categoryId: 1,
      image: null
    };
  }

  // ================= EDIT PRODUCT =================

  editProduct(product: any) {
    this.editingProduct = { ...product };
    this.activeTab = 'editProduct';
  }
updateProduct() {

  this.http.put<{message: string}>(
    `${environment.apiBaseUrl}/seller/${this.editingProduct.productId}`,
    this.editingProduct
  ).subscribe({
    next: (res) => {

      this.updateSuccess = res.message;
      this.updateError = '';

      // Auto clear after 3 sec
      setTimeout(() => {
        this.updateSuccess = '';
      }, 3000);

      this.activeTab = 'inventory';
      this.loadInventory();
    },

    error: (err) => {
      console.error(err);

      this.updateError = "Failed to update product.";
      this.updateSuccess = '';

      setTimeout(() => {
        this.updateError = '';
      }, 3000);
    }
  });

}

  // ================= DELETE PRODUCT =================
deleteProduct(productId: number) {

this.http.delete<{message:string}>(
  `${environment.apiBaseUrl}/seller/${productId}`
).subscribe({
  next: (res) => {
    this.deleteSuccess = res.message;
    this.deleteError = '';
    this.loadInventory();

    setTimeout(() => {
      this.deleteSuccess = '';
    }, 3000);
  },
  error: () => {
    this.deleteError = "Failed to delete product.";
    this.deleteSuccess = '';

    setTimeout(() => {
      this.deleteError = '';
    }, 3000);
  }
});

}

  // ================= NOTIFICATIONS =================

  loadNotifications() {
    this.http.get<any[]>(`${environment.apiBaseUrl}/api/notifications`)
      .subscribe(res => {
        this.notifications = res;
      });
  }

  // ================= CHANGE PASSWORD =================

  changePassword() {

    if (!this.newPassword || this.newPassword.length < 4) {
      this.passwordError = "Password must be at least 4 characters.";
      return;
    }

    this.http.put<{message: string}>(
      `${environment.apiBaseUrl}/user/change-password`,
      { newPassword: this.newPassword }
    ).subscribe({
      next: (res) => {
      this.passwordSuccess = res.message;
this.passwordError = '';
        this.newPassword = '';

        setTimeout(() => {
          this.passwordSuccess = '';
        }, 3000);
      },
      error: () => {
        this.passwordError = "Failed to change password.";
        this.passwordSuccess = '';

        setTimeout(() => {
          this.passwordError = '';
        }, 3000);
      }
    });
  }

  // ================= LOGOUT =================

  logout() {
    localStorage.clear();
    this.router.navigate(['/my-account']);
  }

}
