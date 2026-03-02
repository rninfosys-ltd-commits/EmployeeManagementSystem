import { Component, OnInit } from '@angular/core';
import { Receipt } from 'src/app/models/receipt.model';
import { ReceiptService } from 'src/app/services/receiptService';
// import html2canvas from 'html2canvas';
// import jsPDF from 'jspdf';
import { Router } from '@angular/router';
import { UserService } from 'src/app/services/UserService';
import jsPDF from 'jspdf';
import autoTable from 'jspdf-autotable';
import { NotificationService } from 'src/app/services/notification.service';


@Component({
  selector: 'app-receipt-list',
  templateUrl: './receipt-list.component.html',
  styleUrls: ['./receipt-list.component.css']
})
export class ReceiptListComponent implements OnInit {

  receipts: Receipt[] = [];
  paginatedReceipts: Receipt[] = [];
  ledger: any[] = [];
  customerInfo: any = {};
  loggedInUser: any = {};

  page = 1;
  pageSize = 6;
  totalPages = 1;

  selectedCustomerId: number | string | null = null;
  todayDate: string = "";

  constructor(
    private receiptService: ReceiptService,
    private router: Router,
    private userService: UserService,
    private notify: NotificationService
  ) { }

  ngOnInit(): void {
    this.loadReceipts();

    const userJson = localStorage.getItem('currentUser');
    console.log("Loaded user from localStorage:", userJson);


    const storedUser = UserService.getUser();
    console.log("LOCAL STORAGE USER = ", storedUser);

    if (userJson) this.loggedInUser = JSON.parse(userJson);

    this.todayDate = new Date().toISOString().split("T")[0];
  }

  loadReceipts() {
    setTimeout(() => {
      this.receiptService.getAll().subscribe({
        next: (res: Receipt[]) => {
          console.log("Fetched receipts:", res);
          this.receipts = res || [];
          this.totalPages = Math.max(1, Math.ceil(this.receipts.length / this.pageSize));
          this.setPage(this.page > this.totalPages ? 1 : this.page);  // 🟢 FIX
        }
      });
    }, 500);

  }


  setPage(p: number) {
    this.page = p;
    const start = (p - 1) * this.pageSize;
    this.paginatedReceipts = this.receipts.slice(start, start + this.pageSize);
    console.log(`Set to page ${p}:`, this.paginatedReceipts);
  }

  prevPage() { if (this.page > 1) this.setPage(this.page - 1); }
  nextPage() { if (this.page < this.totalPages) this.setPage(this.page + 1); }

  normalizeImageForView(image: string | undefined) {
    if (!image) return "";
    if (image.startsWith("data:")) return image;
    return `data:image/jpeg;base64,${image}`;
  }

  openCustomerReceipts(customerId: number | string) {
    this.selectedCustomerId = customerId;

    this.receiptService.getLedger(customerId).subscribe({
      next: (res: any) => {
        this.customerInfo = res.customerInfo || {};
        this.ledger = res.ledger || [];
        this.loggedInUser = res.loggedInUser || this.loggedInUser;

        // ⭐ FIX IMAGE NORMALIZATION
        if (this.loggedInUser?.profileImage)
          this.loggedInUser.profileImage = this.normalizeImageForView(this.loggedInUser.profileImage);

        if (this.customerInfo?.parentImage)
          this.customerInfo.parentImage = this.normalizeImageForView(this.customerInfo.parentImage);

        // ⭐ WAIT for modal animation + DOM update
        setTimeout(() => console.log("Modal ready"), 500);
      }
    });
  }



  async delete(id?: number) {
    if (!id) return;

    const confirmed = await this.notify.confirm({
      title: 'Delete Receipt',
      message: 'Are you sure you want to delete this receipt? This action cannot be undone.',
      confirmText: 'Delete',
      cancelText: 'Cancel',
      type: 'error'
    });

    if (!confirmed) return;

    this.receiptService.delete(id).subscribe(() => {
      this.notify.success('Receipt deleted successfully!');
      this.loadReceipts();

      // Remove from receipts array immediately
      this.receipts = this.receipts.filter(r => r.id !== id);

      // Recalculate total pages
      this.totalPages = Math.max(1, Math.ceil(this.receipts.length / this.pageSize));

      // Adjust current page
      if (this.page > this.totalPages) {
        this.page = this.totalPages;
      }

      // Refresh paginated list instantly
      this.setPage(this.page);

    });
  }



  // handlePDF(customerId?: number | string) {
  //   if (customerId) {
  //     this.selectedCustomerId = customerId;

  //     this.receiptService.getLedger(customerId).subscribe({
  //       next: (res: any) => {
  //         this.customerInfo = res.customerInfo || {};
  //         this.ledger = res.ledger || [];
  //         // Ensure the loggedInUser is updated so the PDF shows the profile data
  //         this.loggedInUser = res.loggedInUser || this.loggedInUser;

  //         // Normalize the image if present
  //         if (this.loggedInUser?.profileImage)
  //           this.loggedInUser.profileImage = this.normalizeImageForView(this.loggedInUser.profileImage);

  //         // ⭐ Wait for Angular + DOM to finish rendering, then generate PDF
  //         setTimeout(() => this.generatePDF(), 800);
  //       }
  //     });

  //     return;
  //   }

  //   setTimeout(() => this.generatePDF(), 800);
  // }

  getBase64Image(image: string | undefined): string | null {
    if (!image) return null;

    // already data url
    if (image.startsWith('data:image')) return image;

    // backend sends only base64 string
    return `data:image/jpeg;base64,${image}`;
  }


  handleDownloadPDF(customerId: number | string | undefined) {
    if (!customerId) {
      this.notify.warning('No customer ID found for this receipt');
      return;
    }

    this.selectedCustomerId = customerId;

    this.receiptService.getLedger(customerId).subscribe({
      next: (res: any) => {
        this.customerInfo = res.customerInfo || {};
        this.ledger = res.ledger || [];
        this.loggedInUser = res.loggedInUser || this.loggedInUser;

        if (this.loggedInUser?.profileImage)
          this.loggedInUser.profileImage = this.normalizeImageForView(this.loggedInUser.profileImage);

        // Now generate the PDF after data is loaded
        setTimeout(() => this.downloadLedgerPDF(), 300);
      },
      error: (err: any) => {
        console.error('Failed to load ledger:', err);
        this.notify.error('Failed to load ledger data');
      }
    });
  }

  downloadLedgerPDF() {
    if (!this.selectedCustomerId || this.ledger.length === 0) {
      this.notify.warning('No ledger data available');
      return;
    }

    const doc = new jsPDF('p', 'mm', 'a4');

    /* ================= HEADER ================= */
    doc.setFontSize(18);
    doc.text('Customer Ledger Report', 105, 15, { align: 'center' });

    /* ================= USER IMAGE ================= */
    const imgBase64 = this.getBase64Image(this.loggedInUser?.profileImage);

    if (imgBase64) {
      doc.addImage(imgBase64, 'JPEG', 14, 22, 25, 25);
    }

    /* ================= USER INFO ================= */
    doc.setFontSize(11);

    doc.text(`ID: ${this.customerInfo.id || ''}`, 45, 28);
    doc.text(`Name: ${this.customerInfo.name || ''}`, 45, 35);

    doc.text(`Mobile: ${this.customerInfo.mobile || ''}`, 140, 28);
    doc.text(`Email: ${this.customerInfo.email || ''}`, 140, 35);

    doc.text(`Generated By: ${this.loggedInUser?.name || 'User'}`, 14, 55);
    doc.text(`Date: ${new Date().toLocaleDateString()}`, 160, 55);

    /* ================= TABLE ================= */
    const tableBody = this.ledger.map((r, i) => [
      r.srNo || i + 1,
      new Date(r.receiptDate).toLocaleDateString(),
      r.transactionTypeLabel,
      r.paymentModeLabel,
      `₹${r.amount}`,
      `₹${r.closingBalance}`
    ]);

    autoTable(doc, {
      startY: 62,
      head: [[
        'Sr No',
        'Date',
        'Type',
        'Mode',
        'Amount',
        'Closing Balance'
      ]],
      body: tableBody,
      styles: {
        fontSize: 10,
        halign: 'center'
      },
      headStyles: {
        fillColor: [79, 70, 229],
        textColor: 255,
        fontStyle: 'bold'
      },
      theme: 'grid'
    });

    /* ================= FOOTER ================= */
    const finalY = (doc as any).lastAutoTable.finalY + 15;
    doc.setFontSize(10);
    doc.text(
      'This is a system generated ledger report.',
      105,
      finalY,
      { align: 'center' }
    );

    /* ================= SAVE ================= */
    doc.save(`Ledger_${this.customerInfo.id}.pdf`);
  }





  // downloadLedgerPDF() {
  //   if (!this.selectedCustomerId) {
  //     alert("Please select a customer first");
  //     return;
  //   }

  //   // ⭐ Wait 600–800ms ALWAYS before generating PDF
  //   setTimeout(() => {
  //     this.handlePDF(this.selectedCustomerId!);
  //   }, 800);
  // }




}
