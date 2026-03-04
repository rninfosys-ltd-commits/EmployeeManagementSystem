import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { CastingHallReportService } from '../services/CastingHallReportService';
import { ProductionService } from '../services/ProductionService';
import * as bootstrap from 'bootstrap';
import { Router } from '@angular/router';

import jsPDF from 'jspdf';
import autoTable from 'jspdf-autotable';
import * as XLSX from 'xlsx';
import { AuthService } from '../services/auth.service';
import { WorkflowService } from '../services/workflow.service';
import { FilterService } from '../services/filter.service';
import { HorizontalReportService } from '../services/horizontal-report.service';

@Component({
  selector: 'app-casting-hall-report',
  templateUrl: './casting-hall-report.component.html',
  styleUrls: ['./casting-hall-report.component.css']
})
export class CastingHallReportComponent implements OnInit {
  currentUserRole = '';

  showForm = false;
  reportForm!: FormGroup;
  // ================= IMPORT =================
  showImportModal = false;
  // importPreviewList: any[] = [];
  pagedImportPreview: any[] = [];

  importPageSize = 5;
  importCurrentPage = 1;
  importTotalPages = 0;

  reportList: any[] = [];
  filteredList: any[] = [];
  productionList: any[] = [];

  availableProductionList: any[] = [];
  allProductionList: any[] = [];

  // 🔥 NEW: merged export will use this
  mergedExportList: any[] = [];
  // ================= PAGINATION =================
  pageSize = 5;
  currentPage = 1;
  totalPages = 0;
  pagedList: any[] = [];


  // selectedCasting: any = null;

  editId: number | null = null;
  importColumns: string[] = [];   // Excel headers (unique)
  importPreviewList: any[] = [];  // Excel rows

  filterFromDate = '';
  filterToDate = '';
  selectedCasting: any = null;

  constructor(
    private fb: FormBuilder,
    private service: CastingHallReportService,
    private productionService: ProductionService,
    private auth: AuthService,
    private router: Router,
    private workflowService: WorkflowService,
    private filterService: FilterService,
    private horizontalReportService: HorizontalReportService
  ) { }

  // ================= INIT =================
  ngOnInit(): void {
    this.loadCurrentUserRole(); // 🔥 REQUIRED

    const today = new Date().toISOString().substring(0, 10);

    this.reportForm = this.fb.group({
      reportDate: [today],
      batchNo: ['', Validators.required],
      size: [0],
      bedNo: [0],
      mouldNo: [0],
      castingTime: [''],
      consistency: [0],
      flowInCm: [0],
      castingTempC: [0],
      vt: [0],
      massTemp: [0],
      fallingTestMm: [0],
      testTime: [0],
      // hTime: [0],
      hTime: [''],
      remark: [''],
      shift: ['', Validators.required]
    });

    this.loadReports();
    this.loadProductionBatches();

    this.filterService.fromDate$.subscribe(d => {
      this.filterFromDate = d;
      this.applyFilters();
    });
    this.filterService.toDate$.subscribe(d => {
      this.filterToDate = d;
      this.applyFilters();
    });
  }
  private buildMergedExportData() {
    return this.filteredList.map(casting => {

      const production = this.allProductionList.find(
        p => p.batchNo === casting.batchNo
      ) || {};

      // 🔥 merge production + casting
      return {
        ...production,
        ...casting
      };
    });
  }
  getCurrentTime(): string {
    const now = new Date();

    const hh = String(now.getHours()).padStart(2, '0');
    const mm = String(now.getMinutes()).padStart(2, '0');
    const ss = String(now.getSeconds()).padStart(2, '0');

    return `${hh}:${mm}:${ss} `;
  }
  goToDashboard() {
    this.router.navigate(['/production-dashboard']);
  }

  // ================= LOAD =================
  loadProductionBatches() {
    this.productionService.getAll().subscribe(res => {
      this.allProductionList = res;          // ✅ keep all
      this.filterAvailableBatches();         // ✅ calculate available
    });
  }


  loadReports() {
    this.service.getAll().subscribe(res => {
      this.reportList = res;
      this.applyFilters();
      this.filterAvailableBatches();
      this.updatePagination();      // ⭐ important
    });
  }


  filterAvailableBatches() {
    if (!this.allProductionList.length) return;

    const usedBatchNos = this.reportList.map(r => r.batchNo);

    this.availableProductionList = this.allProductionList.filter(
      p => !usedBatchNos.includes(p.batchNo)
    );
  }


  // ================= FILTER =================
  applyFilters() {
    if (
      this.filterFromDate &&
      this.filterToDate &&
      new Date(this.filterToDate) < new Date(this.filterFromDate)
    ) {
      alert('To Date cannot be earlier than From Date');
      return;
    }

    const from = this.filterFromDate ? new Date(this.filterFromDate).getTime() : null;
    const to = this.filterToDate ? new Date(this.filterToDate + 'T23:59:59').getTime() : null;

    this.filteredList = this.reportList.filter(r => {
      const dateValue = r.reportDate || r.createdDate;
      if (!dateValue) return false;

      const recordDate = new Date(dateValue).getTime();
      return (!from || recordDate >= from) && (!to || recordDate <= to);
    });
    this.currentPage = 1;
    this.updatePagination();
  }

  updatePagination() {
    this.totalPages = Math.ceil(this.filteredList.length / this.pageSize);
    const start = (this.currentPage - 1) * this.pageSize;
    const end = start + this.pageSize;
    this.pagedList = this.filteredList.slice(start, end);
  }

  goToPage(page: number) {
    if (page < 1 || page > this.totalPages) return;
    this.currentPage = page;
    this.updatePagination();
  }

  nextPage() {
    if (this.currentPage < this.totalPages) {
      this.currentPage++;
      this.updatePagination();
    }
  }

  prevPage() {
    if (this.currentPage > 1) {
      this.currentPage--;
      this.updatePagination();
    }
  }


  onDateChange() {
    this.filterService.setFromDate(this.filterFromDate);
    this.filterService.setToDate(this.filterToDate);
  }

  clearFilters() {
    this.filterFromDate = '';
    this.filterToDate = '';
    this.onDateChange();
  }

  openForm() {
    this.showForm = true;
    this.editId = null;

    this.reportForm.reset({
      reportDate: new Date().toISOString().substring(0, 10),
      testTime: this.getCurrentTime(),
      hTime: this.getCurrentTime()
    });

    this.productionList = [...this.availableProductionList];
  }


  edit(row: any) {
    this.editId = row.id;
    this.showForm = true;

    this.reportForm.patchValue(row);

    // ✅ EDIT MODE → show ALL batches
    this.productionList = [...this.allProductionList];
  }


  delete(id: number) {
    if (confirm('Delete this casting report?')) {
      this.service.delete(id).subscribe(() => this.loadReports());
    }
  }

  submit() {

    this.reportForm.patchValue({
      testTime: this.getCurrentTime(),
      hTime: this.getCurrentTime()
    });

    const userId = this.auth.getLoggedInUserId();

    const currentTime = this.getCurrentTime();

    if (!this.editId) {
      this.reportForm.patchValue({
        testTime: currentTime,
        hTime: currentTime
      });
    }

    const payload = {
      ...this.reportForm.value,
      userId,
      branchId: 1,
      orgId: 1
    };

    const req$ = this.editId
      ? this.service.update(this.editId, payload)
      : this.service.save(payload);

    req$.subscribe(() => {
      this.showForm = false;
      this.editId = null;
      this.loadReports();
    });
  }


  cancel() {
    this.showForm = false;
    this.editId = null;
  }

  get noBatchAvailable(): boolean {
    return !this.productionList || this.productionList.length === 0;
  }

  openCastingModal(r: any) {
    this.selectedCasting = r;

    const modalEl = document.getElementById('castingModal');

    console.log('modal element:', modalEl);   // ADD THIS

    if (!modalEl) return;

    const modal = new bootstrap.Modal(modalEl);
    modal.show();
  }


  closeCastingModal() {
    const modalEl = document.getElementById('castingModal');
    if (!modalEl) return;

    const modalInstance = bootstrap.Modal.getInstance(modalEl);
    modalInstance?.hide();
  }


  // ================= EXPORT =================
  formatDate(date: any): string {
    return date ? new Date(date).toLocaleDateString('en-GB') : '';
  }

  exportPDF() {
    if (!this.filterFromDate || !this.filterToDate) {
      alert('Please select date range');
      return;
    }

    this.workflowService.exportReport('CASTING', this.filterFromDate, this.filterToDate, 'pdf').subscribe({
      next: (blob) => {
        const url = URL.createObjectURL(blob);
        const a = document.createElement('a');
        a.href = url;
        a.download = `Casting_Report_${this.filterFromDate}_to_${this.filterToDate}.pdf`;
        a.click();
        URL.revokeObjectURL(url);
      },
      error: () => alert('Failed to export PDF')
    });
  }



  private excelFieldConfig = [

    // ===== COMMON (once) =====
    { label: 'Batch No', key: 'batchNo' },
    { label: 'Production Date', key: 'createdDate', format: 'date' },
    { label: 'Shift', key: 'shift' },

    // ===== PRODUCTION =====
    { label: 'Silo No 1', key: 'siloNo1' },
    { label: 'Liter Weight 1', key: 'literWeight1' },
    { label: 'FA Solid 1', key: 'faSolid1' },

    { label: 'Silo No 2', key: 'siloNo2' },
    { label: 'Liter Weight 2', key: 'literWeight2' },
    { label: 'FA Solid 2', key: 'faSolid2' },

    { label: 'Total Solid', key: 'totalSolid' },
    { label: 'Water Liter', key: 'waterLiter' },
    { label: 'Cement Kg', key: 'cementKg' },
    { label: 'Lime Kg', key: 'limeKg' },
    { label: 'Gypsum Kg', key: 'gypsumKg' },
    { label: 'Sol Oil Kg', key: 'solOilKg' },
    { label: 'AI Power (gm)', key: 'aiPowerGm' },
    { label: 'Temperature (°C)', key: 'tempC' },

    { label: 'Casting Time', key: 'castingTime' },
    { label: 'Production Time', key: 'productionTime' },
    { label: 'Production Remark', key: 'productionRemark' },

    // ===== CASTING =====
    { label: 'Size', key: 'size' },
    { label: 'Bed No', key: 'bedNo' },
    { label: 'Mould No', key: 'mouldNo' },
    { label: 'Consistency', key: 'consistency' },
    { label: 'Flow (cm)', key: 'flowInCm' },
    { label: 'Casting Temp (°C)', key: 'castingTempC' },
    { label: 'V.T.', key: 'vt' },
    { label: 'Mass Temp', key: 'massTemp' },
    { label: 'Falling Test (mm)', key: 'fallingTestMm' },
    { label: 'Test Time', key: 'testTime' },
    { label: 'H Time', key: 'hTime' },
    { label: 'Casting Remark', key: 'remark' },

    // ===== APPROVAL =====
    { label: 'Approval Stage', key: 'approvalStage' },
    { label: 'Approved By L1', key: 'approvedByL1' },
    { label: 'Approved By L2', key: 'approvedByL2' },
    { label: 'Approved By L3', key: 'approvedByL3' }
  ];


  exportExcel() {
    if (!this.filterFromDate || !this.filterToDate) {
      alert('Please select date range');
      return;
    }

    this.workflowService.exportReport('CASTING', this.filterFromDate, this.filterToDate, 'excel').subscribe({
      next: (blob) => {
        const url = URL.createObjectURL(blob);
        const a = document.createElement('a');
        a.href = url;
        a.download = `Casting_Report_${this.filterFromDate}_to_${this.filterToDate}.xlsx`;
        a.click();
        URL.revokeObjectURL(url);
      },
      error: () => alert('Failed to export Excel')
    });
  }



  getCastingApprovalLevels(c: any) {
    return {
      checkedBy: {
        name: c?.approvedByL1 || '',
        level: c?.approvedByL1 ? 'L1' : ''
      },
      reviewedBy: {
        name: c?.approvedByL2 || '',
        level: c?.approvedByL2 ? 'L2' : ''
      },
      approvedBy: {
        name: c?.approvedByL3 || '',
        level: c?.approvedByL3 ? 'L3' : ''
      }
    };
  }




  canApproveCasting(c: any): boolean {
    if (!c) return false;

    const stage = c.approvalStage || 'NONE';

    return (
      (this.currentUserRole === 'ROLE_DIRECTOR' && stage === 'NONE') ||
      (this.currentUserRole === 'ROLE_MANAGER' && stage === 'L1') ||
      (this.currentUserRole === 'ROLE_SUPERVISOR' && stage === 'L2')
    );
  }

  canRejectCasting(c: any): boolean {
    if (!c) return false;

    const stage = c.approvalStage;

    if (stage === 'L3') return false;

    return (
      (this.currentUserRole === 'ROLE_DIRECTOR' && stage === 'NONE') ||
      (this.currentUserRole === 'ROLE_MANAGER' && stage === 'L1') ||
      (this.currentUserRole === 'ROLE_SUPERVISOR' && stage === 'L2')
    );
  }


  approveCasting() {

    this.service.approve(this.selectedCasting.id).subscribe(() => {
      alert('Approved successfully');
      this.reloadSelectedCasting();
      this.loadReports();
    });
  }

  rejectCasting() {

    const reason = prompt('Enter rejection reason');
    if (!reason) return;

    this.service.reject(this.selectedCasting.id, reason).subscribe(() => {
      alert('Rejected successfully');
      this.reloadSelectedCasting();
      this.loadReports();
    });
  }





  reloadSelectedCasting() {
    this.service.getById(this.selectedCasting.id).subscribe(res => {
      this.selectedCasting = res;
    });
  }
  onExportChange(event: Event) {
    const value = (event.target as HTMLSelectElement).value;

    if (value === 'pdf') {
      this.exportPDF();
    } else if (value === 'excel') {
      this.exportExcel();
    } else if (value === 'horizontal') {
      this.exportHorizontalReport();
    }

    // reset dropdown
    (event.target as HTMLSelectElement).value = '';
  }
  downloadCasting(format: string = 'pdf') {
    if (!this.selectedCasting || !this.selectedCasting.batchNo) {
      alert('No batch selected to download');
      return;
    }
    this.workflowService.downloadReport(this.selectedCasting.batchNo, 'CASTING', format).subscribe({
      next: (blob) => {
        const url = URL.createObjectURL(blob);
        const a = document.createElement('a');
        a.href = url;
        const ext = format === 'excel' ? 'xlsx' : 'pdf';
        a.download = `workflow_report_${this.selectedCasting.batchNo}_CASTING.${ext}`;
        a.click();
        URL.revokeObjectURL(url);
      },
      error: () => {
        alert('Failed to download report. Ensure previous stages are completed.');
      }
    });
  }

  /** Download combined horizontal Excel for selected casting batch */
  downloadHorizontalReport(r?: any) {
    const batchNo = r?.batchNo || this.selectedCasting?.batchNo;
    if (!batchNo) { alert('No batch number available'); return; }
    this.horizontalReportService.downloadLifecycleExcel(batchNo, 'CASTING').subscribe({
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
    this.horizontalReportService.downloadExcel(this.filterFromDate, this.filterToDate, undefined, 'CASTING').subscribe({
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
  private castingPdfFields = [
    { label: 'Batch No', key: 'batchNo' },
    { label: 'Date', key: 'createdDate', format: 'date' },

    { label: 'Size', key: 'size' },
    { label: 'Bed No', key: 'bedNo' },
    { label: 'Mould No', key: 'mouldNo' },
    { label: 'Casting Time', key: 'castingTime' },

    { label: 'Consistency', key: 'consistency' },
    { label: 'Flow (cm)', key: 'flowInCm' },
    { label: 'Casting Temp (°C)', key: 'castingTempC' },

    { label: 'V.T.', key: 'vt' },
    { label: 'Mass Temp', key: 'massTemp' },
    { label: 'Falling Test (mm)', key: 'fallingTestMm' },
    { label: 'Test Time', key: 'testTime' },
    { label: 'H Time', key: 'hTime' },

    { label: 'Remark', key: 'remark' },

    { label: 'Approved By L1', key: 'approvedByL1' },
    { label: 'Approved By L2', key: 'approvedByL2' },
    { label: 'Approved By L3', key: 'approvedByL3' }
  ];
  canViewCasting(c: any): boolean {
    if (!c) return false;

    const stage = c.approvalStage || 'NONE';

    switch (this.currentUserRole) {

      case 'ROLE_DIRECTOR':
        return stage === 'NONE'; // pending + rejected

      case 'ROLE_MANAGER':
        return stage === 'L1';

      case 'ROLE_SUPERVISOR':
        return stage === 'L2' || stage === 'L3';

      case 'ROLE_COMPANY_OWNER':
      case 'ROLE_ADMIN':
      case 'ROLE_USER':
        return true;

      default:
        return false;
    }
  }




  loadCurrentUserRole() {
    const role = localStorage.getItem('role') || '';
    this.currentUserRole = role.startsWith('ROLE_')
      ? role
      : `ROLE_${role} `;
  }
  openImportModal() {
    this.showImportModal = true;
  }

  closeImportModal() {
    this.showImportModal = false;
    this.importPreviewList = [];
    this.pagedImportPreview = [];
  }

  // 🔥 Excel → DTO field mapping
  private excelToDtoMap: Record<string, string> = {
    'Batch No': 'batchNo',
    'Size': 'size',
    'Bed No': 'bedNo',
    'Mould No': 'mouldNo',
    'Casting Time': 'castingTime',
    'Consistency': 'consistency',
    'Flow (cm)': 'flowInCm',
    'Casting Temp (°C)': 'castingTempC',
    'V.T.': 'vt',
    'Mass Temp': 'massTemp',
    'Falling Test (mm)': 'fallingTestMm',
    'Test Time': 'testTime',
    'H Time': 'hTime',
    'Casting Remark': 'remark'
  };
  // Columns to show in Import Preview Modal
  importPreviewFields = [
    { label: 'Batch No', key: 'batchNo' },
    { label: 'Size', key: 'size' },
    { label: 'Bed No', key: 'bedNo' },
    { label: 'Mould No', key: 'mouldNo' },
    { label: 'Casting Time', key: 'castingTime' },
    { label: 'Consistency', key: 'consistency' },
    { label: 'Flow (cm)', key: 'flowInCm' },
    { label: 'Casting Temp (°C)', key: 'castingTempC' },
    { label: 'V.T.', key: 'vt' },
    { label: 'Mass Temp', key: 'massTemp' },
    { label: 'Falling Test (mm)', key: 'fallingTestMm' },
    { label: 'Test Time', key: 'testTime' },
    { label: 'H Time', key: 'hTime' },
    { label: 'Remark', key: 'remark' }
  ];

  onExcelSelect(event: any) {
    const file = event.target.files[0];
    if (!file) return;

    this.showImportModal = true;   // OPEN MODAL FIRST

    const reader = new FileReader();

    reader.onload = (e: any) => {
      const workbook = XLSX.read(e.target.result, { type: 'binary' });
      const sheet = workbook.Sheets[workbook.SheetNames[0]];

      // 1️⃣ Read raw rows
      const rawRows: any[] = XLSX.utils.sheet_to_json(sheet, {
        defval: '',
        raw: false
      });

      if (!rawRows.length) {
        alert('Excel file is empty');
        return;
      }

      // 2️⃣ Extract & REMOVE duplicate columns
      const columnSet = new Set<string>();

      rawRows.forEach(row => {
        Object.keys(row).forEach(key => columnSet.add(key.trim()));
      });

      this.importPreviewList = rawRows.map(row => {
        const dto: any = {};

        Object.keys(this.excelToDtoMap).forEach(excelCol => {
          const dtoField = this.excelToDtoMap[excelCol];
          dto[dtoField] = row[excelCol] ?? null;
        });

        // 🔥 REQUIRED DEFAULTS
        dto.userId = 1;
        dto.branchId = 1;
        dto.orgId = 1;

        return dto;
      });


      // 4️⃣ Pagination
      this.importCurrentPage = 1;
      this.updateImportPagination();
    };

    reader.readAsBinaryString(file);

    // reset input so same file can be re-selected
    event.target.value = '';
  }



  updateImportPagination() {
    this.importTotalPages = Math.ceil(
      this.importPreviewList.length / this.importPageSize
    );

    const start = (this.importCurrentPage - 1) * this.importPageSize;
    const end = start + this.importPageSize;

    this.pagedImportPreview =
      this.importPreviewList.slice(start, end);
  }

  goToImportPage(p: number) {
    this.importCurrentPage = p;
    this.updateImportPagination();
  }

  nextImportPage() {
    if (this.importCurrentPage < this.importTotalPages) {
      this.importCurrentPage++;
      this.updateImportPagination();
    }
  }

  prevImportPage() {
    if (this.importCurrentPage > 1) {
      this.importCurrentPage--;
      this.updateImportPagination();
    }
  }
  saveImportedCasting() {
    if (!this.importPreviewList.length) {
      alert('No data to save');
      return;
    }

    const payload = {
      castings: this.importPreviewList
    };


    this.service.importCasting(payload).subscribe({
      next: (res) => {
        alert(`Saved: ${res.savedCount}, Failed: ${res.errorCount} `);

        this.closeImportModal();
        this.loadReports();
      },
      error: () => {
        alert('Import failed');
      }
    });
  }

  onImportSelect(event: Event) {
    const value = (event.target as HTMLSelectElement).value;

    if (value === 'excel') {
      const fileInput = document.querySelector(
        'input[type="file"][accept=".xlsx,.xls"]'
      ) as HTMLInputElement;

      fileInput?.click();
    }

    // reset dropdown
    (event.target as HTMLSelectElement).value = '';
  }

}
