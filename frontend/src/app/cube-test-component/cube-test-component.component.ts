import { Component, OnInit } from '@angular/core';
import { FormGroup, FormBuilder } from '@angular/forms';
import { CubeTestService } from '../services/CubeTestService';
import * as XLSX from 'xlsx';
import jsPDF from 'jspdf';
import autoTable from 'jspdf-autotable';
import { BlockSeparatingService } from '../services/BlockSeparatingService';
import { AuthService } from '../services/auth.service';
import * as bootstrap from 'bootstrap';
import { Router } from '@angular/router';
import { WorkflowService } from '../services/workflow.service';
import { FilterService } from '../services/filter.service';
import { HorizontalReportService } from '../services/horizontal-report.service';


@Component({
  selector: 'app-cube-test',
  templateUrl: './cube-test-component.component.html',
  styleUrls: ['./cube-test-component.component.css']
})
export class CubeTestComponent implements OnInit {

  form!: FormGroup;

  list: any[] = [];
  filtered: any[] = [];
  paginated: any[] = [];
  batches: any[] = [];
  selectedCube: any = null;
  currentRole = '';

  showLeads = true;
  editId: any = null;

  filterFromDate = '';
  filterToDate = '';
  filterShift = '';

  currentPage = 1;
  pageSize = 5;
  totalPages = 0;

  constructor(private fb: FormBuilder, private service: CubeTestService,
    private auth: AuthService,
    private blockService: BlockSeparatingService,
    private router: Router,
    private workflowService: WorkflowService,
    private filterService: FilterService,
    private horizontalReportService: HorizontalReportService
  ) { }

  ngOnInit() {

    const today = new Date().toISOString().split('T')[0];

    this.form = this.fb.group({
      batchNo: [''],
      reportDate: [today],     // ✅ fixed name
      castDate: [''],          // ✅ must exist
      testingDate: [''],
      shift: [''],

      cubeDimensionImmediate: [''],
      cubeDimensionOvenDry1: [''],
      cubeDimensionOvenDry2: [''],
      cubeDimensionOvenDry3: [''],
      weightImmediateKg: [''],
      weightovenDryKg: [''],
      weightWithMoistureKg: [''],
      loadovenDryTonn: [''],
      loadMoistureTonn: [''],
      compStrengthovenDry: [''],
      compStrengthMoisture: [''],
      densityKgM3: [''],
      isActive: [1]
    });

    this.load();
    this.loadBatches();
    const role = localStorage.getItem('role') || '';
    this.currentRole = role.startsWith('ROLE_') ? role : `ROLE_${role}`;

    this.filterService.fromDate$.subscribe(d => {
      this.filterFromDate = d;
      this.applyFilters();
    });
    this.filterService.toDate$.subscribe(d => {
      this.filterToDate = d;
      this.applyFilters();
    });
  }


  // ================= LOAD =================

  load() {
    this.service.getAll().subscribe(r => {
      this.list = r;
      this.filtered = [...r];
      this.setupPagination();
    });
  }
  get noBatchAvailable(): boolean {
    return !this.batches || this.batches.length === 0;
  }
  goToDashboard() {
    this.router.navigate(['/production-dashboard']);
  }

  // ================= PAGINATION =================

  setupPagination() {
    this.totalPages = Math.ceil(this.filtered.length / this.pageSize);
    this.currentPage = 1;
    this.updatePage();
  }

  updatePage() {
    const start = (this.currentPage - 1) * this.pageSize;
    this.paginated = this.filtered.slice(start, start + this.pageSize);
  }
  loadBatches() {
    this.blockService.getAll().subscribe(res => {
      this.batches = res;
      console.log('Batches:', this.batches); // 🔥 debug
    });
  }

  nextPage() {
    if (this.currentPage < this.totalPages) {
      this.currentPage++;
      this.updatePage();
    }
  }

  prevPage() {
    if (this.currentPage > 1) {
      this.currentPage--;
      this.updatePage();
    }
  }

  // ================= FILTERS =================

  applyFilters() {
    // ✅ validate date range first
    if (this.filterFromDate && this.filterToDate) {

      const from = new Date(this.filterFromDate).setHours(0, 0, 0, 0);
      const to = new Date(this.filterToDate).setHours(23, 59, 59, 999);

      if (to < from) {
        alert('To Date cannot be less than From Date');
        this.filterToDate = '';
        return;
      }
    }

    this.filtered = this.list.filter(r => {

      // 🔥 USE createdDate instead of castDate
      const rowDate = new Date(r.createdDate).getTime();

      const from = this.filterFromDate
        ? new Date(this.filterFromDate).setHours(0, 0, 0, 0)
        : null;

      const to = this.filterToDate
        ? new Date(this.filterToDate).setHours(23, 59, 59, 999)
        : null;

      if (from && rowDate < from) return false;
      if (to && rowDate > to) return false;

      // SHIFT FILTER
      if (this.filterShift && r.shift !== this.filterShift) return false;

      return true;
    });

    this.setupPagination();
  }



  onDateChange() {
    this.filterService.setFromDate(this.filterFromDate);
    this.filterService.setToDate(this.filterToDate);
  }

  clearFilters() {
    this.filterFromDate = '';
    this.filterToDate = '';
    this.filterShift = '';
    this.onDateChange();
  }

  // ================= FORM =================
  openCreate() {
    const today = new Date().toISOString().split('T')[0];

    this.form.reset({
      reportDate: today,   // ✅ keep today after reset
      isActive: 1
    });

    this.editId = null;
    this.showLeads = false;
  }


  edit(row: any) {
    this.form.patchValue(row);
    this.editId = row.id;
    this.showLeads = false;
  }

  submit() {
    const userId = this.auth.getLoggedInUserId();


    const payload = {
      ...this.form.value,
      userId: userId
    };

    console.log('FINAL PAYLOAD:', payload); // 🔥

    const req = this.editId
      ? this.service.update(this.editId, payload)
      : this.service.create(payload);

    req.subscribe(() => {
      alert('Saved');
      this.showLeads = true;
      this.load();
    });
  }


  backToList() {
    this.showLeads = true;
  }

  // ================= EXPORT =================

  onExportChange(event: any) {

    if (event.target.value === 'excel') this.exportExcel();
    if (event.target.value === 'pdf') this.exportPdf();
    if (event.target.value === 'horizontal') this.exportHorizontalReport();

    event.target.value = '';
  }

  exportExcel() {
    if (!this.filterFromDate || !this.filterToDate) {
      alert('Please select date range');
      return;
    }
    this.workflowService.exportReport('CUBE_TEST', this.filterFromDate, this.filterToDate, 'excel').subscribe({
      next: (blob) => {
        const url = URL.createObjectURL(blob);
        const a = document.createElement('a');
        a.href = url;
        a.download = `CubeTest_Report_${this.filterFromDate}_to_${this.filterToDate}.xlsx`;
        a.click();
        URL.revokeObjectURL(url);
      },
      error: () => alert('Failed to export Excel')
    });
  }

  exportPdf() {
    if (!this.filterFromDate || !this.filterToDate) {
      alert('Please select date range');
      return;
    }
    this.workflowService.exportReport('CUBE_TEST', this.filterFromDate, this.filterToDate, 'pdf').subscribe({
      next: (blob) => {
        const url = URL.createObjectURL(blob);
        const a = document.createElement('a');
        a.href = url;
        a.download = `CubeTest_Report_${this.filterFromDate}_to_${this.filterToDate}.pdf`;
        a.click();
        URL.revokeObjectURL(url);
      },
      error: () => alert('Failed to export PDF')
    });
  }

  // ================= IMPORT =================

  onImportSelect(event: any) {
    if (event.target.value === 'excel') {
      document.querySelector('input[type=file]')?.dispatchEvent(new MouseEvent('click'));
    }
    event.target.value = '';
  }

  onExcelSelect(event: any) {
    alert('Import coming later');
  }

  // onBatchChange(event: any) {

  //   const batchNo = event.target.value;

  //   const selected = this.list.find(x => x.batchNumber == batchNo);

  //   if (selected) {
  //     this.form.patchValue({
  //       castDate: selected.castingDate
  //     });
  //   }
  // }
  onBatchSelect(event: any) {

    const batchNo = event.target.value;

    console.log('Selected value:', batchNo);
    console.log('All batches:', this.batches);

    const selected = this.batches.find(
      (x: any) => String(x.batchNumber) === String(batchNo)
    );

    console.log('Selected batch:', selected);

    if (selected) {
      this.form.patchValue({
        castDate: selected.castingDate
      });
    }
  }

  delete(id: number) {

    if (!confirm('Are you sure you want to delete this record?')) return;

    this.service.delete(id).subscribe(() => {
      alert('Deleted successfully');
      this.load();   // reload table
    });

  }

  openCubeModal(r: any) {

    console.log('CLICKED:', r);

    this.selectedCube = r;

    const modalEl = document.getElementById('cubeModal');

    console.log('MODAL:', modalEl);

    if (!modalEl) return;

    const modal = new bootstrap.Modal(modalEl);
    modal.show();
  }


  closeCubeModal() {
    const modalEl = document.getElementById('cubeModal');
    if (!modalEl) return;

    bootstrap.Modal.getInstance(modalEl)?.hide();
  }

  getApprovalLevels(c: any) {
    return {
      checkedBy: { name: c?.approvedByL1 || '', level: 'L1' },
      reviewedBy: { name: c?.approvedByL2 || '', level: 'L2' },
      approvedBy: { name: c?.approvedByL3 || '', level: 'L3' }
    };
  }

  canApprove(c: any): boolean {
    if (!c) return false;

    const stage = c.approvalStage || 'NONE';

    return (
      (this.currentRole === 'ROLE_DIRECTOR' && stage === 'NONE') ||
      (this.currentRole === 'ROLE_MANAGER' && stage === 'L1') ||
      (this.currentRole === 'ROLE_SUPERVISOR' && stage === 'L2')
    );
  }


  canReject(c: any): boolean {
    if (!c) return false;

    const stage = c.approvalStage || 'NONE';

    if (stage === 'NONE' && this.currentRole === 'ROLE_DIRECTOR') return true;
    if (stage === 'L1' && this.currentRole === 'ROLE_MANAGER') return true;
    if (stage === 'L2' && this.currentRole === 'ROLE_SUPERVISOR') return true;

    return false;
  }



  approveCube() {
    if (!this.selectedCube) return;

    const userId = this.auth.getLoggedInUserId();
    const role = this.currentRole;

    this.service.approve(this.selectedCube.id, userId, role)
      .subscribe(() => {
        alert('Approved successfully');
        this.closeCubeModal();
        this.load();
      });
  }

  rejectCube() {
    if (!this.selectedCube) return;

    const reason = prompt('Enter rejection reason');
    if (!reason) return;

    const userId = this.auth.getLoggedInUserId();
    const role = this.currentRole;

    this.service.reject(this.selectedCube.id, reason, userId, role)
      .subscribe(() => {
        alert('Rejected successfully');
        this.closeCubeModal();
        this.load();
      });
  }

  downloadCube(format: string = 'pdf') {
    if (!this.selectedCube || !this.selectedCube.batchNo) {
      alert('No batch selected to download');
      return;
    }
    this.workflowService.downloadReport(this.selectedCube.batchNo, 'CUBE_TEST', format).subscribe({
      next: (blob) => {
        const url = URL.createObjectURL(blob);
        const a = document.createElement('a');
        a.href = url;
        const ext = format === 'excel' ? 'xlsx' : 'pdf';
        a.download = `workflow_report_${this.selectedCube.batchNo}_CUBE_TEST.${ext}`;
        a.click();
        URL.revokeObjectURL(url);
      },
      error: () => { alert('Failed to download report.'); }
    });
  }

  /** Download combined horizontal Excel for this cube test batch */
  downloadHorizontalReport(r?: any) {
    const batchNo = r?.batchNo || this.selectedCube?.batchNo;
    if (!batchNo) { alert('No batch number available'); return; }
    this.horizontalReportService.downloadLifecycleExcel(batchNo, 'CUBE_TEST').subscribe({
      next: (blob) => {
        const url = URL.createObjectURL(blob);
        const a = document.createElement('a');
        a.href = url;
        a.download = `horizontal_report_${batchNo}.xlsx`;
        a.click();
        URL.revokeObjectURL(url);
      },
      error: () => alert('Failed to download horizontal report.')
    });
  }

  /** Export horizontal report for the selected date range */
  exportHorizontalReport() {
    if (!this.filterFromDate || !this.filterToDate) { alert('Please select a date range first'); return; }
    this.horizontalReportService.downloadExcel(this.filterFromDate, this.filterToDate, undefined, 'CUBE_TEST').subscribe({
      next: (blob) => {
        const url = URL.createObjectURL(blob);
        const a = document.createElement('a');
        a.href = url;
        a.download = `horizontal_report_${this.filterFromDate}_to_${this.filterToDate}.xlsx`;
        a.click();
        URL.revokeObjectURL(url);
      },
      error: () => alert('Failed to export horizontal report.')
    });
  }

}
