import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { BlockSeparatingService } from '../services/BlockSeparatingService';
// import { BlockSeparatingService } from '../services/block-separating.service';
import * as XLSX from 'xlsx';
import jsPDF from 'jspdf';
import autoTable from 'jspdf-autotable';
import { AuthService } from '../services/auth.service';
import { Router } from '@angular/router';
import { WorkflowService } from '../services/workflow.service';
import { FilterService } from '../services/filter.service';
import { HorizontalReportService } from '../services/horizontal-report.service';

@Component({
  selector: 'app-block-separating',
  templateUrl: './block-separating.component.html',
  styleUrls: ['./block-separating.component.css']
})
export class BlockSeparatingComponent implements OnInit {

  form!: FormGroup;
  isSubmitting = false;
  isEdit = false;
  filterFromDate = '';
  filterToDate = '';
  autoclaveBatches: any[] = [];
  // ================= PAGINATION =================
  pageSize = 5;        // records per page
  currentPage = 1;
  totalPages = 0;
  pagedList: any[] = [];
  editId: number | null = null;

  filterShift = '';
  filterBlockSize = '';

  blockSizes: string[] = [];
  usedBatchNumbers: string[] = [];

  showForm = false;

  list: any[] = [];          // full data
  filteredList: any[] = [];  // table data

  constructor(
    private fb: FormBuilder,
    private service: BlockSeparatingService,
    private auth: AuthService,
    private router: Router,
    private workflowService: WorkflowService,
    private filterService: FilterService,
    private horizontalReportService: HorizontalReportService
  ) { }

  ngOnInit(): void {
    const today = new Date().toISOString().split('T')[0];

    this.form = this.fb.group({
      reportDate: [today, Validators.required],
      batchNumber: ['', Validators.required],
      castingDate: ['', Validators.required],
      blockSize: ['', Validators.required],
      shift: ['', Validators.required],
      time: ['', Validators.required],
      remark: ['']
    });

    this.loadList();
    // this.loadCuttingBatches();   // ✅ correct method

    this.filterService.fromDate$.subscribe(d => {
      this.filterFromDate = d;
      this.applyFilters();
    });
    this.filterService.toDate$.subscribe(d => {
      this.filterToDate = d;
      this.applyFilters();
    });
  }

  edit(row: any) {
    this.showForm = true;
    this.isEdit = true;
    this.editId = row.id;

    // 🔥 Allow current batch in dropdown
    this.autoclaveBatches = [
      { batchNo: row.batchNumber },
      ...this.autoclaveBatches
    ];

    this.form.patchValue({
      reportDate: row.reportDate,
      batchNumber: row.batchNumber,
      castingDate: row.castingDate,
      blockSize: row.blockSize,
      shift: row.shift,
      time: row.time,
      remark: row.remark
    });
  }

  goToDashboard() {
    this.router.navigate(['/production-dashboard']);
  }

  getCurrentTime(): string {
    const now = new Date();
    const hh = String(now.getHours()).padStart(2, '0');
    const mm = String(now.getMinutes()).padStart(2, '0');
    const ss = String(now.getSeconds()).padStart(2, '0');
    return `${hh}:${mm}:${ss}`;
  }
  loadList() {
    this.service.getAll().subscribe(res => {

      const map = new Map<string, any>();
      res.forEach((r: any) => map.set(r.batchNumber, r));

      this.list = Array.from(map.values());
      this.filteredList = [...this.list];

      this.usedBatchNumbers = this.list.map(r => r.batchNumber);

      this.blockSizes = [
        ...new Set(this.list.map(r => r.blockSize).filter(Boolean))
      ];

      this.currentPage = 1;
      this.updatePagination();      // 🔥 ADD THIS

      this.loadCuttingBatches();    // dropdown refresh
    });
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



  loadCuttingBatches() {
    this.service.getCuttingBatches().subscribe({
      next: (res: any[]) => {

        // 🔥 REMOVE already used batch numbers
        this.autoclaveBatches = res
          .filter(r => !this.usedBatchNumbers.includes(r.batchNo))
          .map(r => ({ batchNo: r.batchNo }));

      },
      error: (err: any) => {
        console.error('Batch load error:', err);
        alert('Failed to load batches');
      }
    });
  }





  openForm() {
    this.showForm = true;
    this.isEdit = false;

    const today = new Date().toISOString().split('T')[0];

    this.form.reset({
      reportDate: today,
      time: this.getCurrentTime()   // 🔥 AUTO TIME
    });
  }
  resetFormWithDefaults() {
    const today = new Date().toISOString().split('T')[0];

    this.form.reset({
      reportDate: today,
      shift: ''     // or 'G' if you want default General
    });
  }

  cancel() {
    this.showForm = false;
    this.editId = null;
    this.isEdit = false;

    const today = new Date().toISOString().split('T')[0];
    this.form.reset({ reportDate: today });
  }


  // ================= SUBMIT =================
  submit() {

    if (!this.form.value.time) {
      this.form.patchValue({
        time: this.getCurrentTime()
      });
    }

    if (this.form.invalid) {
      this.form.markAllAsTouched();
      return;
    }

    this.isSubmitting = true;
    const userId = this.auth.getLoggedInUserId();


    const now = this.getCurrentTime();

    // ✅ NEW record → always take current time
    if (!this.editId) {
      this.form.patchValue({
        time: now
      });
    }

    // ✅ Edit mode → if time empty then take current time
    if (this.editId && !this.form.value.time) {
      this.form.patchValue({
        time: now
      });
    }

    const payload = {
      ...this.form.value,
      userId: userId
    };

    const request$ = this.editId
      ? this.service.update(this.editId, payload)
      : this.service.create(payload);

    request$.subscribe({
      next: () => {
        this.isSubmitting = false;
        this.showForm = false;
        this.editId = null;
        this.isEdit = false;

        this.loadList();
      },
      error: () => {
        alert('Error while saving');
        this.isSubmitting = false;
      }
    });
  }

  get noBatchAvailable(): boolean {
    return this.autoclaveBatches.length === 0;
  }

  delete(id: number) {
    if (!confirm('Are you sure you want to delete this record?')) return;

    this.service.delete(id).subscribe({
      next: () => {
        this.loadList();
      },
      error: () => {
        alert('Delete failed');
      }
    });
  }

  applyFilters() {
    this.filteredList = this.list.filter(r => {

      let ok = true;

      if (this.filterFromDate) {
        ok = ok && new Date(r.castingDate) >= new Date(this.filterFromDate);
      }

      if (this.filterToDate) {
        ok = ok && new Date(r.castingDate) <= new Date(this.filterToDate);
      }

      if (this.filterShift) {
        ok = ok && r.shift === this.filterShift;
      }

      if (this.filterBlockSize) {
        ok = ok && r.blockSize === this.filterBlockSize;
      }

      return ok;
    });

    this.currentPage = 1;        // 🔥 RESET PAGE
    this.updatePagination();     // 🔥 IMPORTANT
  }




  onDateChange() {
    this.filterService.setFromDate(this.filterFromDate);
    this.filterService.setToDate(this.filterToDate);
  }

  clearFilters() {
    this.filterFromDate = '';
    this.filterToDate = '';
    this.filterShift = '';
    this.filterBlockSize = '';
    this.onDateChange();
  }


  exportExcel() {
    if (!this.filterFromDate || !this.filterToDate) {
      alert('Please select date range');
      return;
    }
    this.workflowService.exportReport('BLOCK_SEPARATING', this.filterFromDate, this.filterToDate, 'excel').subscribe({
      next: (blob) => {
        const url = URL.createObjectURL(blob);
        const a = document.createElement('a');
        a.href = url;
        a.download = `BlockSeparating_Report_${this.filterFromDate}_to_${this.filterToDate}.xlsx`;
        a.click();
        URL.revokeObjectURL(url);
      },
      error: () => alert('Failed to export Excel')
    });
  }


  // onImportSelect(event: any, fileInput: HTMLInputElement) {
  //   if (event.target.value === 'excel') {
  //     fileInput.click();
  //   }
  //   event.target.value = '';
  // }

  importExcel(event: any) {
    alert('Excel import will be added later');
  }

  onExportChange(event: any) {
    const value = event.target.value;

    if (value === 'excel') {
      this.exportExcel();
    }

    if (value === 'pdf') {
      this.exportPdf();
    }

    event.target.value = '';
  }


  exportPdf() {
    if (!this.filterFromDate || !this.filterToDate) {
      alert('Please select date range');
      return;
    }
    this.workflowService.exportReport('BLOCK_SEPARATING', this.filterFromDate, this.filterToDate, 'pdf').subscribe({
      next: (blob) => {
        const url = URL.createObjectURL(blob);
        const a = document.createElement('a');
        a.href = url;
        a.download = `BlockSeparating_Report_${this.filterFromDate}_to_${this.filterToDate}.pdf`;
        a.click();
        URL.revokeObjectURL(url);
      },
      error: () => alert('Failed to export PDF')
    });
  }


  // ================= IMPORT SELECT =================
  onImportSelect(event: any) {
    const value = event.target.value;
    if (value === 'excel') {
      const fileInput = document.getElementById('blockExcelInput') as HTMLInputElement;
      fileInput?.click();
    }
    event.target.value = '';
  }

  // ================= FILE SELECT =================
  onExcelSelect(event: any) {
    this.importExcel(event);
  }

  downloadBlock(r: any, format: string = 'pdf') {
    if (!r || !r.batchNumber) { alert('No batch selected to download'); return; }
    this.workflowService.downloadReport(r.batchNumber, 'BLOCK_SEPARATING', format).subscribe({
      next: (blob) => {
        const url = URL.createObjectURL(blob);
        const a = document.createElement('a');
        a.href = url;
        const ext = format === 'excel' ? 'xlsx' : 'pdf';
        a.download = `workflow_report_${r.batchNumber}_BLOCK_SEPARATING.${ext}`;
        a.click();
        URL.revokeObjectURL(url);
      },
      error: () => { alert('Failed to download report.'); }
    });
  }

  /** Download combined horizontal Excel for this block separating batch */
  downloadHorizontalReport(r: any) {
    const batchNo = r?.batchNumber;
    if (!batchNo) { alert('No batch number available'); return; }
    this.horizontalReportService.downloadLifecycleExcel(batchNo, 'BLOCK_SEPARATING').subscribe({
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
    this.horizontalReportService.downloadExcel(this.filterFromDate, this.filterToDate, undefined, 'BLOCK_SEPARATING').subscribe({
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
