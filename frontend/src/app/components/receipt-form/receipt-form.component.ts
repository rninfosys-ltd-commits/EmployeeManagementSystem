import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup } from '@angular/forms';
import { ReceiptService } from 'src/app/services/receiptService';
import { UserService } from 'src/app/services/UserService';
import { Router, ActivatedRoute } from '@angular/router';
import { NotificationService } from 'src/app/services/notification.service';

@Component({
  selector: 'app-receipt-form',
  templateUrl: './receipt-form.component.html',
  styleUrls: ['./receipt-form.component.css']
})
export class ReceiptFormComponent implements OnInit {

  form!: FormGroup;
  isEdit = false;
  id: number | null = null;

  users: any[] = [];
  uploadedFile: File | null = null;
  selectedFileName: string | null = null;

  today = new Date().toISOString().split("T")[0];


  constructor(
    private fb: FormBuilder,
    private receiptService: ReceiptService,
    private userService: UserService,
    private router: Router,
    private route: ActivatedRoute,
    private notify: NotificationService
  ) { }

  ngOnInit(): void {

    this.form = this.fb.group({
      partyId: [''],
      // mobile: [''],
      amount: [''],
      transactionType: ['1'],
      paymentMode: ['0'],
      transactionId: [''],
      receiptDate: [this.today],
      description: ['']
    });

    this.form.get('paymentMode')?.valueChanges.subscribe(val => {
      this.handleTransactionIdVisibility(val);
    });

    this.loadUsers();

    this.id = Number(this.route.snapshot.paramMap.get("id"));
    if (this.id) {
      this.isEdit = true;
      this.loadExisting();
    } else {
      // Set defaults for new form
      this.selectTxnType('1', 'Receive');
      this.selectPayment('0', 'Cash');
    }
  }

  loadUsers() {
    this.userService.getAllUsers().subscribe(res => {
      this.users = res;
      // If editing, find the party label now
      if (this.isEdit) {
        const pId = this.form.get('partyId')?.value;
        const u = res.find((x: any) => x.id == pId);
        if (u) this.partyLabel = u.username;
      }
    });
  }

  loadExisting() {
    this.receiptService.getById(this.id!).subscribe(r => {
      this.form.patchValue(r);

      // Map labels
      const tType = String(r.transactionType);
      const pMode = String(r.paymentMode);

      if (tType === '1') this.txnTypeLabel = 'Receive';
      if (tType === '2') this.txnTypeLabel = 'Payment';

      if (pMode === '0') this.paymentLabel = 'Cash';
      if (pMode === '1') this.paymentLabel = 'UPI';
      if (pMode === '2') this.paymentLabel = 'Bank Transfer';
      if (pMode === '3') this.paymentLabel = 'Card';

      this.handleTransactionIdVisibility(pMode);

      if (r.receiptImage) {
        this.selectedFileName = "(Existing Image)";
      }
    });
  }

  onPartyChange(event: any) {
    const userId = event.target.value;
    const u = this.users.find(x => x.id == userId);
    // if (u) this.form.patchValue({ mobile: u.mobile });
  }

  triggerFile() {
    document.getElementById("fileUpload")?.click();
  }

  onImageSelected(event: any) {
    const file = event.target.files[0];
    if (!file) return;

    this.uploadedFile = file;
    this.selectedFileName = file.name;
  }

  submit() {
    // getRawValue() includes disabled fields (important for backend)
    const formData = new FormData();
    const values = this.form.getRawValue();

    Object.entries(values).forEach(([k, v]) => {
      formData.append(k, v as any);
    });

    // Backend requires createdBy (logged-in user ID)
    const userId = localStorage.getItem('userId');
    if (userId) {
      formData.append('createdBy', userId);
    }

    if (this.uploadedFile) {
      formData.append("receiptImage", this.uploadedFile);
    }

    if (this.isEdit) {
      this.receiptService.update(this.id!, formData).subscribe(() => {
        this.notify.success('Receipt updated successfully!');
        this.router.navigate(['/receipts']);
      });
    } else {
      this.receiptService.create(formData).subscribe(() => {
        this.notify.success('Receipt saved successfully!');
        this.router.navigate(['/receipts']);
      });
    }
  }

  close() {
    this.router.navigate(['/receipts']);
  }

  partyOpen = false;
  txnTypeOpen = false;
  paymentOpen = false;

  partyLabel = '';
  txnTypeLabel = '';
  paymentLabel = '';

  toggleParty() {
    this.partyOpen = !this.partyOpen;
    this.txnTypeOpen = this.paymentOpen = false;
  }

  toggleTxnType() {
    this.txnTypeOpen = !this.txnTypeOpen;
    this.partyOpen = this.paymentOpen = false;
  }

  togglePayment() {
    this.paymentOpen = !this.paymentOpen;
    this.partyOpen = this.txnTypeOpen = false;
  }

  selectParty(u: any) {
    this.form.patchValue({ partyId: u.id });
    this.partyLabel = u.username;
    this.partyOpen = false;
  }

  selectTxnType(val: string, label: string) {
    this.form.patchValue({ transactionType: val });
    this.txnTypeLabel = label;
    this.txnTypeOpen = false;
  }

  selectPayment(val: string, label: string) {
    this.form.patchValue({ paymentMode: val });
    this.paymentLabel = label;
    this.paymentOpen = false;
  }

  handleTransactionIdVisibility(mode: string) {
    if (mode === '0') { // 0 is Cash
      this.form.get('transactionId')?.disable();
      this.form.patchValue({ transactionId: '' });
    } else {
      this.form.get('transactionId')?.enable();
    }
  }



}
