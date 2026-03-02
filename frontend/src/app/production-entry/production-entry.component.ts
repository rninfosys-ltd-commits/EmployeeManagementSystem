import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup } from '@angular/forms';
import { ProductionService } from '../services/ProductionService';
import { MaterialMasterService, MaterialMaster } from '../services/MaterialMasterService';
import * as bootstrap from 'bootstrap';
import { ProductionImportResponse } from '../models/ProductionImportResponse';
import jsPDF from 'jspdf';
import autoTable from 'jspdf-autotable';
import * as XLSX from 'xlsx';
import { AuthService } from '../services/auth.service';
import { Router } from '@angular/router';
import { WorkflowService } from '../services/workflow.service';
import { HorizontalReportService } from '../services/horizontal-report.service';

@Component({
  selector: 'app-production-entry',
  templateUrl: './production-entry.component.html',
  styleUrls: ['./production-entry.component.css']
})
export class ProductionEntryComponent implements OnInit {

  currentUserRole = '';

  selectedProduction: any = null;

  productionForm!: FormGroup;

  productionList: any[] = [];
  filteredProductionList: any[] = [];

  showForm = false;
  editId: number | null = null;

  siloList: number[] = [];
  showImportModal = false;
  excelHeaders: string[] = [];
  excelRows: any[] = [];

  filterFromDate = '';
  filterToDate = '';
  excelPreview: any[] = [];
  hasExcelErrors = false;
  apiMessage = '';

  // ================= PAGINATION =================
  pageSize = 5;
  currentPage = 1;
  totalPages = 0;
  pagedProductionList: any[] = [];

  // ================= DYNAMIC MATERIALS =================
  materialList: MaterialMaster[] = [];
  materialValues: { [materialId: number]: number } = {};

  constructor(
    private fb: FormBuilder,
    private service: ProductionService,
    private materialService: MaterialMasterService,
    private auth: AuthService,
    private router: Router,
    private workflowService: WorkflowService,
    private horizontalReportService: HorizontalReportService
  ) { }

  ngOnInit(): void {

    console.log('ROLE IN COMPONENT:', this.currentUserRole);

    this.loadCurrentUserRole();

    this.siloList = Array.from({ length: 5 }, (_, i) => i + 1);

    const today = new Date().toISOString().substring(0, 10);
    this.filterFromDate = today;
    this.filterToDate = today;

    this.productionForm = this.fb.group({
      shift: [''],
      productionDate: [today],

      siloNo1: [''],
      literWeight1: [''],
      faSolid1: [''],

      siloNo2: [''],
      literWeight2: [''],
      faSolid2: [''],

      castingTime: [''],
      productionTime: [''],
      productionRemark: [''],
      remark: [''],

      userId: [1],
      branchId: [1],
      orgId: [1]
    });

    this.setShiftByTime();
    this.loadData();
    this.loadUsers();
    this.loadMaterials();
  }


  loadCurrentUserRole() {
    const role = localStorage.getItem('role') || '';

    this.currentUserRole = role.startsWith('ROLE_')
      ? role
      : `ROLE_${role}`;

    console.log('ROLE IN COMPONENT:', this.currentUserRole);
  }

  loadUsers() {
    this.auth.getAllUsers().subscribe(users => {
      users.forEach((u: any) => {
        this.userMap[String(u.id)] = u.username;
      });
      console.log('USER MAP:', this.userMap);
    });
  }

  // ================= LOAD DYNAMIC MATERIALS =================
  loadMaterials() {
    this.materialService.getAll().subscribe({
      next: (materials) => {
        this.materialList = materials || [];
        // Initialize values map
        this.materialList.forEach(m => {
          this.materialValues[m.id] = 0;
        });
        console.log('MATERIALS LOADED:', this.materialList);
      },
      error: (err) => {
        console.warn('Material master API not available, using legacy mode:', err.status);
        this.materialList = [];
      }
    });
  }


  openProductionModal(p: any) {
    this.selectedProduction = p;
    this.loadCurrentUserRole();
    const modalEl = document.getElementById('productionModal');
    if (!modalEl) return;

    const modal = new bootstrap.Modal(modalEl);
    modal.show();
  }

  loadData() {
    this.service.getAll().subscribe(res => {
      this.productionList = res || [];
      this.applyFilters();
      this.updatePagination();
    });
  }

  applyFilters() {
    if (
      this.filterFromDate &&
      this.filterToDate &&
      new Date(this.filterToDate) < new Date(this.filterFromDate)
    ) {
      alert('To Date cannot be earlier than From Date');
      return;
    }

    const from = this.filterFromDate
      ? new Date(this.filterFromDate).getTime()
      : null;

    const to = this.filterToDate
      ? new Date(this.filterToDate + 'T23:59:59').getTime()
      : null;

    this.filteredProductionList = this.productionList.filter(p => {

      // ✅ DATE FILTER
      const date = new Date(p.createdDate).getTime();
      const dateOk =
        (!from || date >= from) &&
        (!to || date <= to);

      if (!dateOk) return false;

      // ✅ ROLE FILTER
      const stage = p.approvalStage || 'NONE';

      switch (this.currentUserRole) {
        case 'ROLE_DIRECTOR':
          return true;

        case 'ROLE_MANAGER':
          return stage === 'L1';

        case 'ROLE_SUPERVISOR':
          return stage === 'L2';

        case 'ROLE_COMPANY_OWNER':
        case 'ROLE_ADMIN':
        case 'ROLE_USER':
          return true;

        default:
          return false;
      }
    });

    this.currentPage = 1;
    this.updatePagination();
  }

  updatePagination() {
    this.totalPages = Math.ceil(
      this.filteredProductionList.length / this.pageSize
    );

    const start = (this.currentPage - 1) * this.pageSize;
    const end = start + this.pageSize;

    this.pagedProductionList =
      this.filteredProductionList.slice(start, end);
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



  clearFilters() {
    this.filterFromDate = '';
    this.filterToDate = '';
    this.filteredProductionList = [...this.productionList];
    this.currentPage = 1;
    this.updatePagination();
  }

  exportData(type: string) {

    // 🔥 force close import modal before export
    this.showImportModal = false;
    this.excelPreview = [];

    if (type === 'pdf') this.exportPDF();
    if (type === 'excel') this.exportExcel();
  }


  exportPDF() {
    if (!this.filterFromDate || !this.filterToDate) {
      alert('Please select date range');
      return;
    }

    this.workflowService.exportReport('PRODUCTION', this.filterFromDate, this.filterToDate, 'pdf').subscribe({
      next: (blob) => {
        const url = URL.createObjectURL(blob);
        const a = document.createElement('a');
        a.href = url;
        a.download = `Production_Report_${this.filterFromDate}_to_${this.filterToDate}.pdf`;
        a.click();
        URL.revokeObjectURL(url);
      },
      error: () => alert('Failed to export PDF')
    });
  }


  formatDate(date: any): string {
    if (!date) return '';
    return new Date(date).toLocaleDateString('en-GB'); // dd/MM/yyyy
  }


  exportExcel() {
    if (!this.filterFromDate || !this.filterToDate) {
      alert('Please select date range');
      return;
    }

    this.workflowService.exportReport('PRODUCTION', this.filterFromDate, this.filterToDate, 'excel').subscribe({
      next: (blob) => {
        const url = URL.createObjectURL(blob);
        const a = document.createElement('a');
        a.href = url;
        a.download = `Production_Report_${this.filterFromDate}_to_${this.filterToDate}.xlsx`;
        a.click();
        URL.revokeObjectURL(url);
      },
      error: () => alert('Failed to export Excel')
    });
  }



  calculateTotalSolid(): number {
    return (+this.productionForm.value.faSolid1 || 0) +
      (+this.productionForm.value.faSolid2 || 0);
  }

  userMap: { [key: string]: string } = {};


  getUserName(userId: any): string {
    if (!userId) return '—';

    const key = String(userId);

    return this.userMap[key] || key;
  }




  setShiftByTime() {
    const hour = new Date().getHours();
    if (hour >= 9 && hour < 18) this.productionForm.patchValue({ shift: 'G' });
    else if (hour < 14) this.productionForm.patchValue({ shift: '1' });
    else if (hour < 22) this.productionForm.patchValue({ shift: '2' });
    else this.productionForm.patchValue({ shift: '3' });
  }

  openForm() {
    this.showForm = true;
    this.editId = null;

    const today = new Date().toISOString().substring(0, 10);

    this.productionForm.reset({
      productionDate: today,
      productionTime: this.getCurrentTime()
    });

    // Reset dynamic material values
    this.materialList.forEach(m => {
      this.materialValues[m.id] = 0;
    });

    this.setShiftByTime();
  }

  getCurrentTime(): string {
    const now = new Date();

    let hours: number | string = now.getHours();
    let minutes: number | string = now.getMinutes();
    let seconds: number | string = now.getSeconds();

    hours = hours < 10 ? '0' + hours : hours;
    minutes = minutes < 10 ? '0' + minutes : minutes;
    seconds = seconds < 10 ? '0' + seconds : seconds;

    return `${hours}:${minutes}:${seconds}`;
  }

  submit() {
    const userId = this.auth.getLoggedInUserId();

    if (!userId) {
      alert('User not logged in');
      return;
    }
    const currentTime = this.getCurrentTime();

    this.productionForm.patchValue({
      productionTime: currentTime
    });

    // Build dynamic materials array
    const materialsPayload = this.materialList.map(m => ({
      materialMasterId: m.id,
      materialName: m.materialName,
      unit: m.unit,
      value: this.materialValues[m.id] || 0,
      displayOrder: m.displayOrder
    }));

    const payload = {
      ...this.productionForm.value,
      userId,
      totalSolid: this.calculateTotalSolid(),
      materials: materialsPayload
    };

    const req$ = this.editId
      ? this.service.update(this.editId, payload)
      : this.service.save(payload);

    req$.subscribe(() => {
      this.showForm = false;
      this.loadData();
    });
  }


  edit(row: any) {
    this.editId = row.id;
    this.showForm = true;
    this.productionForm.patchValue(row);

    // Populate dynamic material values from saved data
    this.materialList.forEach(m => {
      this.materialValues[m.id] = 0; // reset
    });

    if (row.materials && row.materials.length) {
      row.materials.forEach((pm: any) => {
        if (pm.materialMasterId) {
          this.materialValues[pm.materialMasterId] = pm.value || 0;
        }
      });
    }
  }

  delete(id: number) {
    if (confirm('Delete this production entry?')) {
      this.service.delete(id).subscribe(() => this.loadData());
    }
  }

  getApprovalLevels(p: any) {
    return {
      checkedBy: {
        name: this.getUserName(p?.approvedByL1),
        level: p?.approvedByL1 ? 'Director' : ''
      },
      reviewedBy: {
        name: this.getUserName(p?.approvedByL2),
        level: p?.approvedByL2 ? 'Manager' : ''
      },
      approvedBy: {
        name: this.getUserName(p?.approvedByL3),
        level: p?.approvedByL3 ? 'Supervisor' : ''
      }
    };
  }



  private buildExportRows(p: any): any[] {
    return this.getFieldConfig().map(f => {
      let value = p?.[f.key];

      if (f.format === 'date' && value) {
        value = this.formatDate(value);
      }

      return [
        f.label,
        value !== null && value !== undefined && value !== '' ? value : ''
      ];
    });
  }

  // Dynamic field config that includes materials
  getFieldConfig(): { label: string; key: string; format?: string }[] {
    const baseConfig = [
      { label: 'Batch No', key: 'batchNo' },
      { label: 'Date', key: 'createdDate', format: 'date' },
      { label: 'Shift', key: 'shift' },

      { label: 'Silo No 1', key: 'siloNo1' },
      { label: 'Liter Weight 1', key: 'literWeight1' },
      { label: 'FA Solid 1', key: 'faSolid1' },

      { label: 'Silo No 2', key: 'siloNo2' },
      { label: 'Liter Weight 2', key: 'literWeight2' },
      { label: 'FA Solid 2', key: 'faSolid2' },

      { label: 'Total Solid', key: 'totalSolid' },
    ];

    // Add dynamic material columns
    this.materialList.forEach(m => {
      baseConfig.push({
        label: m.materialName,
        key: `material_${m.id}`
      });
    });

    const tailConfig = [
      { label: 'Casting Time', key: 'castingTime' },
      { label: 'Production Time', key: 'productionTime' },

      { label: 'Production Remark', key: 'productionRemark' },
      { label: 'Remark', key: 'remark' },

      { label: 'Approval Stage', key: 'approvalStage' },
      { label: 'Approved By L1', key: 'approvedByL1' },
      { label: 'Approved By L2', key: 'approvedByL2' },
      { label: 'Approved By L3', key: 'approvedByL3' }
    ];

    return [...baseConfig, ...tailConfig];
  }

  // Helper to get material value from a production entry for display
  getMaterialValue(production: any, materialId: number): any {
    if (!production || !production.materials) return '—';
    const mat = production.materials.find((m: any) => m.materialMasterId === materialId);
    return mat ? mat.value : '—';
  }


  downloadProduction(format: string = 'pdf') {
    if (!this.selectedProduction || !this.selectedProduction.batchNo) {
      alert('No batch selected to download');
      return;
    }

    this.workflowService.downloadReport(this.selectedProduction.batchNo, 'PRODUCTION', format).subscribe({
      next: (blob) => {
        const url = URL.createObjectURL(blob);
        const a = document.createElement('a');
        a.href = url;
        const ext = format === 'excel' ? 'xlsx' : 'pdf';
        a.download = `workflow_report_${this.selectedProduction.batchNo}_PRODUCTION.${ext}`;
        a.click();
        URL.revokeObjectURL(url);
      },
      error: () => {
        alert('Failed to download report.');
      }
    });
  }

  /** Download combined horizontal Excel for one batch */
  downloadHorizontalReport(batchNo: string) {
    if (!batchNo) { alert('No batch number available'); return; }
    this.horizontalReportService.downloadLifecycleExcel(batchNo, 'PRODUCTION').subscribe({
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
    if (!this.filterFromDate || !this.filterToDate) {
      alert('Please select a date range first');
      return;
    }
    this.horizontalReportService.downloadExcel(this.filterFromDate, this.filterToDate, undefined, 'PRODUCTION').subscribe({
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


  canApprove(p: any): boolean {
    if (!p) return false;

    const stage = p.approvalStage || 'NONE';

    return (
      (this.currentUserRole === 'ROLE_DIRECTOR' && stage === 'NONE') ||
      (this.currentUserRole === 'ROLE_MANAGER' && stage === 'L1') ||
      (this.currentUserRole === 'ROLE_SUPERVISOR' && stage === 'L2')
    );
  }


  canReject(p: any): boolean {
    if (!p) return false;

    const stage = p.approvalStage;

    if (stage === 'L3') return false;

    return (
      (this.currentUserRole === 'ROLE_DIRECTOR' && stage === 'NONE') ||
      (this.currentUserRole === 'ROLE_MANAGER' && stage === 'L1') ||
      (this.currentUserRole === 'ROLE_SUPERVISOR' && stage === 'L2')
    );
  }


  canEditDelete(): boolean {
    return (
      this.currentUserRole === 'ROLE_COMPANY_OWNER' ||
      this.currentUserRole === 'ROLE_ADMIN' ||
      this.currentUserRole === 'ROLE_DIRECTOR'
    );
  }

  approveProduction() {
    if (!this.selectedProduction) return;

    const userId = this.auth.getLoggedInUserId();
    if (!userId) return;

    this.service.approve(
      this.selectedProduction.id,
      userId,
      this.currentUserRole
    ).subscribe(() => {
      alert('Approved successfully');
      this.closeModal();
      this.loadData();
    });
  }


  rejectProduction() {
    if (!this.selectedProduction) return;

    const reason = prompt('Enter rejection reason');
    if (!reason) return;

    const userId = this.auth.getLoggedInUserId();
    if (!userId) return;

    this.service.reject(
      this.selectedProduction.id,
      reason,
      userId,
      this.currentUserRole
    ).subscribe(() => {
      alert('Rejected successfully');
      this.closeModal();
      this.loadData();
    });
  }

  closeModal() {
    const modalEl = document.getElementById('productionModal');
    if (!modalEl) return;

    const modalInstance = bootstrap.Modal.getInstance(modalEl);
    modalInstance?.hide();
  }



  canViewProduction(p: any): boolean {
    if (!p) return false;

    const stage = p.approvalStage || 'NONE';

    switch (this.currentUserRole) {

      case 'ROLE_DIRECTOR':
        return stage === 'NONE';

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

  importExcel(event: any) {
    const file = event.target.files[0];
    if (!file) return;

    const reader = new FileReader();

    reader.onload = (e: any) => {
      const workbook = XLSX.read(e.target.result, { type: 'binary' });
      const sheetName = workbook.SheetNames[0];
      const sheet = workbook.Sheets[sheetName];

      const rows = XLSX.utils.sheet_to_json<any>(sheet, {
        defval: ''
      });

      if (!rows.length) {
        alert('Excel file is empty');
        return;
      }

      this.excelHeaders = Object.keys(rows[0]);

      this.excelRows = rows.map(r => ({
        ...r,
        importStatus: 'PENDING',
        errorMessage: ''
      }));

      this.showImportModal = true;
    };

    reader.readAsBinaryString(file);
  }


  normalizeRow(row: any) {
    const obj: any = {};
    Object.keys(row).forEach(k => {
      const key = k.toLowerCase().replace(/\s+/g, '').replace(/_/g, '');
      obj[key] = row[k];
    });
    return obj;
  }

  formatExcelDate(value: any): string | null {
    if (!value) return null;

    if (typeof value === 'number') {
      const d = XLSX.SSF.parse_date_code(value);
      return `${d.y}-${String(d.m).padStart(2, '0')}-${String(d.d).padStart(2, '0')}`;
    }

    if (typeof value === 'string' && value.includes('/')) {
      const [d, m, y] = value.split('/');
      return `${y}-${m.padStart(2, '0')}-${d.padStart(2, '0')}`;
    }

    return value;
  }

  saveExcelToDB() {
    const payload = {
      productions: this.excelRows.map(r => ({
        shift: r['Shift'],
        siloNo1: r['Silo No 1'],
        literWeight1: Number(r['Liter Weight 1'] || 0),
        faSolid1: Number(r['FA Solid 1'] || 0),

        siloNo2: r['Silo No 2'],
        literWeight2: Number(r['Liter Weight 2'] || 0),
        faSolid2: Number(r['FA Solid 2'] || 0),

        waterLiter: Number(r['Water Liter'] || 0),
        cementKg: Number(r['Cement Kg'] || 0),
        limeKg: Number(r['Lime Kg'] || 0),
        gypsumKg: Number(r['Gypsum Kg'] || 0),
        solOilKg: Number(r['Sol Oil Kg'] || 0),
        aiPowerGm: Number(r['AI Power gm'] || 0),
        tempC: Number(r['Temperature (°C)'] || 0),

        castingTime: r['Casting Time'],
        productionTime: r['Production Time'],
        productionRemark: r['Production Remark'],
        remark: r['Remark']
      })),
      uploadedBy: 1,
      branchId: 1,
      orgId: 1
    };

    this.service.importProduction(payload).subscribe(res => {

      this.excelRows.forEach((row, index) => {
        row.importStatus = res.results[index]?.status;
        row.errorMessage = res.results[index]?.error || '';
      });

      this.apiMessage =
        `${res.savedCount} saved, ${res.errorCount} failed`;

      this.showImportModal = false;
      this.loadData();
      this.currentPage = 1;
    });
  }




  clearExcelPreview() {
    this.excelPreview = [];
    this.hasExcelErrors = false;
    this.apiMessage = '';
    this.showImportModal = false;
  }

  onImportSelect(event: any, fileInput: HTMLInputElement) {
    if (event.target.value === 'excel') fileInput.click();
    event.target.value = '';
  }

  goToDashboard() {
    this.router.navigate(['/production-dashboard']);
  }

}
