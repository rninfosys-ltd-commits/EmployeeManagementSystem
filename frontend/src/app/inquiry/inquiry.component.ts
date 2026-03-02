import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { InquiryService } from '../services/inquiry.service';
import { LeadService } from '../services/lead.service';
import { ProjectService } from '../services/project.service';

import jsPDF from 'jspdf';
import autoTable from 'jspdf-autotable';
import * as XLSX from 'xlsx';

@Component({
  selector: 'app-inquiry',
  templateUrl: './inquiry.component.html',
  styleUrls: ['./inquiry.component.css']
})
export class InquiryComponent implements OnInit {

  form!: FormGroup;
  loading = false;

  leads: any[] = [];
  projects: any[] = [];
  inquiries: any[] = [];
  filteredInquiries: any[] = [];

  currentPage = 1;
  pageSize = 5;
  totalPages = 0;
  paginatedInquiries: any[] = [];

  excelPreview: any[] = [];
  hasExcelErrors = false;


  showInquiries = true;
  isEditMode = false;
  editInquiryId: number | null = null;

  filterFromDate = '';
  filterToDate = '';
  filterStatus = '';
  filterInquiryStatus: number | '' = '';


  statusList = [
    { id: 1, name: 'Open' },
    { id: 2, name: 'In Progress' },
    { id: 3, name: 'Closed' },
    { id: 4, name: 'Success' },
    { id: 5, name: 'Cancelled' }
  ];

  constructor(
    private fb: FormBuilder,
    private inquiryService: InquiryService,
    private leadService: LeadService,
    private projectService: ProjectService
  ) { }

  ngOnInit(): void {

    const today = new Date().toISOString().split('T')[0];
    this.filterFromDate = '';
    this.filterToDate = '';

    this.form = this.fb.group({
      inqueryDate: [today, Validators.required],
      inqStatusId: ['', Validators.required],
      leadAccountId: ['', Validators.required],
      projectCode: ['', Validators.required],
      unitCode: ['', Validators.required],
      rate: [0, Validators.required],
      quantity: [1, Validators.required],
      total: [0],
      particulars: [''],
      isActive: [1]
    });

    this.loadLeads();
    this.loadProjects();
    this.loadInquiries();

    this.form.valueChanges.subscribe(v => {
      this.form.patchValue(
        { total: (v.rate || 0) * (v.quantity || 0) },
        { emitEvent: false }
      );
    });
  }

  toggleInquiries() {
    this.showInquiries = !this.showInquiries;
  }

  loadLeads() {
    this.leadService.getAll().subscribe(res => this.leads = res);
  }
  loadProjects() {
    this.projectService.getAll().subscribe(res => {
      console.log('Projects:', res);
      this.projects = res;
    });
  }


  loadInquiries() {
    this.inquiryService.getAll().subscribe(res => {
      this.inquiries = res;

      // ✅ DEFAULT CURRENT MONTH
      const now = new Date();
      const start = new Date(now.getFullYear(), now.getMonth(), 1).getTime();
      const end = new Date(now.getFullYear(), now.getMonth() + 1, 0, 23, 59, 59).getTime();

      this.filteredInquiries = this.inquiries.filter(i => {
        const d = new Date(i.inqueryDate).getTime();
        return d >= start && d <= end;
      });

      this.setupPagination();
    });
  }

  /* ================= PAGINATION ================= */

  setupPagination() {
    this.totalPages = Math.ceil(this.filteredInquiries.length / this.pageSize);
    this.currentPage = 1;
    this.updatePaginatedData();
  }

  updatePaginatedData() {
    const sorted = [...this.filteredInquiries]
      .sort((a, b) => b.inqueryId - a.inqueryId); // 🔥 DESC by ID

    const start = (this.currentPage - 1) * this.pageSize;
    this.paginatedInquiries = sorted.slice(start, start + this.pageSize);
  }


  nextPage() {
    if (this.currentPage < this.totalPages) {
      this.currentPage++;
      this.updatePaginatedData();
    }
  }

  prevPage() {
    if (this.currentPage > 1) {
      this.currentPage--;
      this.updatePaginatedData();
    }
  }

  /* ================= FILTER ================= */

  applyFilters() {

    const from =
      this.filterInquiryStatus || this.filterStatus
        ? null
        : this.filterFromDate
          ? new Date(this.filterFromDate).getTime()
          : null;

    const to =
      this.filterInquiryStatus || this.filterStatus
        ? null
        : this.filterToDate
          ? new Date(this.filterToDate + 'T23:59:59').getTime()
          : null;

    this.filteredInquiries = this.inquiries.filter(i => {

      const d = new Date(i.inqueryDate).getTime();

      const dateOk =
        (!from || d >= from) &&
        (!to || d <= to);

      const activeOk =
        this.filterStatus !== ''
          ? Number(i.isActive) === Number(this.filterStatus)
          : true;

      const inquiryOk =
        this.filterInquiryStatus !== ''
          ? Number(i.inqStatusId) === Number(this.filterInquiryStatus)
          : true;

      return dateOk && activeOk && inquiryOk;
    });

    this.setupPagination();
  }






  getToday(): string {
    const today = new Date();
    return today.toISOString().split('T')[0]; // yyyy-MM-dd
  }



  clearFilters() {
    const today = this.getToday();
    this.filterFromDate = today;   // ✅ today
    this.filterToDate = today;
    this.filterStatus = '';
    this.filterInquiryStatus = '';
    this.filteredInquiries = [...this.inquiries];
    this.setupPagination();
  }

  /* ================= EXPORT ================= */

  exportData(type: string) {
    if (type === 'pdf') this.exportPDF();
    if (type === 'excel') this.exportExcel();
  }

  exportPDF() {
    const doc = new jsPDF();
    doc.text('Inquiry List', 14, 15);

    autoTable(doc, {
      head: [['#', 'Date', 'Lead', 'Project', 'Status', 'Inquiry Status', 'Total']],
      body: this.filteredInquiries.map((i, idx) => [
        idx + 1,
        new Date(i.inqueryDate).toLocaleDateString(),
        this.getLeadName(i.leadAccountId),
        this.getProjectName(i.projectCode),
        i.isActive ? 'Active' : 'Inactive',
        this.getInquiryStatusName(i.inqStatusId),
        i.total
      ]),
      startY: 20
    });

    doc.save('inquiries.pdf');
  }


  exportExcel() {
    const data = this.filteredInquiries.map(i => ({
      'Date': new Date(i.inqueryDate).toISOString().split('T')[0],

      // 🔥 EXACT DB VALUES
      'Lead': this.leads.find(l => l.leadId === i.leadAccountId)?.cName || '',
      'Project': this.projects.find(p => p.projectId === i.projectCode)?.projectName || '',

      'Inquiry Status': this.getInquiryStatusName(i.inqStatusId),
      'Unit Code': i.unitCode,
      'Qty': i.quantity,
      'Rate': i.rate,
      'Total': i.total,
      'Lead PAN No': '',
      'Lead Contact No': '',
    }));

    const ws = XLSX.utils.json_to_sheet(data);
    const wb = XLSX.utils.book_new();
    XLSX.utils.book_append_sheet(wb, ws, 'Inquiries');
    XLSX.writeFile(wb, 'inquiries.xlsx');
  }


  /* ================= CRUD ================= */

  editInquiry(i: any) {
    this.isEditMode = true;
    this.editInquiryId = i.inqueryId;
    this.showInquiries = false;

    this.form.patchValue({
      inqueryDate: i.inqueryDate?.split('T')[0],
      inqStatusId: i.inqStatusId,
      leadAccountId: i.leadAccountId,
      projectCode: i.projectCode,
      unitCode: i.unitCode,
      rate: i.rate,
      quantity: i.quantity,
      total: i.total,
      particulars: i.particulars,
      isActive: i.isActive
    });
  }

  submit() {
    if (this.form.invalid) return;

    this.loading = true;
    const payload = { ...this.form.value, inquiryId: this.editInquiryId };

    const req = this.isEditMode
      ? this.inquiryService.update(this.editInquiryId!, payload)
      : this.inquiryService.create(payload);

    req.subscribe(() => {
      alert(this.isEditMode ? 'Inquiry Updated' : 'Inquiry Created');
      this.resetForm();
    });
  }

  resetForm() {
    const today = new Date().toISOString().split('T')[0];
    this.form.reset({ inqueryDate: today, isActive: 1 });
    this.isEditMode = false;
    this.editInquiryId = null;
    this.loading = false;
    this.showInquiries = true;
    this.loadInquiries();
  }

  /* ================= HELPERS ================= */
  getLeadName(id: number) {
    return this.leads.find(l => l.leadId === id)?.cName ?? '-';
  }


  getProjectName(id: number) {
    return this.projects.find(p => p.projectId === id)?.projectName ?? '-';
  }

  getInquiryStatusName(id: number): string {
    return this.statusList.find(s => s.id === id)?.name ?? '-';
  }

  onImportSelect(event: any, fileInput: HTMLInputElement) {
    if (event.target.value === 'excel') {
      fileInput.click();
    }
    event.target.value = '';
  }


  importInquiryExcel(event: any) {

    if (!this.dataReady()) {
      alert('Leads and Projects are still loading. Please try again.');
      return;
    }

    const file = event.target.files[0];
    if (!file) return;

    this.excelPreview = [];
    this.hasExcelErrors = false;

    const reader = new FileReader();
    reader.onload = (e: any) => {
      const data = new Uint8Array(e.target.result);
      const workbook = XLSX.read(data, { type: 'array' });

      const sheet = workbook.Sheets[workbook.SheetNames[0]];
      const rows = XLSX.utils.sheet_to_json<any>(sheet, { defval: '' });

      this.validateInquiryExcel(rows);
    };

    reader.readAsArrayBuffer(file);
  }


  getCell(row: any, key: string): any {
    const foundKey = Object.keys(row).find(
      k => k.replace(/\s+/g, '').toLowerCase() === key.replace(/\s+/g, '').toLowerCase()
    );
    return foundKey ? row[foundKey] : null;
  }



  validateInquiryExcel(rows: any[]) {

    this.excelPreview = [];
    this.hasExcelErrors = false;

    rows.forEach((r: any) => {

      if (!r['Date'] && !r['Lead'] && !r['Project']) return;

      const errors: string[] = [];
      const panNo = r['PAN Number']
        ? String(r['PAN Number']).trim()
        : null;

      const contactNo = r['Contact Number']
        ? this.parseNumber(r['Contact Number'])
        : null;

      const projectBudget = this.parseNumber(r['Project Budget']);

      const date = r['Date'];
      const leadName = String(r['Lead'] || '').trim();
      const projectName = String(r['Project'] || '').trim();
      const inquiryStatusText = String(r['Inquiry Status'] || '').trim();
      const unitCode = Number(r['Unit Code']);

      const rate = this.parseNumber(r['Rate']);
      const qty = this.parseNumber(r['Qty']);

      const safeRate = isNaN(rate) ? 0 : rate;
      const safeQty = isNaN(qty) ? 1 : qty;
      const total = safeRate * safeQty;

      // 🔎 FIND LEAD (BY NAME)
      const lead = this.leads.find(l =>
        this.normalize(l.cName) === this.normalize(leadName)
      );

      // 🔎 FIND PROJECT (BY NAME)
      const project = this.projects.find(p =>
        this.normalize(p.projectName) === this.normalize(projectName)
      );

      // 🔎 FIND STATUS
      const inquiryStatus = this.statusList.find(s =>
        this.normalize(s.name) === this.normalize(inquiryStatusText)
      );

      // ❌ VALIDATIONS
      if (!date) errors.push('Date required');

      if (!leadName) errors.push('Lead required');

      if (!projectName) errors.push('Project required');

      if (!inquiryStatusText) errors.push('Inquiry Status required');
      else if (!inquiryStatus) errors.push('Invalid Inquiry Status');

      if (!unitCode || isNaN(unitCode)) errors.push('Unit Code required');

      // 🔥 DUPLICATE CHECK (ONLY IF BOTH EXIST)
      if (
        lead &&
        project &&
        unitCode &&
        this.isDuplicateInquiry(lead.leadId, project.projectId, unitCode)
      ) {
        errors.push('Inquiry already exists for this Lead + Project + Unit');
      }

      if (errors.length) this.hasExcelErrors = true;

      // ✅ PUSH PREVIEW OBJECT (WITH NAME + FLAG)
      this.excelPreview.push({
        inqueryDate: this.parseExcelDate(date),

        leadObj: lead
          ? { leadId: lead.leadId, cName: lead.cName, _isNew: false }
          : {
            cName: leadName,
            panNo,
            contactNo,
            _isNew: true
          },

        projectObj: project
          ? { projectId: project.projectId, projectName: project.projectName, _isNew: false }
          : {
            projectName,
            budgetAmt: isNaN(projectBudget) ? null : projectBudget,
            _isNew: true
          },


        inqStatusId: inquiryStatus ? inquiryStatus.id : null,
        unitCode,
        rate: safeRate,
        quantity: safeQty,
        total,
        isActive: 1,
        _errors: errors
      });
    });
  }



  normalize(value: any): string {
    return String(value || '')
      .replace(/\u00A0/g, ' ')
      .replace(/[^\w\s]/g, '')
      .replace(/\s+/g, ' ')
      .trim()
      .toLowerCase();
  }



  saveInquiryExcelToDB() {

    const validRows = this.excelPreview.filter(r => !r._errors.length);
    const invalidCount = this.excelPreview.length - validRows.length;

    if (!validRows.length) {
      alert('No valid inquiries to import');
      return;
    }

    let successCount = 0;
    const requests: Promise<any>[] = [];

    validRows.forEach(r => {

      const leadPromise = r.leadObj._isNew
        ? this.leadService.createFromInquiry({
          customerName: r.leadObj.cName,
          panNo: r.leadObj.panNo ?? null,
          contactNo: r.leadObj.contactNo ?? null,
          stateId: null,
          cityId: null,
          budget: null
        })

          .toPromise()
        : Promise.resolve(r.leadObj);

      const projectPromise = r.projectObj._isNew
        ? this.projectService.createFromInquiry({
          projectName: r.projectObj.projectName,
          budgetAmt: r.projectObj.budgetAmt ?? null
        }).toPromise()
        : Promise.resolve(r.projectObj);

      const req = Promise.all([leadPromise, projectPromise]).then(
        ([lead, project]: any[]) => {

          return this.inquiryService.create({
            inqueryDate: r.inqueryDate,
            leadAccountId: lead.leadId,
            projectCode: project.projectId,
            inqStatusId: r.inqStatusId,
            unitCode: r.unitCode,
            rate: r.rate,
            quantity: r.quantity,
            amount: r.rate * r.quantity,
            total: r.total,
            particulars: '',
            userId: 1,
            branchId: 1,
            orgId: 1,
            isActive: 1
          }).toPromise().then(() => successCount++);
        }
      );

      requests.push(req);
    });

    Promise.all(requests).then(() => {

      let message = `${successCount} inquiry(s) imported successfully.`;
      if (invalidCount > 0) {
        message += `\n${invalidCount} inquiry(s) skipped due to errors.`;
      }

      alert(message);

      this.clearExcelPreview();
      this.loadLeads();
      this.loadProjects();
      this.loadInquiries();
    });
  }




  clearExcelPreview() {
    this.excelPreview = [];
    this.hasExcelErrors = false;
  }

  parseExcelDate(value: any): string {
    if (typeof value === 'number') {
      const date = new Date((value - 25569) * 86400 * 1000);
      return date.toISOString().split('T')[0];
    }
    return new Date(value).toISOString().split('T')[0];
  }

  parseNumber(val: any): number {
    if (val === null || val === undefined || val === '') return NaN;

    // Remove currency symbols, commas, spaces
    const clean = String(val).replace(/[₹, ]/g, '');
    return Number(clean);
  }

  isDuplicateInquiry(
    leadId: number | null,
    projectId: number | null,
    unitCode: number | null
  ): boolean {
    if (!leadId || !projectId || !unitCode) return false;

    return this.inquiries.some(i =>
      Number(i.leadAccountId) === Number(leadId) &&
      Number(i.projectCode) === Number(projectId) &&
      Number(i.unitCode) === Number(unitCode)
    );
  }

  private dataReady(): boolean {
    return this.leads.length > 0 && this.projects.length > 0;
  }



}
