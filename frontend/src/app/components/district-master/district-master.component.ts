import { Component, OnInit } from '@angular/core';
import { FormGroup, FormBuilder, Validators } from '@angular/forms';
import { LocationService } from 'src/app/services/LocationService';

@Component({
  selector: 'app-district-master',
  templateUrl: './district-master.component.html',
  styleUrls: ['./district-master.component.css']
})
export class DistrictMasterComponent implements OnInit {

  form!: FormGroup;

  states: any[] = [];
  districts: any[] = [];
  paginated: any[] = [];

  isEdit = false;
  editId: number | null = null;

  page = 1;
  pageSize = 5;

  constructor(
    private fb: FormBuilder,
    private service: LocationService
  ) { }

  ngOnInit(): void {
    this.form = this.fb.group({
      stateId: ['', Validators.required],
      name: ['', Validators.required]
    });

    this.loadStates();
    // this.loadDistricts();
  }

  loadStates(): void {
    this.service.getStates().subscribe(res => {
      this.states = res;
    });
  }

  loadDistricts(): void {
    this.service.getAllDistricts().subscribe((res: any[]) => {
      this.districts = res.sort((a, b) => b.id - a.id);
      this.setPage(1);
    });
  }

  onSubmit(): void {
    if (this.form.invalid) return;

    const payload = {
      name: this.form.value.name.trim()
    };

    const stateId = this.form.value.stateId;

    this.service.addDistrict(payload, stateId).subscribe(() => {
      alert(this.isEdit ? 'District Updated' : 'District Added');
      this.reset();
      // this.loadDistricts();
    });
  }

  edit(d: any): void {
    this.isEdit = true;
    this.editId = d.id;

    this.form.patchValue({
      name: d.name,
      stateId: d.state?.id
    });
  }

  reset(): void {
    this.form.reset();
    this.isEdit = false;
    this.editId = null;
  }

  setPage(page: number): void {
    this.page = page;
    const start = (page - 1) * this.pageSize;
    this.paginated = this.districts.slice(start, start + this.pageSize);
  }

  onStateChange(): void {
    const stateId = this.form.get('stateId')?.value;
    if (!stateId) return;

    this.service.getDistricts(stateId).subscribe(res => {
      this.districts = res;
      this.setPage(1);
    });
  }

  totalPages(): number {
    return Math.ceil(this.districts.length / this.pageSize);
  }


}
