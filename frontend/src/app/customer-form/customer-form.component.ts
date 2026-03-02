import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { Router, ActivatedRoute } from '@angular/router';
import { forkJoin } from 'rxjs';
import { CustomerService } from '../services/customer.service';
import { CustomerTrn } from '../models/customer.model';
import { AuthService } from '../services/auth.service';

@Component({
  selector: 'app-customer-form',
  templateUrl: './customer-form.component.html',
  styleUrls: ['./customer-form.component.css']
})
export class CustomerFormComponent implements OnInit {

  customerForm!: FormGroup;
  isSaving = false;
  errorMessage = '';
  cartItems: CustomerTrn[] = [];

  editingIndex: number | null = null;   // <-- NEW (Track which item is being edited)
  currentUserName: string = '';

  constructor(
    private fb: FormBuilder,
    private customerService: CustomerService,
    private router: Router,
    private route: ActivatedRoute,
    private authService: AuthService
  ) { }

  ngOnInit(): void {
    const today = new Date().toLocaleDateString('en-CA');

    this.customerForm = this.fb.group({
      trndate: [today, Validators.required],
      createdby: [''],
      aproval1: [''],
      aproval2: [''],
      aproval3: [''],
      aproval4: [''],

      customerName: ['', [Validators.required, Validators.minLength(2)]],
      toolingdrawingpartno: ['', Validators.required],
      partdrawingname: ['', Validators.required],
      partdrawingno: ['', Validators.required],
      descriptionoftooling: ['', Validators.required],
      cmworkorderno: ['', Validators.required],
      toolingassetno: ['', Validators.required]
    });

    this.authService.getCurrentUserFromAPI().subscribe({
      next: (res) => {
        this.currentUserName = res.username;
        this.customerForm.patchValue({ createdby: this.currentUserName });
      },
      error: () => {
        this.currentUserName = 'SYSTEM';
        this.customerForm.patchValue({ createdby: 'SYSTEM' });
      }
    });
  }

  // ------------------------------------------
  // ADD OR UPDATE CART ITEM
  // ------------------------------------------
  addToCart(): void {
    if (this.customerForm.invalid) {
      this.customerForm.markAllAsTouched();
      return;
    }

    const v = this.customerForm.value;
    const createdBy = this.currentUserName || 'SYSTEM';

    const item: CustomerTrn = {
      trbactno: undefined,
      trndate: v.trndate,
      createdby: createdBy,
      customerName: v.customerName,
      toolingdrawingpartno: v.toolingdrawingpartno,
      partdrawingname: v.partdrawingname,
      partdrawingno: v.partdrawingno,
      descriptionoftooling: v.descriptionoftooling,
      cmworkorderno: v.cmworkorderno,
      toolingassetno: v.toolingassetno,
      status: 'PENDING'
    };

    // IF EDITING → UPDATE EXISTING
    if (this.editingIndex !== null) {
      this.cartItems[this.editingIndex] = item;
      this.editingIndex = null;
    } else {
      // ADD NEW ITEM
      this.cartItems.push(item);
    }

    this.resetTransactionFields();
  }

  // ------------------------------------------
  // LOAD ITEM INTO FORM FOR EDITING
  // ------------------------------------------
  editCartItem(index: number): void {
    const item = this.cartItems[index];
    this.editingIndex = index;

    this.customerForm.patchValue({
      customerName: item.customerName,
      toolingdrawingpartno: item.toolingdrawingpartno,
      partdrawingname: item.partdrawingname,
      partdrawingno: item.partdrawingno,
      descriptionoftooling: item.descriptionoftooling,
      cmworkorderno: item.cmworkorderno,
      toolingassetno: item.toolingassetno
    });
  }

  // ------------------------------------------
  // REMOVE ITEM
  // ------------------------------------------
  removeFromCart(index: number): void {
    this.cartItems.splice(index, 1);

    // If deleting the one being edited
    if (this.editingIndex === index) {
      this.editingIndex = null;
      this.resetTransactionFields();
    }
  }

  // ------------------------------------------
  // RESET ONLY TRANSACTION FIELDS
  // ------------------------------------------
  resetTransactionFields() {
    this.customerForm.patchValue({
      customerName: '',
      toolingdrawingpartno: '',
      partdrawingname: '',
      partdrawingno: '',
      descriptionoftooling: '',
      cmworkorderno: '',
      toolingassetno: ''
    });

    [
      'customerName',
      'toolingdrawingpartno',
      'partdrawingname',
      'partdrawingno',
      'descriptionoftooling',
      'cmworkorderno',
      'toolingassetno'
    ].forEach(ctrl => {
      this.customerForm.get(ctrl)?.markAsPristine();
      this.customerForm.get(ctrl)?.markAsUntouched();
    });
  }

  // ------------------------------------------
  // SAVE BATCH + ITEMS
  // ------------------------------------------
  saveTransactions(): void {
    if (this.cartItems.length === 0) {
      alert('Cart is empty. Add at least one transaction.');
      return;
    }

    const trndateCtrl = this.customerForm.get('trndate');
    if (!trndateCtrl || trndateCtrl.invalid) {
      trndateCtrl?.markAsTouched();
      alert('Please fill Transaction Date.');
      return;
    }

    this.isSaving = true;
    this.errorMessage = '';

    const v = this.customerForm.value;
    const createdBy = this.currentUserName || 'SYSTEM';

    const header = {
      trndate: v.trndate,
      createdby: createdBy,
      aproval1: v.aproval1,
      aproval2: v.aproval2,
      aproval3: v.aproval3,
      aproval4: v.aproval4
    };

    // 1) Create batch
    this.customerService.createBatch(header).subscribe({
      next: (batch) => {
        const bactno = batch.bactno;

        const calls = this.cartItems.map(c => {
          const payload: CustomerTrn = {
            ...c,
            trbactno: bactno,
            trndate: header.trndate,
            createdby: createdBy,
            status: c.status || 'PENDING'
          };
          return this.customerService.createTransactionWithBatch(bactno, payload);
        });

        forkJoin(calls).subscribe({
          next: () => {
            this.isSaving = false;
            alert('Batch and transactions saved successfully!');
            this.cartItems = [];
            this.customerForm.reset();
            this.customerForm.patchValue({ createdby: createdBy });
            this.router.navigate(['/customers']);
          },
          error: () => {
            this.isSaving = false;
            this.errorMessage = 'Failed to save transactions.';
          }
        });
      },
      error: () => {
        this.isSaving = false;
        this.errorMessage = 'Failed to create batch.';
      }
    });
  }

  cancel(): void {
    this.errorMessage = '';
    this.isSaving = false;
    this.cartItems = [];
    this.customerForm.reset();
    this.customerForm.patchValue({ createdby: this.currentUserName });
    this.router.navigate(['/customers']);
  }

}
