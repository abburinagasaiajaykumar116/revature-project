import { Component } from '@angular/core';
import { RouterOutlet } from '@angular/router';
import { HeaderComponent } from './layout/header/header.component';
import { FooterComponent } from './layout/footer/footer.component';
import { FavoriteService } from './core/services/favorite.service';
@Component({
  selector: 'app-root',
  standalone: true,
  imports: [RouterOutlet, HeaderComponent, FooterComponent],
  templateUrl: './app.component.html'
})
export class AppComponent {
  title="revshop-frontend";
    constructor(private favoriteService: FavoriteService) {}

  ngOnInit() {
    this.favoriteService.loadFavorites();
  }
}
