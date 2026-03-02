import { Component } from '@angular/core';
import { Router } from '@angular/router';
import { Product } from 'src/app/models/product.model';
import { ProductService } from 'src/app/services/product.service';

@Component({
  selector: 'app-product-form',
  templateUrl: './product-form.component.html',
  styleUrls: ['./product-form.component.css']
})
export class ProductFormComponent {

  product: Product = {
    name: '',
    unitPrice: 0,
    description: '',
    img: ''
  };

  previewImage: string | null = null;

  loggedInUser: any = JSON.parse(localStorage.getItem('user')!);

  constructor(
    private productService: ProductService,
    private router: Router
  ) { }

  // ===============================
  // IMAGE UPLOAD
  // ===============================
  onImageSelected(event: any) {
    const file = event.target.files[0];
    if (!file) return;

    const reader = new FileReader();
    reader.onload = () => {
      const base64 = reader.result as string;

      // store only base64 (backend friendly)
      this.product.img = base64.split(',')[1];

      // full base64 for preview
      this.previewImage = base64;
    };

    reader.readAsDataURL(file);
  }

  // ===============================
  // SAVE PRODUCT
  // ===============================
  save() {
    this.productService
      .create(this.product, this.loggedInUser.id)
      .subscribe(() => {
        this.router.navigate(['/products']);
      });
  }
}
