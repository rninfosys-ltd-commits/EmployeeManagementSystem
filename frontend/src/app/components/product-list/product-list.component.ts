import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { MatSnackBar } from '@angular/material/snack-bar';
import { MatDialog } from '@angular/material/dialog';
import { PostProductComponent } from 'src/app/post-product/post-product.component';
import { ProductService } from 'src/app/services/product.service';
import { Product } from 'src/app/models/product.model';

@Component({
  selector: 'app-product-list',
  templateUrl: './product-list.component.html',
  styleUrls: ['./product-list.component.css']
})
export class ProductListComponent implements OnInit {

  products: Product[] = [];

  // ✅ INITIALIZE IMMEDIATELY (BEST FIX)
  searchProductForm: FormGroup;

  constructor(
    private productService: ProductService,
    private fb: FormBuilder,
    private snackbar: MatSnackBar,
    private dialog: MatDialog
  ) {
    // ✅ form is READY before template loads
    this.searchProductForm = this.fb.group({
      title: ['']
    });
  }

  ngOnInit(): void {
    this.getAllProducts();

    this.searchProductForm.get('title')?.valueChanges.subscribe(value => {
      if (!value) {
        this.getAllProducts();
      } else {
        this.search(value);
      }
    });
  }

  getAllProducts(): void {
    this.productService.getAll().subscribe({
      next: (res) => {
        console.log('Products fetched:', res);
        this.products = res;
      },
      error: (err) => console.error(err)
    });
  }

  search(value: string): void {
    const term = value.toLowerCase();
    this.products = this.products.filter(p =>
      p.name.toLowerCase().includes(term)
    );
  }

  imageSrc(img?: string): string {
    return img ? `data:image/jpeg;base64,${img}` : '';
  }

  deleteProduct(id?: number): void {
    if (!id) {
      this.snackbar.open('Product ID missing', 'Close', { duration: 3000 });
      return;
    }

    if (!confirm('Are you sure you want to delete this product?')) return;

    this.productService.delete(id).subscribe({
      next: () => {
        this.products = this.products.filter(p => p.id !== id);
        this.snackbar.open('Product deleted successfully', 'Close', { duration: 3000 });
      },
      error: () => {
        this.snackbar.open('Delete failed', 'Close', { duration: 3000 });
      }
    });
  }

  openAddProductModal(): void {
    const dialogRef = this.dialog.open(PostProductComponent, {
      width: '550px',
      panelClass: 'custom-dialog-container'
    });

    dialogRef.afterClosed().subscribe(() => this.getAllProducts());
  }

  openUpdateProductModal(productId?: number): void {
    if (!productId) return;

    const dialogRef = this.dialog.open(PostProductComponent, {
      width: '550px',
      panelClass: 'custom-dialog-container',
      data: { productId } // 👈 PASS ID
    });

    dialogRef.afterClosed().subscribe(() => this.getAllProducts());
  }

}
