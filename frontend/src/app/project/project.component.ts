import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { ProjectService } from '../services/project.service';
import { AuthService } from '../services/auth.service';

import jsPDF from 'jspdf';
import autoTable from 'jspdf-autotable';
import * as XLSX from 'xlsx';

@Component({
  selector: 'app-project',
  templateUrl: './project.component.html',
  styleUrls: ['./project.component.css']
})
export class ProjectComponent implements OnInit {

  form!: FormGroup;
  loading = false;

  showProjects = true;
  openDropdown: string | null = null;
  projects: any[] = [];
  paginatedProjects: any[] = [];

  currentPage = 1;
  pageSize = 7;
  totalPages = 0;

  isEditMode = false;
  editProjectId: number | null = null;

  filterFromDate = '';
  filterToDate = '';
  filterStatus = '';

  // ✅ MODAL DATA
  excelPreview: any[] = [];
  hasExcelErrors = false;

  apiMessage = '';
  apiErrorProjects: string[] = [];

  filteredProjects: any[] = [];

  constructor(
    private fb: FormBuilder,
    private projectService: ProjectService,
    private authService: AuthService
  ) { }

  ngOnInit(): void {
    const userId = this.authService.getCurrentUser()?.id || 0;
    const today = new Date().toISOString().split('T')[0];
    this.filterFromDate = today;
    this.filterToDate = today;


    this.form = this.fb.group({
      projectName: ['', Validators.required],
      sanctionDate: [today, Validators.required],
      startDate: [today, Validators.required],
      endDate: [today, Validators.required],
      srvGutNo: [''],
      previousLandOwner: [''],
      landOwner: [''],
      builderName: [''],
      reraNo: [0],
      address: [''],
      budgetAmt: ['', Validators.required],
      orgId: [0],
      branchId: [0],
      userId: [userId],
      isActive: [1]
    });

    this.loadProjects();
  }

  // ================= PROJECT LIST =================

  loadProjects() {
    this.projectService.getAll().subscribe({
      next: (res) => {
        this.projects = res || [];

        // ✅ DEFAULT: CURRENT MONTH PROJECTS
        const now = new Date();
        const monthStart = new Date(now.getFullYear(), now.getMonth(), 1).getTime();
        const monthEnd = new Date(
          now.getFullYear(),
          now.getMonth() + 1,
          0,
          23,
          59,
          59
        ).getTime();

        this.filteredProjects = this.projects.filter(p => {
          const start = new Date(p.startDate).getTime();
          return start >= monthStart && start <= monthEnd;
        });

        this.setupPagination();
      },
      error: () => alert('Failed to load projects')
    });
  }

  toggleDropdown(type: string) {
    this.openDropdown = this.openDropdown === type ? null : type;
  }

  setStatus(value: string) {
    this.filterStatus = value;
    this.applyFilters();
    this.openDropdown = null;
  }

  setupPagination() {
    this.totalPages = Math.ceil(this.filteredProjects.length / this.pageSize);

    this.currentPage = 1;
    this.updatePaginatedData();
  }

  updatePaginatedData() {
    const sorted = [...this.filteredProjects]
      .sort((a, b) => b.projectId - a.projectId); // ✅ DESC by ID

    const start = (this.currentPage - 1) * this.pageSize;
    this.paginatedProjects = sorted.slice(start, start + this.pageSize);
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

  // ================= CREATE / EDIT =================

  openCreateForm() {
    this.isEditMode = false;
    this.editProjectId = null;
    this.showProjects = false;
    this.apiMessage = '';
  }

  backToList() {
    this.showProjects = true;
    this.apiMessage = '';
  }

  editProject(p: any) {
    this.isEditMode = true;
    this.editProjectId = p.projectId;
    this.showProjects = false;
    this.apiMessage = '';
    this.form.patchValue(p);
  }

  submit() {
    if (this.form.invalid) return;

    this.loading = true;
    this.apiMessage = '';

    const payload = {
      ...this.form.getRawValue(),
      projectId: this.editProjectId
    };

    const req$ = this.isEditMode
      ? this.projectService.update(this.editProjectId!, payload)
      : this.projectService.create(payload);

    req$.subscribe({
      next: () => {
        alert(this.isEditMode ? 'Project updated' : 'Project created');
        this.resetForm();
      },
      error: (err) => {
        this.apiMessage = err?.error?.message || 'Operation failed';
        this.loading = false;
      }
    });
  }

  resetForm() {
    const today = new Date().toISOString().split('T')[0];
    this.form.reset({ sanctionDate: today, isActive: 1 });
    this.loading = false;
    this.isEditMode = false;
    this.showProjects = true;
    this.loadProjects();
  }

  // ================= EXCEL IMPORT (FIXED) =================

  importExcel(event: any) {
    const file = event.target.files[0];
    if (!file) return;

    const reader = new FileReader();

    reader.onload = (e: any) => {
      const workbook = XLSX.read(new Uint8Array(e.target.result), { type: 'array' });
      const sheet = workbook.Sheets[workbook.SheetNames[0]];
      const rows = XLSX.utils.sheet_to_json<any>(sheet);

      // 🔍 DEBUG LINE (ADD HERE)
      console.log('Excel row sample:', rows[0]);

      this.excelPreview = rows.map((r: any) => {

        const row = this.normalizeRow(r);

        return {
          projectName:
            row.projectname || '',

          startDate: this.formatExcelDate(
            row.startdate
          ),

          endDate: this.formatExcelDate(
            row.enddate
          ),

          sanctionDate: this.formatExcelDate(
            row.sanctiondate
          ),

          // 🔥 FIX: SUPPORT budgetAmt ALSO
          budgetAmt: Number(
            row.budget ??
            row.budgetamount ??
            row.budgetamt ??
            0
          ),

          // 🔥 FIX: SUPPORT isActive ALSO
          isActive:
            row.isactive === 1 ||
              row.isactive === '1' ||
              row.status === 'Active' ||
              row.status === 'Yes' ||
              row.activestatus === 'Yes'
              ? 1
              : 0,

          importStatus: 'PENDING',
          errorMessage: ''
        };

      });

      this.apiMessage = '';
      this.hasExcelErrors = false;
    };


    reader.readAsArrayBuffer(file);
  }
  formatExcelDate(value: any): string | null {
    if (!value) return null;

    // ✅ Case 1: Already ISO date (yyyy-mm-dd)
    if (typeof value === 'string' && /^\d{4}-\d{2}-\d{2}$/.test(value)) {
      return value;
    }

    // ✅ Case 2: Excel numeric date
    if (typeof value === 'number') {
      const date = XLSX.SSF.parse_date_code(value);
      if (!date) return null;

      return `${date.y}-${String(date.m).padStart(2, '0')}-${String(date.d).padStart(2, '0')}`;
    }

    // ✅ Case 3: dd/mm/yyyy
    if (typeof value === 'string' && value.includes('/')) {
      const parts = value.split('/');
      if (parts.length === 3) {
        const [d, m, y] = parts;
        return `${y}-${String(m).padStart(2, '0')}-${String(d).padStart(2, '0')}`;
      }
    }

    return null;
  }




  // ================= SAVE TO BACKEND =================

  saveExcelToDB() {
    const payload = {
      projects: this.excelPreview,
      uploadedBy: this.authService.getCurrentUser()?.id || 0
    };

    this.projectService.importProjects(payload).subscribe({
      next: (res: any) => {

        // 🔥 Map backend result to preview rows
        res.results.forEach((r: any) => {
          const row = this.excelPreview.find(
            p => p.projectName === r.projectName
          );

          if (row) {
            row.importStatus = r.status;
            row.errorMessage = r.error || '';
          }
        });

        this.apiMessage = `${res.savedCount} saved, ${res.errorCount} failed`;
        this.loadProjects();
      },
      error: () => {
        this.apiMessage = 'Import failed';
      }
    });
  }






  clearExcelPreview() {
    this.excelPreview = [];
    this.hasExcelErrors = false;
    this.apiMessage = '';
  }

  // ================= EXPORT =================

  exportData(type: string) {
    if (type === 'pdf') this.exportPDF();
    if (type === 'excel') this.exportExcel();
  }

  exportPDF() {
    const doc = new jsPDF();
    autoTable(doc, {
      head: [['ID', 'Project Name', 'Start', 'End', 'Budget', 'Status']],
      body: this.projects.map(p => [
        p.projectId,
        p.projectName,
        p.startDate,
        p.endDate,
        p.budgetAmt,
        p.isActive ? 'Active' : 'Inactive'
      ])
    });
    doc.save('projects.pdf');
  }

  exportExcel() {
    const ws = XLSX.utils.json_to_sheet(this.projects);
    const wb = XLSX.utils.book_new();
    XLSX.utils.book_append_sheet(wb, ws, 'Projects');
    XLSX.writeFile(wb, 'projects.xlsx');
  }

  onImportSelect(event: any, fileInput: HTMLInputElement) {
    if (event.target.value === 'excel') fileInput.click();
    event.target.value = '';
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

    this.filteredProjects = this.projects.filter(p => {
      const start = new Date(p.startDate).getTime();
      const end = new Date(p.endDate).getTime();

      const dateOk =
        (!from || start >= from) &&
        (!to || end <= to);

      const statusOk =
        this.filterStatus !== ''
          ? String(p.isActive) === this.filterStatus
          : true;

      return dateOk && statusOk;
    });

    this.currentPage = 1;
    this.setupPagination();
  }

  clearFilters() {
    this.filterFromDate = '';
    this.filterToDate = '';
    this.filterStatus = '';

    this.filteredProjects = [...this.projects];
    this.currentPage = 1;
    this.setupPagination();
  }

  normalizeRow(row: any) {
    const normalized: any = {};

    Object.keys(row).forEach(key => {
      const cleanKey = key
        .toLowerCase()
        .replace(/\s+/g, '')      // remove spaces
        .replace(/_/g, '');       // remove underscores

      normalized[cleanKey] = row[key];
    });

    return normalized;
  }


}
