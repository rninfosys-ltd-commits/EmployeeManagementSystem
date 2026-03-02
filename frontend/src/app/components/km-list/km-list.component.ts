import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { AuthService } from 'src/app/services/auth.service';
import { KmService } from 'src/app/services/KmService';
import jsPDF from 'jspdf';
import autoTable from 'jspdf-autotable';
import { APP_CONFIG } from 'src/app/config/config';



declare var bootstrap: any;

interface KmBatch {
  kmBatchNo?: number;
  trndate?: string | Date;
  createdby?: string;
  totalEntries?: number;
  approvalStage?: string;
  approval1Name?: string;
  approval2Name?: string;
  approval3Name?: string;
}

interface KmEntry {
  id?: number;
  salesperson?: string | null;
  startKm: number | null;
  endKm: number | null;
  visitedPlace?: string | null;
  trnDate?: string | Date | null;
  filePath?: string | null;
  trbactno?: number | null;
  status?: string | null;
}

@Component({
  selector: 'app-km-list',
  templateUrl: './km-list.component.html',
  styleUrls: ['./km-list.component.css']
})
export class KmListComponent implements OnInit {

  batches: KmBatch[] = [];
  filteredBatches: KmBatch[] = [];
  entries: KmEntry[] = [];

  activeFilter = 'ALL';
  isLoading = false;
  currentUserRole = '';

  itemsPerPage = 7;
  currentPage = 1;

  selectedBatchNo?: number;
  selectedBatch?: KmBatch | null;

  editingIndex: number | null = null;
  editModel: any = {};
  // router: any;

  constructor(private kmService: KmService, private authService: AuthService, private router: Router) { }

  ngOnInit(): void {
    const current = this.authService.getCurrentUser();
    this.currentUserRole = current ? current.role : '';
    this.load();
  }

  load() {
    this.isLoading = true;
    this.kmService.getAllBatches().subscribe({
      next: (res: any[]) => {
        this.batches = (res || []).sort((a, b) => (b.kmBatchNo || 0) - (a.kmBatchNo || 0));
        this.applyFilter();
        this.isLoading = false;
      },
      error: () => { this.isLoading = false; }
    });
  }

  setFilter(f: string) {
    this.activeFilter = f;
    this.currentPage = 1;
    this.applyFilter();
  }

  // applyFilter() {
  //   if (this.activeFilter === 'ALL') {
  //     this.filteredBatches = this.batches;
  //     return;
  //   }

  //   let pending = '';
  //   if (this.currentUserRole === 'ROLE_DIRECTOR') pending = 'NONE';
  //   if (this.currentUserRole === 'ROLE_MANAGER') pending = 'L1';
  //   if (this.currentUserRole === 'ROLE_SUPERVISOR') pending = 'L2';

  //   if (this.activeFilter === 'PENDING') {
  //     this.filteredBatches = this.batches.filter(b => (b.approvalStage || 'NONE') === pending);
  //     return;
  //   }
  // }
  applyFilter() {

    // ⭐ ALL — show everything
    if (this.activeFilter === 'ALL') {
      this.filteredBatches = this.batches;
      return;
    }

    let stagePending = '';
    let stageApproved = '';

    // ⭐ Stage mapping same as customer list
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

    // ⭐ PENDING
    if (this.activeFilter === 'PENDING') {
      this.filteredBatches = this.batches.filter(
        b => b.approvalStage === stagePending
      );
      return;
    }

    // ⭐ APPROVED
    if (this.activeFilter === 'APPROVED') {
      this.filteredBatches = this.batches.filter(
        b => b.approvalStage === stageApproved
      );
      return;
    }

    // ⭐ REJECTED (L1 / L2 / L3 logic same as customer)
    if (this.activeFilter === 'REJECTED') {

      if (this.currentUserRole === 'ROLE_DIRECTOR') {
        this.filteredBatches = this.batches.filter(b =>
          b.approvalStage === 'L1_REJECTED' ||
          b.approvalStage === 'L2_REJECTED'
        );
      }

      if (this.currentUserRole === 'ROLE_MANAGER') {
        this.filteredBatches = this.batches.filter(b =>
          b.approvalStage === 'L2_REJECTED' ||
          b.approvalStage === 'L3_REJECTED'
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


  get paginatedBatches() {
    const start = (this.currentPage - 1) * this.itemsPerPage;
    return this.filteredBatches.slice(start, start + this.itemsPerPage);
  }

  get totalPages() {
    return Math.ceil(this.filteredBatches.length / this.itemsPerPage);
  }

  changePage(p: number) {
    if (p >= 1 && p <= this.totalPages) this.currentPage = p;
  }

  openBatchModal(batchNo?: number) {
    if (!batchNo) return;

    this.selectedBatchNo = batchNo;

    this.kmService.getEntriesByBatch(batchNo).subscribe({
      next: (res: KmEntry[]) => {
        this.entries = res || [];
        this.selectedBatch = this.batches.find(b => b.kmBatchNo === batchNo) || null;

        const el = document.getElementById('kmBatchModal');
        if (el) {
          const modal = new bootstrap.Modal(el);
          modal.show();
        }
      }
    });
  }

  startEdit(i: number) {
    this.editingIndex = i;
    this.editModel = { ...this.entries[i] };
  }

  cancelEdit() {
    this.editingIndex = null;
    this.editModel = {};
  }

  saveEdit(id?: number) {
    if (!id) return;

    this.kmService.updateEntry(id, this.editModel).subscribe({
      next: () => {
        Object.assign(this.entries[this.editingIndex!], this.editModel);
        this.cancelEdit();
        alert('Entry updated');
      }
    });
  }

  // canEditDelete() {
  //   return this.currentUserRole === 'ROLE_COMPANY_OWNER' || this.currentUserRole === 'ROLE_DIRECTOR';
  // }

  isAnyApprover() {
    return ['ROLE_DIRECTOR', 'ROLE_MANAGER', 'ROLE_SUPERVISOR'].includes(this.currentUserRole);
  }

  canApprove() {
    const stage = this.selectedBatch?.approvalStage || 'NONE';
    return (
      (this.currentUserRole === 'ROLE_DIRECTOR' && stage === 'NONE') ||
      (this.currentUserRole === 'ROLE_MANAGER' && stage === 'L1') ||
      (this.currentUserRole === 'ROLE_SUPERVISOR' && stage === 'L2')
    );
  }

  canAddBatch(): boolean {
    return this.currentUserRole === 'ROLE_COMPANY_OWNER' || this.currentUserRole === 'ROLE_DIRECTOR';
  }


  approve(batchNo?: number) {
    if (!batchNo) return;

    this.kmService.approveBatch(batchNo).subscribe({
      next: () => {
        alert('KM approved');
        this.router.navigate(['/km-list']);
        // this.selectedBatch = null;
        this.closeModal();
        this.load();
      }
    });
  }

  closeModal() {
    const modalElement = document.getElementById('kmBatchModal');
    if (modalElement) {
      const instance = bootstrap.Modal.getInstance(modalElement);
      if (instance) instance.hide();
    }
  }

  reject(batchNo?: number) {
    if (!batchNo) return;

    const reason = prompt('Enter rejection reason:') || '';
    this.kmService.rejectBatch(batchNo, reason).subscribe({
      next: () => {
        alert('KM rejected');
        this.router.navigate(['/km-list']);
        // this.selectedBatch = null;
        this.closeModal();
        this.load();
      }
    });
  }

  deleteBatch(batchNo?: number) {
    if (!batchNo) return;

    if (!confirm('Delete this batch?')) return;

    this.kmService.deleteBatch(batchNo).subscribe({
      next: () => {
        alert('Batch deleted');
        this.router.navigate(['/km-list']);
        // this.selectedBatch = null;
        this.closeModal();
        this.load();
      }
    });
  }
  isAdmin(): boolean {
    return this.currentUserRole === 'ROLE_COMPANY_OWNER';
  }


  download(batchNo?: number) {
    if (!batchNo || !this.selectedBatch) return;

    const doc = new jsPDF('p', 'mm', 'a4');

    // ================= TITLE =================
    doc.setFontSize(18);
    doc.text('Demo Auto Limited', 105, 15, { align: 'center' });

    doc.setFontSize(11);
    doc.text(`KM Batch No: ${batchNo}`, 14, 25);
    doc.text(
      `Date: ${new Date(this.selectedBatch.trndate || '').toLocaleDateString()}`,
      150,
      25
    );

    doc.text(`Created By: ${this.selectedBatch.createdby || '-'}`, 14, 32);

    // ================= TABLE DATA =================
    const tableBody = this.entries.map((e, i) => [
      i + 1,
      e.salesperson || '',
      e.startKm ?? '',
      e.endKm ?? '',
      e.visitedPlace || '',
      e.trnDate ? new Date(e.trnDate).toLocaleDateString() : ''
    ]);

    autoTable(doc, {
      startY: 40,
      head: [[
        'ID',
        'Salesperson',
        'Start KM',
        'End KM',
        'Visited Place',
        'Date'
      ]],
      body: tableBody,
      styles: {
        fontSize: 9,
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

    // ================= APPROVAL FOOTER =================
    const finalY = (doc as any).lastAutoTable.finalY + 15;

    doc.setFontSize(11);
    doc.text('CHECKED BY', 30, finalY);
    doc.text('REVIEWED BY', 95, finalY);
    doc.text('APPROVED BY', 160, finalY);

    doc.setFontSize(10);
    doc.text(this.selectedBatch.approval1Name || '—', 30, finalY + 8, { align: 'center' });
    doc.text(this.selectedBatch.approval2Name || '—', 95, finalY + 8, { align: 'center' });
    doc.text(this.selectedBatch.approval3Name || '—', 160, finalY + 8, { align: 'center' });

    // ================= SAVE =================
    doc.save(`KM-Batch-${batchNo}.pdf`);
  }


  canEditDelete(): boolean {
    return (
      this.currentUserRole === 'ROLE_COMPANY_OWNER' ||
      this.currentUserRole === 'ROLE_DIRECTOR'
    );
  }

  isL1AlreadyApproved(): boolean {
    if (!this.selectedBatch) return false;

    return (
      this.currentUserRole === 'ROLE_DIRECTOR' &&
      (this.selectedBatch.approvalStage || 'NONE') !== 'NONE'
    );
  }

  getApprovalLevels(batch: any) {
    return {
      checkedBy: {
        name: batch.approval1Name || '',
        level: batch.approval1Name ? 'L1' : ''
      },
      reviewedBy: {
        name: batch.approval2Name || '',
        level: batch.approval2Name ? 'L2' : ''
      },
      approvedBy: {
        name: batch.approval3Name || '',
        level: batch.approval3Name ? 'L3' : ''
      }
    };
  }

  canSeeRejectedFilter(): boolean {
    return this.currentUserRole === 'ROLE_COMPANY_OWNER' || this.currentUserRole === 'ROLE_DIRECTOR';
  }

  canReject(): boolean {
    const stage = this.selectedBatch?.approvalStage || 'NONE';

    // After L2 approval → only L3 can reject
    if (stage === 'L2' && this.currentUserRole !== 'ROLE_SUPERVISOR') {
      return false;
    }

    // After L3 approval → no one can reject
    if (stage === 'L3') {
      return false;
    }

    return (
      this.currentUserRole === 'ROLE_DIRECTOR' ||
      this.currentUserRole === 'ROLE_MANAGER' ||
      this.currentUserRole === 'ROLE_SUPERVISOR'
    );
  }
  // km-list.component.ts
  /** IMAGE URL BUILDER — handles base64 or server path */
  getImageUrl(path: string | undefined | null) {
    if (!path) return '';               // no path
    const trimmed = (path as string).trim();
    // If it's already a base64 data URL (starts with data:image) return as-is
    if (trimmed.startsWith('data:image')) {
      return trimmed;
    }
    // If path already looks like full URL, return it
    if (trimmed.startsWith('http://') || trimmed.startsWith('https://')) {
      return trimmed;
    }
    // Otherwise assume server relative path and prefix host
    return `${APP_CONFIG.BASE_URL}/${trimmed}`;
  }

  /** OPEN IMAGE: open in new tab (works for base64 too) */
  openImage(path: string | undefined | null) {
    const url = this.getImageUrl(path);
    if (!url) return;
    // For base64 the browser can open it in new tab as well
    window.open(url, '_blank');
  }


}
