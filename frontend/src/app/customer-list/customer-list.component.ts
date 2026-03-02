import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { CustomerService } from '../services/customer.service';
import { CustomerTrn } from '../models/customer.model';
import { AuthService } from '../services/auth.service';
import jsPDF from 'jspdf';
import autoTable from 'jspdf-autotable';


declare var bootstrap: any;

@Component({
  selector: 'app-customer-list',
  templateUrl: './customer-list.component.html',
  styleUrls: ['./customer-list.component.css']
})
export class CustomerListComponent implements OnInit {

  batches: any[] = [];
  filteredBatches: any[] = [];
  batchTransactions: CustomerTrn[] = [];

  selectedBatchNo?: number;
  selectedBatch: any = null;

  currentPage = 1;
  itemsPerPage = 7;

  isLoading = false;
  errorMessage = '';
  isBatchApproving = false;
  // isL1AlreadyApproved: boolean = false;


  currentUserRole = '';
  activeFilter: string = 'ALL';

  editingIndex: number | null = null;
  editModel: any = {};

  constructor(
    private customerService: CustomerService,
    private router: Router,
    private authService: AuthService
  ) { }

  ngOnInit(): void {
    const current = this.authService.getCurrentUser();
    this.currentUserRole = current ? current.role : '';
    this.loadBatches();
  }

  // ============================================================
  // LOAD BATCHES
  // ============================================================
  loadBatches(): void {
    this.isLoading = true;

    this.customerService.getAllBatches().subscribe({
      next: (data) => {
        this.batches = data.sort((a: any, b: any) => b.bactno - a.bactno);
        this.applyFilter();
        this.isLoading = false;
      },
      error: () => {
        this.errorMessage = 'Failed to load batches.';
        this.isLoading = false;
      }
    });
  }

  // ============================================================
  // FILTER LOGIC (EXACT MATCH OF YOUR BATCH SYSTEM)
  // ============================================================
  setFilter(filter: string) {
    this.activeFilter = filter;
    this.currentPage = 1;
    this.applyFilter();
  }

  applyFilter() {
    if (this.activeFilter === 'ALL') {
      this.filteredBatches = this.batches;
      return;
    }

    let stagePending = '';
    let stageApproved = '';

    if (this.currentUserRole === 'ROLE_DIRECTOR') {
      stagePending = 'NONE';
      stageApproved = 'L1';
    }

    if (this.currentUserRole === 'ROLE_MANAGER') {
      stagePending = 'L1';
      stageApproved = 'L2';
    }

    if (this.currentUserRole === 'ROLE_SUPERVISOR') {
      stagePending = 'L2';
      stageApproved = 'L3';
    }

    if (this.activeFilter === 'PENDING') {
      this.filteredBatches = this.batches.filter(b => b.approvalStage === stagePending);
      return;
    }

    if (this.activeFilter === 'APPROVED') {
      this.filteredBatches = this.batches.filter(b => b.approvalStage === stageApproved);
      return;
    }

    // ------------------- REJECTION LOGIC -----------------------
    if (this.activeFilter === 'REJECTED') {
      if (this.currentUserRole === 'ROLE_DIRECTOR') {
        this.filteredBatches = this.batches.filter(b =>
          b.approvalStage === 'L1_REJECTED' || b.approvalStage === 'L2_REJECTED'
        );
      }

      if (this.currentUserRole === 'ROLE_MANAGER') {
        this.filteredBatches = this.batches.filter(b =>
          b.approvalStage === 'L2_REJECTED' || b.approvalStage === 'L3_REJECTED'
        );
      }

      if (this.currentUserRole === 'ROLE_SUPERVISOR') {
        this.filteredBatches = this.batches.filter(b =>
          b.approvalStage === 'L3_REJECTED'
        );
      }

      return;
    }
  }

  // ============================================================
  // PAGINATION
  // ============================================================
  get paginatedBatches() {
    const start = (this.currentPage - 1) * this.itemsPerPage;
    return this.filteredBatches.slice(start, start + this.itemsPerPage);
  }

  get totalPages() {
    return Math.ceil(this.filteredBatches.length / this.itemsPerPage);
  }

  changePage(page: number) {
    if (page >= 1 && page <= this.totalPages) this.currentPage = page;
  }

  // ============================================================
  // MODAL OPEN
  // ============================================================
  openBatchModal(bactno: number): void {
    this.selectedBatchNo = bactno;
    this.selectedBatch = this.batches.find(b => b.bactno === bactno) || null;

    this.customerService.getCustomersByBatch(bactno).subscribe({
      next: (data: any[]) => {
        this.batchTransactions = data || [];

        if (data.length > 0 && data[0].batchDetails) {
          this.selectedBatch = data[0].batchDetails;
        }

        const modalElement = document.getElementById('batchModal');
        if (modalElement) {
          let modal = new bootstrap.Modal(modalElement);
          modal.show();
        }
      },
      error: () => alert('Failed to load batch transactions')
    });
  }

  addCustomer(): void {
    this.router.navigate(['/customers/add']);
  }

  // ============================================================
  // PERMISSION LOGIC (UPDATED AS YOU REQUESTED)
  // ============================================================
  canEdit(): boolean {
    return this.currentUserRole === 'ROLE_COMPANY_OWNER' || this.currentUserRole === 'ROLE_DIRECTOR';
  }

  canDelete(): boolean {
    return (
      this.currentUserRole === 'ROLE_COMPANY_OWNER' ||
      this.currentUserRole === 'ROLE_DIRECTOR' ||
      this.currentUserRole === 'ROLE_MANAGER' ||
      this.currentUserRole === 'ROLE_SUPERVISOR'
    );
  }

  canReject() {
    // After L2 approval, only L3 can reject
    if (this.selectedBatch?.approvalStage === 'L2' && this.currentUserRole !== 'ROLE_SUPERVISOR') {
      return false;
    }

    return (
      this.currentUserRole === 'ROLE_DIRECTOR' ||
      this.currentUserRole === 'ROLE_MANAGER' ||
      this.currentUserRole === 'ROLE_SUPERVISOR'
    );
  }


  canDownload(): boolean {
    return (
      this.currentUserRole === 'ROLE_DIRECTOR' ||
      this.currentUserRole === 'ROLE_MANAGER' ||
      this.currentUserRole === 'ROLE_SUPERVISOR'
    );
  }

  canApprove(): boolean {
    const stage = this.selectedBatch?.approvalStage || 'NONE';

    return (
      (this.currentUserRole === 'ROLE_DIRECTOR' && stage === 'NONE') ||
      (this.currentUserRole === 'ROLE_MANAGER' && stage === 'L1') ||
      (this.currentUserRole === 'ROLE_SUPERVISOR' && stage === 'L2')
    );
  }

  // ============================================================
  // APPROVE
  // ============================================================
  approveBatch(bactno: number): void {
    if (!confirm('Approve this batch?')) return;

    this.isBatchApproving = true;

    this.customerService.approveBatch(bactno).subscribe({
      next: () => {
        this.isBatchApproving = false;
        alert('Batch approved successfully!');
        this.closeModal();
        this.loadBatches();
      },
      error: () => {
        this.isBatchApproving = false;
        alert('Approval failed');
      }
    });
  }

  isL1AlreadyApproved(): boolean {
    if (!this.selectedBatch) return false;
    return (
      this.currentUserRole === 'ROLE_DIRECTOR' &&
      this.selectedBatch.approvalStage !== 'NONE'
    );
  }


  // ============================================================
  // REJECT
  // ============================================================
  rejectBatch(): void {
    if (!this.selectedBatchNo) return;

    const reason = prompt('Enter rejection reason:', '');
    if (reason === null) return;

    this.customerService.rejectBatch(this.selectedBatchNo, reason).subscribe({
      next: () => {
        alert('Batch rejected');
        this.closeModal();
        this.loadBatches();
      },
      error: (err) => {
        if (err.error?.message === "NO_AUTH") {
          alert("❌ You don’t have authority to reject this batch.");
        } else {
          alert("Reject failed.");

        }
        this.closeModal();
        this.loadBatches();
      }
    });
  }


  // ============================================================
  // DELETE
  // ============================================================
  deleteBatch(): void {
    if (!this.selectedBatchNo) return;
    if (!confirm('Delete batch and all transactions?')) return;

    this.customerService.deleteBatch(this.selectedBatchNo).subscribe({
      next: () => {
        alert('Batch deleted');
        this.batches = this.batches.filter(b => b.bactno !== this.selectedBatchNo);
        this.applyFilter();
        this.closeModal();
      },
      error: () => alert('Delete failed')
    });
  }

  // ============================================================
  // DOWNLOAD
  // ============================================================
  downloadBatch(bactno: number) {
    if (!this.selectedBatch || this.batchTransactions.length === 0) {
      alert('No data to download');
      return;
    }

    const doc = new jsPDF('l', 'mm', 'a4'); // landscape for wide table

    /* ================= HEADER ================= */
    doc.setFontSize(18);
    doc.text('Demo Auto Limited', 148, 15, { align: 'center' });

    doc.setFontSize(11);
    doc.text(`Batch No: ${bactno}`, 14, 25);
    doc.text(
      `Transaction Date: ${this.batchTransactions[0]?.trndate
        ? new Date(this.batchTransactions[0].trndate).toLocaleDateString()
        : ''
      }`,
      14,
      32
    );

    doc.text(
      `Created By: ${this.selectedBatch.createdby || this.selectedBatch.createdByName || '—'
      }`,
      14,
      39
    );

    /* ================= TABLE ================= */
    const tableBody = this.batchTransactions.map((t, i) => [
      i + 1,
      t.customerName || '',
      t.toolingdrawingpartno || '',
      t.partdrawingname || '',
      t.partdrawingno || '',
      t.descriptionoftooling || '',
      t.cmworkorderno || '',
      t.toolingassetno || ''
    ]);

    autoTable(doc, {
      startY: 45,
      head: [[
        'TRN ID',
        'Customer Name',
        'Tooling Drawing Part No',
        'Part Drawing Name',
        'Part Drawing No',
        'Description of Tooling',
        'Work Order No',
        'Tooling Asset No'
      ]],
      body: tableBody,
      styles: {
        fontSize: 8,
        halign: 'center',
        valign: 'middle'
      },
      headStyles: {
        fillColor: [79, 70, 229],
        textColor: 255,
        fontStyle: 'bold'
      },
      theme: 'grid'
    });

    /* ================= FOOTER (APPROVALS) ================= */
    const finalY = (doc as any).lastAutoTable.finalY + 12;

    const approvals = this.getApprovalLevels(this.selectedBatch);

    doc.setFontSize(11);
    doc.text('CHECKED BY', 60, finalY, { align: 'center' });
    doc.text('REVIEWED BY', 148, finalY, { align: 'center' });
    doc.text('APPROVED BY', 235, finalY, { align: 'center' });

    doc.setFontSize(10);
    doc.text(approvals.checkedBy.name || '—', 60, finalY + 8, { align: 'center' });
    doc.text(approvals.reviewedBy.name || '—', 148, finalY + 8, { align: 'center' });
    doc.text(approvals.approvedBy.name || '—', 235, finalY + 8, { align: 'center' });

    doc.text(approvals.checkedBy.level || '', 60, finalY + 14, { align: 'center' });
    doc.text(approvals.reviewedBy.level || '', 148, finalY + 14, { align: 'center' });
    doc.text(approvals.approvedBy.level || '', 235, finalY + 14, { align: 'center' });

    /* ================= SAVE ================= */
    doc.save(`Batch-${bactno}.pdf`);
  }

  // ============================================================
  // APPROVER NAMES IN FOOTER
  // ============================================================
  getApprovalLevels(batch: any) {
    return {
      checkedBy: {
        name: batch.aproval1Name || '',
        level: batch.aproval1Name ? 'Director' : ''
      },
      reviewedBy: {
        name: batch.aproval2Name || '',
        level: batch.aproval2Name ? 'Manager' : ''
      },
      approvedBy: {
        name: batch.aproval3Name || '',
        level: batch.aproval3Name ? 'Supervisor' : ''
      }
    };
  }

  // ============================================================
  // INLINE EDIT
  // ============================================================
  startEdit(index: number) {
    this.editingIndex = index;
    const item = this.batchTransactions[index];

    this.editModel = {
      customerName: item.customerName,
      toolingdrawingpartno: item.toolingdrawingpartno,
      partdrawingname: item.partdrawingname,
      partdrawingno: item.partdrawingno,
      descriptionoftooling: item.descriptionoftooling,
      cmworkorderno: item.cmworkorderno,
      toolingassetno: item.toolingassetno
    };
  }

  cancelEdit() {
    this.editingIndex = null;
    this.editModel = {};
  }

  saveEdit(id: number | undefined) {
    if (!id) {
      alert('Invalid ID');
      return;
    }

    this.customerService.updateCustomer(id, this.editModel).subscribe({
      next: () => {
        alert('Transaction updated');
        Object.assign(this.batchTransactions[this.editingIndex!], this.editModel);
        this.cancelEdit();
      },
      error: () => alert('Update failed')
    });
  }

  // ============================================================
  // CLOSE MODAL
  // ============================================================
  closeModal() {
    const modalElement = document.getElementById('batchModal');
    if (modalElement) {
      const instance = bootstrap.Modal.getInstance(modalElement);
      if (instance) instance.hide();
    }
  }


  canEditDelete() {
    return this.currentUserRole === 'ROLE_COMPANY_OWNER' || this.currentUserRole === 'ROLE_DIRECTOR';
  }
  canAddTransaction(): boolean {
    return this.currentUserRole === 'ROLE_COMPANY_OWNER' || this.currentUserRole === 'ROLE_DIRECTOR';
  }

  canSeeRejectedFilter(): boolean {
    return this.currentUserRole === 'ROLE_COMPANY_OWNER' || this.currentUserRole === 'ROLE_DIRECTOR';
  }
  isAdmin(): boolean {
    return this.currentUserRole === 'ROLE_COMPANY_OWNER';
  }


}
