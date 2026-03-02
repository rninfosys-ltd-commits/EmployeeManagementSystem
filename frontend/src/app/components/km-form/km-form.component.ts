import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
// import { KmService } from '../services/km.service';
// import { KmEntry } from '../models/km-entry.model';
import { Router } from '@angular/router';
import { forkJoin } from 'rxjs';
import { KmEntry } from 'src/app/models/km-entry.model';
import { AuthService } from 'src/app/services/auth.service';
import { KmService } from 'src/app/services/KmService';
// import { AuthService } from '../services/auth.service';

@Component({
  selector: 'app-km-form',
  templateUrl: './km-form.component.html',
  styleUrls: ['./km-form.component.css']
})
export class KmFormComponent implements OnInit {

  kmForm!: FormGroup;
  cart: KmEntry[] = [];
  selectedFile: File | null = null;
  isSaving = false;
  editIndex: number | null = null;
  currentUserName = 'SYSTEM';

  constructor(
    private fb: FormBuilder,
    private kmService: KmService,
    private router: Router,
    private authService: AuthService
  ) { }

  ngOnInit(): void {
    const today = new Date().toISOString().split('T')[0];
    this.kmForm = this.fb.group({
      salesperson: ['', Validators.required],
      startKm: [null, Validators.required],
      endKm: [null, Validators.required],
      visitedPlace: ['', Validators.required],
      trnDate: [today, Validators.required],
      filePath: ['']
    });

    const current = this.authService.getCurrentUser();
    if (current && current.username) {
      this.currentUserName = current.username;
    }
  }

  // km-form.component.ts
  onFileSelected(e: any) {
    const file: File | undefined = e.target.files?.[0];
    if (!file) {
      this.selectedFile = null;
      return;
    }

    this.selectedFile = file;

    // Convert file to base64 data URL and set into the form control 'filePath'
    const reader = new FileReader();
    reader.onload = () => {
      // reader.result is a string like "data:image/png;base64,...."
      this.kmForm.patchValue({
        filePath: reader.result as string
      });
    };
    reader.onerror = (err) => {
      console.error('File read error', err);
      alert('Failed to read file');
    };

    reader.readAsDataURL(file);
  }


  onAddKm() {
    if (this.kmForm.invalid) {
      this.kmForm.markAllAsTouched();
      return;
    }

    const v = this.kmForm.value;
    const item: KmEntry = {
      salesperson: v.salesperson,
      startKm: v.startKm,
      endKm: v.endKm,
      visitedPlace: v.visitedPlace,
      trnDate: v.trnDate,
      filePath: v.filePath,
      status: 'PENDING'
    };

    if (this.editIndex !== null) {
      this.cart[this.editIndex] = item;
      this.editIndex = null;
    } else {
      this.cart.push(item);
    }

    this.resetFields();
  }

  editCart(index: number) {
    const it = this.cart[index];
    this.editIndex = index;
    this.kmForm.patchValue({
      salesperson: it.salesperson,
      startKm: it.startKm,
      endKm: it.endKm,
      visitedPlace: it.visitedPlace,
      trnDate: it.trnDate,
      filePath: it.filePath || ''
    });
  }

  removeCart(index: number) {
    this.cart.splice(index, 1);
    if (this.editIndex === index) {
      this.editIndex = null;
      this.resetFields();
    }
  }

  resetFields() {
    const today = new Date().toISOString().split('T')[0];
    this.kmForm.patchValue({
      salesperson: '',
      startKm: null,
      endKm: null,
      visitedPlace: '',
      trnDate: today,
      filePath: ''
    });
  }

  cancel() {
    this.cart = [];
    this.resetFields();
    this.router.navigate(['/km-list']);
  }

  submitBatch() {
    if (this.cart.length === 0) {
      alert('Cart empty');
      return;
    }

    this.isSaving = true;

    const header = {
      trndate: this.cart[0].trnDate,
      createdby: this.currentUserName
    };

    this.kmService.createBatch(header).subscribe({
      next: (batch) => {
        const batchNo = batch.kmBatchNo;
        const calls = this.cart.map(item => {
          const payload: KmEntry = { ...item, kmBatchNo: batchNo };
          if (!batchNo) {
            console.error('Invalid batch number');
            return;
          }
          return this.kmService.createEntryWithBatch(Number(batchNo), payload);

        });

        forkJoin(calls).subscribe({
          next: () => {
            this.isSaving = false;
            alert('KM batch created');
            this.cart = [];
            this.resetFields();
            this.router.navigate(['/km-list']);
          },
          error: () => {
            this.isSaving = false;
            alert('Failed to save KM entries');
          }
        });
      },
      error: () => {
        this.isSaving = false;
        alert('Failed to create batch');
      }
    });
  }
}
