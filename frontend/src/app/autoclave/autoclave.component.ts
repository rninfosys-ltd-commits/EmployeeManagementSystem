import { Component, OnInit } from '@angular/core';
import {
  FormBuilder,
  FormGroup,
  Validators,
  FormArray
} from '@angular/forms';
import { AutoclaveService } from '../services/AutoclaveService';
import * as XLSX from 'xlsx';
import jsPDF from 'jspdf';
import autoTable from 'jspdf-autotable';
import { WireCuttingReportService } from '../services/WireCuttingReportService';
import { AuthService } from '../services/auth.service';
import { Router } from '@angular/router';
import { WorkflowService } from '../services/workflow.service';
import { FilterService } from '../services/filter.service';
import { HorizontalReportService } from '../services/horizontal-report.service';


@Component({
  selector: 'app-autoclave',
  templateUrl: './autoclave.component.html',
  styleUrls: ['./autoclave.component.css']
})
export class AutoclaveComponent implements OnInit {

  // ================= UI STATE =================
  showForm = false;

  // ================= MAIN FORM =================
  form!: FormGroup;

  // ================= WAGON FORM =================
  wagonForm!: FormGroup;

  // ================= DATA =================
  list: any[] = [];
  filteredList: any[] = [];
  pagedList: any[] = [];

  availableBatches: string[] = [];


  // ================= FILTER =================
  filterFromDate = '';
  filterToDate = '';

  // ================= PAGINATION =================
  pageSize = 5;
  currentPage = 1;
  totalPages = 0;
  isEditMode = false;

  // ================= EDIT =================
  editId: number | null = null;

  constructor(
    private fb: FormBuilder,
    private service: AutoclaveService,
    private wireCuttingService: WireCuttingReportService,
    private auth: AuthService,
    private router: Router,
    private workflowService: WorkflowService,
    private filterService: FilterService,
    private horizontalReportService: HorizontalReportService
  ) { }

  // ================= INIT =================
  ngOnInit(): void {
    const today = new Date().toISOString().substring(0, 10);


    // Main Autoclave Form
    this.form = this.fb.group({
      // autoclaveNo: ['', Validators.required],
      runNo: [''],
      startedAt: [''],
      startedDate: [today],
      completedAt: [''],
      completedDate: [''],
      remarks: ['']
    });

    this.wagonForm = this.fb.group({
      eBatch: ['', Validators.required],
      eSize: [''],
      mBatch: [''],
      mSize: [''],
      wBatch: [''],
      wSize: [''],
      wagons: this.fb.array([])
    });

    this.wagonForm.valueChanges.subscribe(() => {

      (['eBatch', 'mBatch', 'wBatch'] as const).forEach(side => {

        const control = this.wagonForm.get(side);
        if (!control) return;

        const value = control.value;

        if (value && !this.getAvailableBatchesFor(side[0] as 'e' | 'm' | 'w').includes(value)) {
          control.setValue(null, { emitEvent: false });
        }

      });

    });

    this.form.get('completedDate')?.valueChanges.subscribe(val => {
      if (val) {
        this.form.patchValue({
          completedAt: this.getCurrentTime()
        });
      }
    });
    this.loadCuttingBatches();
    this.loadList();

    this.filterService.fromDate$.subscribe(d => {
      this.filterFromDate = d;
      this.applyFilters();
    });
    this.filterService.toDate$.subscribe(d => {
      this.filterToDate = d;
      this.applyFilters();
    });
  }
  getCurrentTime(): string {
    const now = new Date();
    const hh = String(now.getHours()).padStart(2, '0');
    const mm = String(now.getMinutes()).padStart(2, '0');
    const ss = String(now.getSeconds()).padStart(2, '0');
    return `${hh}:${mm}:${ss}`;
  }
  // ================= WAGONS =================
  get wagons(): FormArray {
    return this.wagonForm.get('wagons') as FormArray;
  }

  loadCuttingBatches(): void {
    this.wireCuttingService.getAll().subscribe(res => {

      console.log('CUTTING API RESPONSE:', res);

      // ✅ NO FILTER – take ALL batch numbers
      this.availableBatches = [
        ...new Set(res.map(r => r.batchNo))
      ];

      console.log('AVAILABLE BATCHES (ALL):', this.availableBatches);
    });
  }


  addWagon(): void {
    if (this.wagons.length >= 14) return;

    const wagon = this.fb.group({
      eBatch: this.wagonForm.value.eBatch,
      eSize: this.wagonForm.value.eSize,
      mBatch: this.wagonForm.value.mBatch,
      mSize: this.wagonForm.value.mSize,
      wBatch: this.wagonForm.value.wBatch,
      wSize: this.wagonForm.value.wSize
    });

    this.wagons.push(wagon);

    // clear input fields after add
    this.wagonForm.patchValue({
      eBatch: null,
      eSize: '',
      mBatch: null,
      mSize: '',
      wBatch: null,
      wSize: ''
    });
  }
  goToDashboard() {
    this.router.navigate(['/production-dashboard']);
  }


  removeWagon(index: number): void {
    this.wagons.removeAt(index);
  }

  isBatchUsed(batch: string): boolean {
    return this.wagons.controls.some(ctrl =>
      Object.values(ctrl.value).includes(batch)
    );
  }

  // ================= LOAD =================
  loadList(): void {
    this.service.getAll().subscribe(res => {

      console.log('API RESPONSE:', res);
      console.log('TOTAL RECORDS FROM API:', res.length);

      this.list = res || [];
      this.applyFilters();
    });
  }


  applyFilters(): void {
    const from = this.filterFromDate
      ? new Date(this.filterFromDate).getTime()
      : null;

    const to = this.filterToDate
      ? new Date(this.filterToDate + 'T23:59:59').getTime()
      : null;

    this.filteredList = this.list.filter(r => {
      const d = new Date(r.startedDate).getTime();
      return (!from || d >= from) && (!to || d <= to);
    });

    console.log('FILTERED LIST COUNT:', this.filteredList.length);

    this.currentPage = 1;
    this.updatePagination();
  }


  onDateChange(): void {
    this.filterService.setFromDate(this.filterFromDate);
    this.filterService.setToDate(this.filterToDate);
  }

  clearFilters(): void {
    this.filterFromDate = '';
    this.filterToDate = '';
    this.onDateChange();
  }

  // ================= PAGINATION =================
  updatePagination(): void {
    this.totalPages = Math.ceil(this.filteredList.length / this.pageSize);
    const start = (this.currentPage - 1) * this.pageSize;
    this.pagedList = this.filteredList.slice(start, start + this.pageSize);
  }

  goToPage(p: number): void {
    this.currentPage = p;
    this.updatePagination();
  }

  nextPage(): void {
    if (this.currentPage < this.totalPages) {
      this.currentPage++;
      this.updatePagination();
    }
  }

  prevPage(): void {
    if (this.currentPage > 1) {
      this.currentPage--;
      this.updatePagination();
    }
  }

  openForm(): void {
    this.isEditMode = false;
    this.showForm = true;
    this.editId = null;
    const now = this.getCurrentTime();
    this.form.reset({
      startedDate: new Date().toISOString().substring(0, 10),
      startedAt: this.getCurrentTime()
    });

    this.form.patchValue({
      startedDate: new Date().toISOString().substring(0, 10),
      startedAt: now,
      completedDate: '',
      completedAt: '',
      runNo: '',
      remarks: ''
    });

    this.wagonForm.reset();
    this.wagons.clear();
  }

  edit(row: any): void {
    this.isEditMode = true;
    this.showForm = true;
    this.editId = row.id;

    this.form.patchValue(row);

    this.wagons.clear();
    if (row.wagons?.length) {
      row.wagons.forEach((w: any) => {
        this.wagons.push(this.fb.group(w));
      });
    }
  }

  save(): void {

    // ✅ If user entered completed date but time still empty
    if (this.form.value.completedDate && !this.form.value.completedAt) {
      this.form.patchValue({
        completedAt: this.getCurrentTime()
      });
    }

    const userId = this.auth.getLoggedInUserId();
    const now = this.getCurrentTime();

    // ✅ NEW record → ensure start time exists
    if (!this.editId) {
      this.form.patchValue({
        startedAt: now
      });
    }

    // ✅ completed date selected but time empty
    if (this.form.value.completedDate && !this.form.value.completedAt) {
      this.form.patchValue({
        completedAt: now
      });
    }

    const payload = {
      ...this.form.value,
      wagons: this.wagonForm.value.wagons,
      userId,
      branchId: 1,
      orgId: 1
    };

    console.log('AUTOCLAVE PAYLOAD', payload);

    const req$ = this.editId
      ? this.service.update(this.editId, payload)
      : this.service.save(payload);

    req$.subscribe(() => {
      this.showForm = false;
      this.editId = null;
      this.wagonForm.reset();
      this.wagons.clear();
      this.loadList();
    });
  }

  delete(id: number): void {
    if (confirm('Delete this autoclave cycle?')) {
      this.service.delete(id).subscribe(() => this.loadList());
    }
  }

  // ================= NAV =================
  back(): void {
    this.cancel();
  }

  cancel(): void {
    this.showForm = false;
    this.editId = null;
  }

  // ================= EXPORT =================
  onExportChange(event: Event): void {
    const value = (event.target as HTMLSelectElement).value;
    if (value === 'excel') this.exportExcel();
    if (value === 'pdf') this.exportPdf();
    if (value === 'horizontal') this.exportHorizontalReport();
    (event.target as HTMLSelectElement).value = '';
  }

  exportExcel(): void {
    if (!this.filterFromDate || !this.filterToDate) {
      alert('Please select date range');
      return;
    }
    this.workflowService.exportReport('AUTOCLAVE', this.filterFromDate, this.filterToDate, 'excel').subscribe({
      next: (blob) => {
        const url = URL.createObjectURL(blob);
        const a = document.createElement('a');
        a.href = url;
        a.download = `Autoclave_Report_${this.filterFromDate}_to_${this.filterToDate}.xlsx`;
        a.click();
        URL.revokeObjectURL(url);
      },
      error: () => alert('Failed to export Excel')
    });
  }




  exportPdf(): void {
    if (!this.filterFromDate || !this.filterToDate) {
      alert('Please select date range');
      return;
    }
    this.workflowService.exportReport('AUTOCLAVE', this.filterFromDate, this.filterToDate, 'pdf').subscribe({
      next: (blob) => {
        const url = URL.createObjectURL(blob);
        const a = document.createElement('a');
        a.href = url;
        a.download = `Autoclave_Report_${this.filterFromDate}_to_${this.filterToDate}.pdf`;
        a.click();
        URL.revokeObjectURL(url);
      },
      error: () => alert('Failed to export PDF')
    });
  }

  downloadAutoclave(r: any, format: string = 'pdf') {
    if (!r) return;
    let batchNo = "";
    if (r.wagons && r.wagons.length > 0) {
      batchNo = r.wagons[0].eBatch || r.wagons[0].mBatch || r.wagons[0].wBatch;
    }
    if (!batchNo) { alert("No batch number found in this autoclave cycle."); return; }
    this.workflowService.downloadReport(batchNo, 'AUTOCLAVE', format).subscribe({
      next: (blob) => {
        const url = URL.createObjectURL(blob);
        const a = document.createElement('a');
        a.href = url;
        const ext = format === 'excel' ? 'xlsx' : 'pdf';
        a.download = `workflow_report_${batchNo}_AUTOCLAVE.${ext}`;
        a.click();
        URL.revokeObjectURL(url);
      },
      error: () => { alert('Failed to download report.'); }
    });
  }

  /** Download combined horizontal Excel for one batch from this autoclave cycle */
  downloadHorizontalReport(r: any) {
    let batchNo = '';
    if (r?.wagons?.length > 0) {
      batchNo = r.wagons[0].eBatch || r.wagons[0].mBatch || r.wagons[0].wBatch || '';
    }
    if (!batchNo) { alert('No batch number found.'); return; }
    this.horizontalReportService.downloadLifecycleExcel(batchNo, 'AUTOCLAVE').subscribe({
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
    this.horizontalReportService.downloadExcel(this.filterFromDate, this.filterToDate, undefined, 'AUTOCLAVE').subscribe({
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

  // ================= IMPORT =================
  onImportSelect(event: Event): void {
    const value = (event.target as HTMLSelectElement).value;

    if (value === 'excel') {
      const fileInput = document.querySelector(
        'input[type="file"]'
      ) as HTMLInputElement;

      fileInput?.click();
    }

    (event.target as HTMLSelectElement).value = '';
  }

  onExcelSelect(event: Event): void {
    const input = event.target as HTMLInputElement;
    if (!input.files?.length) return;

    const file = input.files[0];
    const reader = new FileReader();

    reader.onload = (e: any) => {
      const wb = XLSX.read(e.target.result, { type: 'binary' });
      const ws = wb.Sheets[wb.SheetNames[0]];
      const data = XLSX.utils.sheet_to_json(ws);

      console.log('Imported Excel Data:', data);

      // 🔥 OPTIONAL: call backend API here
      // this.service.bulkImport(data).subscribe(...)
    };

    reader.readAsBinaryString(file);
  }

  getUsedBatches(): string[] {
    const used: string[] = [];

    this.wagons.controls.forEach(ctrl => {
      const v = ctrl.value;
      if (v.eBatch) used.push(v.eBatch);
      if (v.mBatch) used.push(v.mBatch);
      if (v.wBatch) used.push(v.wBatch);
    });

    return used;
  }

  getAvailableBatchesFor(side: 'e' | 'm' | 'w'): string[] {

    const current = this.wagonForm.value;

    const used = this.getUsedBatches().filter(b =>
      b !== current.eBatch &&
      b !== current.mBatch &&
      b !== current.wBatch
    );

    return this.availableBatches.filter(b => {

      if (used.includes(b)) return false;

      if (side !== 'e' && current.eBatch === b) return false;
      if (side !== 'm' && current.mBatch === b) return false;
      if (side !== 'w' && current.wBatch === b) return false;

      return true;

    });

  }

  canAddWagon(): boolean {
    const v = this.wagonForm.value;
    return v.eBatch && v.mBatch && v.wBatch;
  }


}
