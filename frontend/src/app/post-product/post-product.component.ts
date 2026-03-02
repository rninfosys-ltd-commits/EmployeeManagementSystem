import { Component, Inject } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { MatSnackBar } from '@angular/material/snack-bar';
import { MAT_DIALOG_DATA, MatDialogRef } from '@angular/material/dialog';
import { ProductService } from 'src/app/services/product.service';
import { Product } from 'src/app/models/product.model';

@Component({
  selector: 'app-post-product',
  templateUrl: './post-product.component.html',
  styleUrls: ['./post-product.component.css']
})
export class PostProductComponent {

  productForm: FormGroup;
  selectedFile: File | null = null;
  imagePreview: string | null = null;
  productId?: number;
  isEdit = false;

  constructor(
    private fb: FormBuilder,
    private snackbar: MatSnackBar,
    private productService: ProductService,
    public dialogRef: MatDialogRef<PostProductComponent>,
    @Inject(MAT_DIALOG_DATA) public data: any
  ) {
    this.productForm = this.fb.group({
      name: ['', Validators.required],
      price: ['', Validators.required],
      description: ['', Validators.required]
    });

    // ✅ EDIT MODE
    if (data?.productId) {
      this.productId = data.productId;
      this.isEdit = true;
      this.loadProduct();
    }
  }

  // ===============================
  // LOAD PRODUCT (AUTO POPULATE)
  // ===============================
  loadProduct(): void {
    this.productService.getById(this.productId!).subscribe(product => {
      this.productForm.patchValue({
        name: product.name,
        price: product.unitPrice,
        description: product.description
      });

      // 👇 show existing image
      if (product.img) {
        this.imagePreview = `data:image/jpeg;base64,${product.img}`;
      }
    });
  }

  closeDialog(): void {
    this.dialogRef.close();
  }

  // ===============================
  // IMAGE SELECT
  // ===============================
  onFileSelected(event: any): void {
    const file = event.target.files[0];
    if (!file) return;

    this.selectedFile = file;

    const reader = new FileReader();
    reader.onload = () => {
      this.imagePreview = reader.result as string;
    };
    reader.readAsDataURL(file);
  }

  // ===============================
  // ADD / UPDATE
  // ===============================
  // submit(): void {
  //   if (this.productForm.invalid) {
  //     this.productForm.markAllAsTouched();
  //     return;
  //   }

  //   const product: Product = {
  //     name: this.productForm.value.name,
  //     unitPrice: this.productForm.value.price,
  //     description: this.productForm.value.description,
  //     img: this.imagePreview?.split(',')[1] // 👈 base64 only
  //   };

  //   if (this.isEdit) {
  //     this.productService.update(this.productId!, product).subscribe(() => {
  //       this.snackbar.open('Product updated successfully', 'Close', { duration: 3000 });
  //       this.dialogRef.close(true);
  //     });
  //   } else {
  //     const userId = 1; // 🔴 replace with logged-in user id
  //     this.productService.add(product, userId).subscribe(() => {
  //       this.snackbar.open('Product added successfully', 'Close', { duration: 3000 });
  //       this.dialogRef.close(true);
  //     });
  //   }
  // }
  // addProduct(): void {
  //   if (this.productForm.invalid) {
  //     this.productForm.markAllAsTouched();
  //     return;
  //   }

  //   this.snackbar.open('Product added successfully', 'Close', {
  //     duration: 3000
  //   });

  //   this.dialogRef.close();
  // }

  //   save() {
  //   this.productService
  //     .create(this.product, this.loggedInUser.id)
  //     .subscribe(() => {
  //       this.router.navigate(['/products']);
  //     });
  // }

  submit(): void {
    if (this.productForm.invalid) {
      this.productForm.markAllAsTouched();
      return;
    }

    const payload: Product = {
      name: this.productForm.value.name,
      unitPrice: this.productForm.value.price,
      description: this.productForm.value.description,
      img: this.imagePreview
        ? this.imagePreview.split(',')[1]
        : undefined   // ✅ FIX
    };

    if (this.isEdit && this.productId) {
      // ✅ UPDATE (UNCHANGED)
      this.productService.update(this.productId, payload).subscribe({
        next: () => {
          this.snackbar.open('Product updated successfully', 'Close', { duration: 3000 });
          this.dialogRef.close(true);
        },
        error: () => {
          this.snackbar.open('Update failed', 'Close', { duration: 3000 });
        }
      });

    } else {
      // ✅ CREATE (NEW API)
      this.productService.createForLoggedInUser(payload).subscribe({
        next: () => {
          this.snackbar.open('Product added successfully', 'Close', { duration: 3000 });
          this.dialogRef.close(true);
        },
        error: () => {
          this.snackbar.open('Add failed', 'Close', { duration: 3000 });
        }
      });
    }
  }


}
