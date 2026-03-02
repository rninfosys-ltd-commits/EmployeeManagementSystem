import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { LocationService } from 'src/app/services/LocationService';

@Component({
  selector: 'app-state-master',
  templateUrl: './state-master.component.html',
  styleUrls: ['./state-master.component.css']
})

export class StateMasterComponent implements OnInit {

  form!: FormGroup;

  // ALL DATA
  states: any[] = [];

  // PAGINATED DATA
  paginatedStates: any[] = [];

  // EDIT STATE
  isEdit = false;
  editId: number | null = null;

  // PAGINATION
  page = 1;
  pageSize = 5;

  constructor(
    private fb: FormBuilder,
    private service: LocationService
  ) { }

  ngOnInit(): void {
    this.form = this.fb.group({
      name: ['', Validators.required]
    });

    this.loadStates();
  }

  // ================= LOAD STATES =================
  loadStates(): void {
    this.service.getAllStates().subscribe((res: any[]) => {
      // ✅ LATEST FIRST
      this.states = res.sort((a: any, b: any) => b.id - a.id);
      this.setPage(1);
    });
  }

  // ================= SUBMIT / UPDATE =================
  onSubmit(): void {
    if (this.form.invalid) {
      this.form.markAllAsTouched();
      return;
    }

    const payload: any = {
      name: this.form.value.name.trim()
    };

    if (this.isEdit && this.editId !== null) {
      payload.id = this.editId;
    }

    this.service.addState(payload).subscribe(() => {
      alert(this.isEdit ? 'State updated successfully' : 'State added successfully');
      this.reset();
      this.loadStates();
    });
  }

  // ================= EDIT =================
  edit(state: any): void {
    this.isEdit = true;
    this.editId = state.id;

    this.form.patchValue({
      name: state.name
    });
  }

  // ================= RESET =================
  reset(): void {
    this.form.reset();
    this.isEdit = false;
    this.editId = null;
  }

  // ================= PAGINATION =================
  setPage(page: number): void {
    this.page = page;
    const start = (page - 1) * this.pageSize;
    const end = start + this.pageSize;
    this.paginatedStates = this.states.slice(start, end);
  }

  totalPages(): number {
    return Math.ceil(this.states.length / this.pageSize);
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

}
