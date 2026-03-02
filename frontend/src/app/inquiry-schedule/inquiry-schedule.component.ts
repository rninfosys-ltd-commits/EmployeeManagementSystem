import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup } from '@angular/forms';
import { HttpClient } from '@angular/common/http';
import { InquiryScheduleService } from '../services/InquiryScheduleService';

import jsPDF from 'jspdf';
import autoTable from 'jspdf-autotable';
import * as XLSX from 'xlsx';
import { APP_CONFIG } from '../config/config';

@Component({
  selector: 'app-inquiry-schedule',
  templateUrl: './inquiry-schedule.component.html',
  styleUrls: ['./inquiry-schedule.component.css']
})
export class InquiryScheduleComponent implements OnInit {

  showList = true;
  isEdit = false;

  isHistoryView = false;

  scheduleExcelPreview: any[] = [];
  hasScheduleExcelErrors = false;

  form!: FormGroup;
  selectedId: number | null = null;

  pageSize = 5;
  currentPage = 1;

  schedules: any[] = [];
  filteredSchedules: any[] = [];
  paginatedData: any[] = [];

  // 👇 THIS WILL POWER THE DROPDOWN
  inquiries: any[] = [];

  filterFromDate = '';
  filterToDate = '';
  filterStatus = '';

  constructor(
    private fb: FormBuilder,
    private scheduleService: InquiryScheduleService,
    private http: HttpClient
  ) { }

  ngOnInit(): void {
    this.form = this.fb.group({
      inquiryId: [''],   // CORRECT
      scheduleDate: [''],
      scheduleTime: [''],
      remark: [''],
      status: ['OPEN'],
      assignTo: ['']
    });

    this.loadSchedules();
    this.loadInquiryDropdown();
  }

  // ================= LOAD SCHEDULES =================
  loadSchedules(): void {
    this.scheduleService.getAllSchedules().subscribe(res => {
      this.schedules = res || [];
      this.filteredSchedules = [...this.schedules];
      this.currentPage = 1;
      this.updatePagination();
    });
  }

  // ================= 🔥 LOAD INQUIRY + LEAD NAME =================
  loadInquiryDropdown(): void {

    this.http.get<any[]>(
      APP_CONFIG.BASE_URL + APP_CONFIG.API.INQUIRIES
    ).subscribe(inquiries => {

      this.http.get<any[]>(
        APP_CONFIG.BASE_URL + APP_CONFIG.API.LEADS
      ).subscribe(leads => {

        // 3️⃣ Map inquiryId + lead name
        this.inquiries = inquiries.map(inq => {
          const lead = leads.find(l => l.leadId === inq.leadAccountId);

          return {
            inqId: inq.inqueryId,                // ✅ REAL inquiry id
            cname: lead ? lead.cname : '-'       // ✅ lead name
          };
        });

      });
    });
  }

  openForm(): void {
    this.showList = false;
    this.isEdit = false;
    this.selectedId = null;
    this.form.reset({ status: 'OPEN' });
  }

  backToList(): void {
    this.showList = true;
    this.form.reset();
  }

  editSchedule(data: any): void {
    this.isEdit = true;
    this.showList = false;
    this.selectedId = data.inqId;

    this.form.patchValue({
      inquiryId: data.inquiryId,
      scheduleDate: data.scheduleDate,
      scheduleTime: data.scheTime,
      remark: data.remark,
      status: this.normalizeStatus(data.inqStatus),

      assignTo: data.assignTo
    });

  }

  submit(): void {
    const payload = {
      inquiryId: this.form.value.inquiryId,
      scheduleDate: this.form.value.scheduleDate,
      scheTime: this.form.value.scheduleTime,
      remark: this.form.value.remark,
      inqStatus: this.form.value.status,
      assignTo: this.form.value.assignTo
    };

    if (this.isEdit && this.selectedId) {
      this.scheduleService.updateSchedule(this.selectedId, payload).subscribe(() => {
        this.loadSchedules();
        this.backToList();
      });
    } else {
      this.scheduleService.createSchedule(payload).subscribe(() => {
        this.loadSchedules();
        this.backToList();
      });
    }
  }
  onImportSelect(event: any, fileInput: HTMLInputElement): void {
    console.log("modal opne")
    if (event.target.value === 'excel') {
      setTimeout(() => {
        fileInput.click();
      });
    }

    setTimeout(() => {
      event.target.value = '';
    }, 200);
  }


  viewHistory(inquiryId: number): void {
    this.isHistoryView = true;
    this.filteredSchedules = this.schedules.filter(
      s => s.inquiryId === inquiryId
    );
    this.currentPage = 1;
    this.updatePagination();
  }



  backFromHistory(): void {
    this.isHistoryView = false;
    this.filteredSchedules = [...this.schedules];
    this.currentPage = 1;
    this.updatePagination();
  }

  // ================= FILTER =================
  applyFilters(): void {
    this.filteredSchedules = this.schedules.filter(s => {
      let ok = true;
      if (this.filterFromDate) ok = ok && new Date(s.scheduleDate) >= new Date(this.filterFromDate);
      if (this.filterToDate) ok = ok && new Date(s.scheduleDate) <= new Date(this.filterToDate);
      if (this.filterStatus) {
        ok = ok && this.normalizeStatus(s.inqStatus) === this.normalizeStatus(this.filterStatus);
      }

      return ok;
    });

    this.currentPage = 1;
    this.updatePagination();
  }
  getToday(): string {
    const today = new Date();
    return today.toISOString().split('T')[0]; // yyyy-MM-dd
  }


  clearFilters(): void {
    const today = this.getToday();
    this.filterFromDate = today;   // ✅ today
    this.filterToDate = today;     // ✅ today
    this.filterStatus = '';
    this.filteredSchedules = [...this.schedules];
    this.currentPage = 1;
    this.updatePagination();
  }

  // ================= PAGINATION =================
  updatePagination(): void {
    const start = (this.currentPage - 1) * this.pageSize;
    this.paginatedData = this.filteredSchedules.slice(start, start + this.pageSize);
  }

  get totalPages(): number {
    return Math.ceil(this.filteredSchedules.length / this.pageSize);
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

  exportData(type: string): void {
    if (!type) return;

    const data = this.filteredSchedules.map((s, i) => ({
      '#': i + 1,
      'Inquiry ID': s.inquiryId,
      'Schedule Date': s.scheduleDate,
      'Time': s.scheTime,
      'Remark': s.remark,
      'Status': s.inqStatus,
      'Assign To': s.assignTo
    }));

    if (type === 'pdf') {
      const doc = new jsPDF();
      autoTable(doc, {
        head: [Object.keys(data[0])],
        body: data.map(d => Object.values(d)),
        startY: 20
      });
      doc.save('schedule-report.pdf');
    }

    if (type === 'excel') {
      const ws = XLSX.utils.json_to_sheet(data);
      const wb = XLSX.utils.book_new();
      XLSX.utils.book_append_sheet(wb, ws, 'Schedules');
      XLSX.writeFile(wb, 'schedule-report.xlsx');
    }
  }

  importScheduleExcel(event: any) {
    const file = event.target.files[0];
    if (!file) return;

    this.scheduleExcelPreview = [];
    this.hasScheduleExcelErrors = false;

    const reader = new FileReader();
    reader.onload = (e: any) => {
      const data = new Uint8Array(e.target.result);
      const workbook = XLSX.read(data, { type: 'array' });

      const sheet = workbook.Sheets[workbook.SheetNames[0]];
      const rows = XLSX.utils.sheet_to_json<any>(sheet);

      this.validateScheduleExcel(rows);
    };

    reader.readAsArrayBuffer(file);
  }


  validateScheduleExcel(rows: any[]) {
    this.scheduleExcelPreview = [];
    this.hasScheduleExcelErrors = false;

    rows.forEach(r => {
      const errors: string[] = [];

      const inquiryId =
        r['Inquiry ID'] ??
        r['InquiryId'] ??
        r['Inquiry_Id'];

      const dateVal =
        r['Schedule Date'] ??
        r['ScheduleDate'] ??
        r['Date'];

      const timeVal = r['Time'];
      const statusVal = r['Status'];

      if (!inquiryId) errors.push('Inquiry ID required');
      if (!dateVal) errors.push('Schedule Date required');
      if (!timeVal) errors.push('Time required');
      if (!statusVal) errors.push('Status required');

      const normalizedStatus = this.normalizeStatus(statusVal);
      const allowed = ['OPEN', 'IN_PROGRESS', 'CLOSED', 'SUCCESS', 'CANCELLED'];

      if (statusVal && !allowed.includes(normalizedStatus)) {
        errors.push('Invalid Status');
      }

      if (errors.length) this.hasScheduleExcelErrors = true;

      this.scheduleExcelPreview.push({
        inquiryId: Number(inquiryId),
        scheduleDate: this.parseExcelDate(dateVal),
        scheTime: timeVal,
        inqStatus: normalizedStatus,
        assignTo: r['Assign To'] ?? r['AssignTo'] ?? '',
        remark: r['Remark'] ?? '',
        _errors: errors
      });
    });

    console.log('Preview rows:', this.scheduleExcelPreview);
  }



  parseExcelDate(value: any): string {
    if (typeof value === 'number') {
      const date = new Date((value - 25569) * 86400 * 1000);
      return date.toISOString().split('T')[0];
    }
    return new Date(value).toISOString().split('T')[0];
  }

  saveScheduleExcelToDB() {
    const validRows = this.scheduleExcelPreview.filter(r => !r._errors.length);

    validRows.forEach(r => {
      this.scheduleService.createSchedule({
        inquiryId: r.inquiryId,
        scheduleDate: r.scheduleDate,
        scheTime: r.scheTime,
        inqStatus: r.inqStatus,
        assignTo: r.assignTo,
        remark: r.remark
      }).subscribe();
    });

    alert('Schedules imported successfully');
    this.clearExcelPreview();
    this.loadSchedules();
  }

  clearExcelPreview() {
    this.scheduleExcelPreview = [];
    this.hasScheduleExcelErrors = false;
  }

  onScheduleImportSelect(event: any, fileInput: HTMLInputElement) {
    if (event.target.value === 'excel') {
      fileInput.click();
    }
    event.target.value = '';
  }
  normalizeStatus(value: any): string {
    if (!value) return '';
    return String(value).toUpperCase().replace(/\s+/g, '_');
  }


}


