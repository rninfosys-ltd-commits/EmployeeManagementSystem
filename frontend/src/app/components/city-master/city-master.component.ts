import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { LocationService } from 'src/app/services/LocationService';

@Component({
  selector: 'app-city-master',
  templateUrl: './city-master.component.html',
  styleUrls: ['./city-master.component.css']
})
export class CityMasterComponent implements OnInit {

  form!: FormGroup;

  talukas: any[] = [];
  cities: any[] = [];
  paginatedCities: any[] = [];

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
      name: ['', Validators.required],
      talukaId: ['', Validators.required]
    });

    this.loadTalukas();
    // ❌ DO NOT load cities here
  }

  // ================= LOAD =================

  loadTalukas(): void {
    this.service.getAllTalukas().subscribe(res => {
      this.talukas = res;
    });
  }

  // ✅ LOAD CITIES ONLY AFTER TALUKA SELECT
  onTalukaChange(): void {
    const talukaId = this.form.get('talukaId')?.value;
    if (!talukaId) return;

    this.service.getCitiesByTaluka(talukaId).subscribe((res: any[]) => {
      this.cities = res;
      this.setPage(1);
    });
  }

  // ================= SUBMIT =================

  onSubmit(): void {
    if (this.form.invalid) return;

    const payload = {
      name: this.form.value.name.trim()
    };

    const talukaId = this.form.value.talukaId;

    this.service.addCity(payload, talukaId).subscribe(() => {
      alert(this.isEdit ? 'City Updated' : 'City Saved');

      this.reset();

      // reload same taluka cities
      this.form.patchValue({ talukaId });
      this.onTalukaChange();
    });
  }

  // ================= EDIT =================

  edit(c: any): void {
    this.isEdit = true;
    this.editId = c.cityId;

    this.form.patchValue({
      name: c.cityName,
      talukaId: c.talukaId
    });

    this.onTalukaChange();
  }

  // ================= UTILS =================

  reset(): void {
    this.form.reset();
    this.isEdit = false;
    this.editId = null;
  }

  setPage(page: number): void {
    this.page = page;
    const start = (page - 1) * this.pageSize;
    this.paginatedCities = this.cities.slice(start, start + this.pageSize);
  }

  totalPages(): number {
    return Math.ceil(this.cities.length / this.pageSize);
  }
}
