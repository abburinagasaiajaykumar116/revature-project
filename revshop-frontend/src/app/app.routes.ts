import { Routes } from '@angular/router';
import { authGuard } from './core/guards/auth.guard';
import { roleGuard } from './core/guards/role.guard';
import { FavoritesComponent } from './features/favorites/favorites/favorites.component';

export const routes: Routes = [

  {
  path: 'my-account',
  loadComponent: () =>
    import('./features/auth/my-account/my-account.component')
      .then(m => m.MyAccountComponent)
},{
  path: 'home',
  loadComponent: () =>
    import('./features/products/home/home.component')
      .then(m => m.HomeComponent)
},{
  path: 'checkout',
  loadComponent: () =>
    import('./features/checkout/checkout.component')
      .then(m => m.CheckoutComponent)
},
{ path: 'wishlist',component:FavoritesComponent},
{
  path: 'product/:id',
  loadComponent: () =>
    import('./features/products/product-card/product-card.component')
      .then(m => m.ProductCardComponent)
}
,
{
  path: 'buyer/dashboard',
  canActivate: [authGuard, roleGuard],
  data: { role: 'BUYER' },
  loadComponent: () =>
    import('./features/buyer/buyer-dashboard/buyer-dashboard.component')
      .then(m => m.BuyerDashboardComponent)
}
,
{
  path: 'shop',
  loadComponent: () =>
    import('./pages/shop/shop.component')
      .then(m => m.ShopComponent)
},
  {
    path: 'cart',
    canActivate: [authGuard, roleGuard],
    data: { role: 'BUYER' },
    loadComponent: () =>
      import('./features/cart/cart/cart.component')
        .then(m => m.CartComponent)
  },

  {
    path: 'seller/dashboard',
    canActivate: [authGuard, roleGuard],
    data: { role: 'SELLER' },
    loadComponent: () =>
      import('./features/seller/seller-dashboard/seller-dashboard.component')
        .then(m => m.SellerDashboardComponent)
  },
{
  path: 'seller/dashboard',
  loadComponent: () =>
    import('./features/seller/seller-dashboard/seller-dashboard.component')
      .then(m => m.SellerDashboardComponent)
},
 { path: '', redirectTo: 'home', pathMatch: 'full' }
];
