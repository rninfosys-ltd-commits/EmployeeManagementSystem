import { Component, OnInit } from '@angular/core';
import { FormGroup, FormBuilder } from '@angular/forms';
import { RejectionService } from '../services/RejectionService';
import { CubeTestService } from '../services/CubeTestService';
import * as XLSX from 'xlsx';
import jsPDF from 'jspdf';
import autoTable from 'jspdf-autotable';
import { AuthService } from '../services/auth.service';
import { Router } from '@angular/router';
import { WorkflowService } from '../services/workflow.service';
import { FilterService } from '../services/filter.service';
import { HorizontalReportService } from '../services/horizontal-report.service';



@Component({
  selector: 'app-rejection-component',
  templateUrl: './rejection-component.component.html',
  styleUrls: ['./rejection-component.component.css']
})
export class RejectionComponentComponent implements OnInit {

  form!: FormGroup;
  cubeTests: any[] = [];

  list: any[] = [];
  filtered: any[] = [];
  paginated: any[] = [];

  showLeads = true;
  editId: any = null;

  filterFromDate = '';
  filterToDate = '';
  filterShift = '';

  currentPage = 1;
  pageSize = 5;
  totalPages = 0;


  constructor(private fb: FormBuilder, private service: RejectionService,
    private auth: AuthService,
    private cubeTestService: CubeTestService,
    private router: Router,
    private workflowService: WorkflowService,
    private filterService: FilterService,
    private horizontalReportService: HorizontalReportService
  ) { }

  ngOnInit() {

    const today = new Date().toISOString().split('T')[0]; // ✅ yyyy-MM-dd

    this.form = this.fb.group({
      date: [today],          // 👈 default today
      batchNo: [''],
      shift: [''],
      blockSize: [''],
      qty: [''],
      cornerDamage: [0],
      eruptionType: [0],
      topSideDamages: [0],
      sideCrackThermalCrack: [0],
      risingCrack: [0],
      centreCrack: [0],
      bottomUncutBlocks: [0],
      totalBreakages: [0],
      isActive: [1]
    });

    this.load();
    this.loadCubeTests();

    this.filterService.fromDate$.subscribe(d => {
      this.filterFromDate = d;
      this.applyFilters();
    });
    this.filterService.toDate$.subscribe(d => {
      this.filterToDate = d;
      this.applyFilters();
    });
  }


  load() {
    this.service.getAll().subscribe((r: any[]) => {
      this.list = r;

      // 🔥 important
      this.filtered = [...r];

      this.setupPagination();
    });
  }

  get noBatchAvailable(): boolean {
    return !this.cubeTests || this.cubeTests.length === 0;
  }
  loadCubeTests() {
    this.cubeTestService.getAll().subscribe((res: any[]) => {
      console.log('Cube tests:', res);   // 🔥 debug
      this.cubeTests = res;
    });
  }

  goToDashboard() {
    this.router.navigate(['/production-dashboard']);
  }


  onBatchSelect(event: any) {

    const batch = event.target.value;

    const selected = this.cubeTests.find((x: any) => x.batchNo == batch);

    if (selected) {
      this.form.patchValue({
        date: selected.castDate   // optional
      });
    }
  }

  setupPagination() {
    this.totalPages = Math.ceil(this.filtered.length / this.pageSize);
    this.currentPage = 1;
    this.updatePage();
  }

  updatePage() {
    const s = (this.currentPage - 1) * this.pageSize;
    this.paginated = this.filtered.slice(s, s + this.pageSize);
  }

  nextPage() { if (this.currentPage < this.totalPages) { this.currentPage++; this.updatePage(); } }
  prevPage() { if (this.currentPage > 1) { this.currentPage--; this.updatePage(); } }

  applyFilters() {
    // ✅ Validate date range first
    if (this.filterFromDate && this.filterToDate) {

      const from = new Date(this.filterFromDate);
      const to = new Date(this.filterToDate);

      if (to < from) {
        alert('To Date must be equal or greater than From Date');
        this.filterToDate = '';
        return;
      }
    }

    this.filtered = this.list.filter(r => {

      const rowDate = new Date(r.date).setHours(0, 0, 0, 0);

      if (this.filterFromDate) {
        const from = new Date(this.filterFromDate).setHours(0, 0, 0, 0);
        if (rowDate < from) return false;
      }

      if (this.filterToDate) {
        const to = new Date(this.filterToDate).setHours(0, 0, 0, 0);
        if (rowDate > to) return false;
      }

      // ✅ Shift filter
      if (this.filterShift && r.shift != this.filterShift) return false;

      return true;
    });

    this.setupPagination();
  }



  delete(id: any) {

    if (!confirm('Are you sure you want to delete this record?')) return;

    this.service.delete(id).subscribe(() => {
      alert('Deleted successfully');
      this.load();   // refresh list
    });
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

  openCreate() {

    const today = new Date().toISOString().split('T')[0];

    this.form.reset({
      date: today,     // ✅ keep today after reset
      isActive: 1
    });

    this.editId = null;
    this.showLeads = false;
  }

  edit(r: any) {

    const date = r.date ? r.date.split('T')[0] : '';

    this.form.patchValue({
      date: date,
      batchNo: r.batchNo,
      blockSize: r.blockSize,
      qty: r.qty,
      shift: r.shift,
      cornerDamage: +r.cornerDamage || 0,
      eruptionType: +r.eruptionType || 0,
      topSideDamages: +r.topSideDamages || 0,
      sideCrackThermalCrack: +r.sideCrackThermalCrack || 0,
      risingCrack: +r.risingCrack || 0,
      centreCrack: +r.centreCrack || 0,
      bottomUncutBlocks: +r.bottomUncutBlocks || 0
    });

    this.editId = r.id;
    this.showLeads = false;
  }



  submit() {
    const userId = this.auth.getLoggedInUserId();
    const f = this.form.value;

    f.totalBreakages =
      +f.cornerDamage +
      +f.eruptionType +
      +f.topSideDamages +
      +f.sideCrackThermalCrack +
      +f.risingCrack +
      +f.centreCrack +
      +f.bottomUncutBlocks;
    +f.remainingQty;

    const payload = {
      ...f,
      userId: userId
    };

    const req = this.editId
      ? this.service.update(this.editId, f)
      : this.service.create(f);

    req.subscribe(() => {
      alert('Saved');
      this.showLeads = true;
      this.load();
    });
  }

  backToList() { this.showLeads = true; }

  // ===== dummy export/import (no errors) =====
  onExportChange(e: any) {
    if (e.target.value === 'excel') this.exportExcel();
    if (e.target.value === 'pdf') this.exportPdf();
    if (e.target.value === 'horizontal') this.exportHorizontalReport();
    e.target.value = '';
  }

  exportExcel() {
    if (!this.filterFromDate || !this.filterToDate) {
      alert('Please select date range');
      return;
    }
    this.workflowService.exportReport('REJECTION', this.filterFromDate, this.filterToDate, 'excel').subscribe({
      next: (blob) => {
        const url = URL.createObjectURL(blob);
        const a = document.createElement('a');
        a.href = url;
        a.download = `Rejection_Report_${this.filterFromDate}_to_${this.filterToDate}.xlsx`;
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
    this.workflowService.exportReport('REJECTION', this.filterFromDate, this.filterToDate, 'pdf').subscribe({
      next: (blob) => {
        const url = URL.createObjectURL(blob);
        const a = document.createElement('a');
        a.href = url;
        a.download = `Rejection_Report_${this.filterFromDate}_to_${this.filterToDate}.pdf`;
        a.click();
        URL.revokeObjectURL(url);
      },
      error: () => alert('Failed to export PDF')
    });
  }


  onExcelSelect(event: any) {
    const file = event.target.files[0];
    if (!file) return;

    alert('Import coming later'); // placeholder
  }
  get totalBreakages(): number {
    const f = this.form.value;

    return (
      (+f.cornerDamage || 0) +
      (+f.eruptionType || 0) +
      (+f.topSideDamages || 0) +
      (+f.sideCrackThermalCrack || 0) +
      (+f.risingCrack || 0) +
      (+f.centreCrack || 0) +
      (+f.bottomUncutBlocks || 0)
    );
  }

  get remainingQty(): number {
    const qty = +this.form.value.qty || 0;
    return qty - this.totalBreakages;
  }

  get isRemainingInvalid(): boolean {
    return this.remainingQty < 0;
  }

  downloadRejection(r: any, format: string = 'pdf') {
    if (!r || !r.batchNo) { alert('No batch selected to download'); return; }
    this.workflowService.downloadReport(r.batchNo, 'REJECTION', format).subscribe({
      next: (blob) => {
        const url = URL.createObjectURL(blob);
        const a = document.createElement('a');
        a.href = url;
        const ext = format === 'excel' ? 'xlsx' : 'pdf';
        a.download = `workflow_report_${r.batchNo}_REJECTION.${ext}`;
        a.click();
        URL.revokeObjectURL(url);
      },
      error: () => { alert('Failed to download report.'); }
    });
  }

  /** Download combined horizontal Excel for this rejection batch */
  downloadHorizontalReport(r: any) {
    const batchNo = r?.batchNo;
    if (!batchNo) { alert('No batch number available'); return; }
    this.horizontalReportService.downloadLifecycleExcel(batchNo, 'REJECTION').subscribe({
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
    this.horizontalReportService.downloadExcel(this.filterFromDate, this.filterToDate, undefined, 'REJECTION').subscribe({
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
