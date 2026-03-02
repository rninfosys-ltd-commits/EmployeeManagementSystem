import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { RootMasterService } from 'src/app/services/RootMasterService';

@Component({
  selector: 'app-root-master',
  templateUrl: './root-master.component.html',
  styleUrls: ['./root-master.component.css']
})
export class RootMasterComponent implements OnInit {

  form!: FormGroup;

  // ALL DATA
  roots: any[] = [];

  // PAGINATED DATA
  paginatedRoots: any[] = [];

  // EDIT STATE
  isEdit: boolean = false;
  editId: number | null = null;

  // PAGINATION
  page = 1;
  pageSize = 5;

  constructor(
    private fb: FormBuilder,
    private rootService: RootMasterService
  ) { }

  ngOnInit(): void {
    this.form = this.fb.group({
      routeName: ['', Validators.required]
    });

    this.loadRoots();
  }

  // ================= LOAD ROOTS =================
  loadRoots(): void {
    this.rootService.getAll().subscribe({
      next: (data) => {
        // ✅ DESCENDING ORDER (LATEST FIRST)
        this.roots = data.sort((a: any, b: any) => b.id - a.id);
        this.setPage(1);
      },
      error: () => {
        alert('Failed to load root list');
      }
    });
  }
  prevPage(): void {
    if (this.page > 1) {
      this.setPage(this.page - 1);
    }
  }

  nextPage(): void {
    if (this.page < this.totalPages()) {
      this.setPage(this.page + 1);
    }
  }

  // ================= SUBMIT / UPDATE =================
  onSubmit(): void {
    if (this.form.invalid) {
      this.form.markAllAsTouched();
      return;
    }

    const payload: any = {
      routeName: this.form.value.routeName.trim()
    };

    // UPDATE CASE
    if (this.isEdit && this.editId !== null) {
      payload.id = this.editId;
    }

    this.rootService.save(payload).subscribe({
      next: () => {
        alert(this.isEdit ? 'Root updated successfully' : 'Root added successfully');
        this.resetForm();
        this.loadRoots();
      },
      error: (err) => {

        // 🔐 HANDLE UNAUTHORIZED CLEANLY
        if (err.status === 401) {
          alert('Duplicate entry not allowed');
          return;
        }

        // ❌ DUPLICATE / VALIDATION MESSAGE FROM BACKEND
        if (err?.error?.message) {
          alert(err.error.message);
          return;
        }

        // ❌ FALLBACK
        alert('Error saving root');
      }
    });
  }

  // ================= EDIT =================
  editRoot(root: any): void {
    this.isEdit = true;
    this.editId = root.id;

    this.form.patchValue({
      routeName: root.routeName
    });
  }

  // ================= RESET =================
  resetForm(): void {
    this.form.reset();
    this.isEdit = false;
    this.editId = null;
  }

  // ================= PAGINATION =================
  setPage(page: number): void {
    this.page = page;
    const start = (page - 1) * this.pageSize;
    const end = start + this.pageSize;
    this.paginatedRoots = this.roots.slice(start, end);
  }

  totalPages(): number {
    return Math.ceil(this.roots.length / this.pageSize);
  }
}
