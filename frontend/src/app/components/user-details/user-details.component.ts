import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { UserRoleDetailsService } from 'src/app/services/UserRoleDetailsService';
import { LocationService } from 'src/app/services/LocationService';
import { forkJoin } from 'rxjs';

type LocationKey = 'state' | 'dist' | 'tq' | 'city' | 'username' | 'routeName';

@Component({
  selector: 'app-user-details',
  templateUrl: './user-details.component.html',
  styleUrls: ['./user-details.component.css']
})
export class UserDetailsComponent implements OnInit {

  form!: FormGroup;

  allUsers: any[] = [];
  users: any[] = [];

  partyUsers: any[] = [];
  routes: any[] = [];

  states: any[] = [];
  districts: any[] = [];
  talukas: any[] = [];
  cities: any[] = [];

  isEdit = false;
  editId: number | null = null;

  page = 1;
  pageSize = 3;
  paginatedUsers: any[] = [];

  /* =========================
     🔽 DROPDOWN STATE (FIXED)
  ========================= */
  open: Record<LocationKey, boolean> = {
    state: false,
    dist: false,
    tq: false,
    city: false,
    username: false,
    routeName: false
  };

  constructor(
    private fb: FormBuilder,
    private service: UserRoleDetailsService,
    private locationService: LocationService
  ) { }

  // ================= INIT =================
  ngOnInit(): void {
    this.form = this.fb.group({
      username: ['', Validators.required],
      routeName: ['', Validators.required],
      gstNo: ['', Validators.required],
      address: [''],
      state: [''],
      dist: [''],
      tq: [''],
      city: [''],
      balance: ['']
    });

    this.loadStates();
    this.loadPartyUsers();
    this.loadRoots();
    this.loadUserDetails();

    this.form.valueChanges.subscribe(() => {
      this.applyFilter();
    });
  }

  // ================= DROPDOWN HELPERS =================

  toggle(key: LocationKey): void {
    (Object.keys(this.open) as LocationKey[])
      .forEach(k => this.open[k] = false);

    this.open[key] = true;
  }

  select(key: LocationKey, value: any): void {
    this.form.patchValue({ [key]: value });
    this.open[key] = false;

    if (key === 'username') {
      this.onUserChange();
    }
  }

  getName(list: any[], value: any): string {
    if (!value) return '';
    const item = list.find(x => x.id === value || x.name === value);
    return item ? item.name : value;
  }

  // ================= LOCATION LOAD =================

  loadStates(): void {
    this.locationService.getStates().subscribe(res => {
      this.states = res.sort((a, b) => a.name.localeCompare(b.name));
    });
  }

  onStateChange(): void {
    const stateId = this.form.get('state')?.value;

    this.form.patchValue({ dist: '', tq: '', city: '' });
    this.districts = [];
    this.talukas = [];
    this.cities = [];

    if (!stateId) return;

    this.locationService.getDistricts(stateId)
      .subscribe(res => this.districts = res);
  }

  onDistrictChange(): void {
    const districtId = this.form.get('dist')?.value;

    this.form.patchValue({ tq: '', city: '' });
    this.talukas = [];
    this.cities = [];

    if (!districtId) return;

    this.locationService.getTalukas(districtId)
      .subscribe(res => this.talukas = res);
  }

  onTalukaChange(): void {
    const talukaId = this.form.get('tq')?.value;

    this.form.patchValue({ city: '' });
    this.cities = [];

    if (!talukaId) return;

    this.locationService.getCities(talukaId)
      .subscribe(res => this.cities = res);
  }

  // ================= LOAD DATA =================

  loadPartyUsers(): void {
    this.service.getPartyUsers().subscribe(data => {
      this.partyUsers = data;
      this.loadUserDetails(); // Reload table once we have users
    });
  }

  loadRoots(): void {
    this.service.getRoots().subscribe(data => {
      this.routes = data;
    });
  }

  loadUserDetails(): void {
    // 1. Get All Party Users
    // 2. Get All Detail Records
    // 3. Join them so we see EVERY party user in the table
    forkJoin({
      users: this.service.getPartyUsers(),
      details: this.service.getAll()
    }).subscribe({
      next: (res) => {
        this.partyUsers = res.users;

        // Map them together
        this.allUsers = res.users.map(u => {
          const detail = res.details.find(d => d.username === u.username);
          return {
            ...u, // id, username from UserEntity
            ...detail, // routeName, gstNo, etc from UserRoleDetails
            detailId: detail ? detail.id : null // original detail ID
          };
        });

        this.applyFilter();
      }
    });
  }

  onUserChange(): void {
    const username = this.form.get('username')?.value;
    if (!username) return;

    // Check if we already have this user in our joined list
    const existing = this.allUsers.find(u => u.username === username && u.detailId);
    if (existing) {
      this.edit(existing);
    } else {
      // Clear but keep username
      const user = username;
      this.resetForm();
      this.form.patchValue({ username: user });
    }
  }

  // ================= FILTER =================

  applyFilter(): void {
    const { username, routeName, gstNo } = this.form.value;

    let filtered = [...this.allUsers];

    if (username) {
      filtered = filtered.filter(u =>
        u.username?.toLowerCase().includes(username.toLowerCase())
      );
    }

    if (routeName) {
      filtered = filtered.filter(u =>
        u.routeName?.toLowerCase().includes(routeName.toLowerCase())
      );
    }

    if (gstNo) {
      filtered = filtered.filter(u =>
        u.gstNo?.toLowerCase().includes(gstNo.toLowerCase())
      );
    }

    const uniqueMap = new Map<string, any>();
    filtered.forEach(u => {
      const key = `${u.username}_${u.routeName}_${u.gstNo}`;
      if (!uniqueMap.has(key)) {
        uniqueMap.set(key, u);
      }
    });

    this.users = Array.from(uniqueMap.values());
    this.setPage(1);
  }

  // ================= SUBMIT =================

  onSubmit(): void {
    if (this.form.invalid) {
      this.form.markAllAsTouched();
      return;
    }

    const payload: any = { ...this.form.value };

    if (this.isEdit && this.editId !== null) {
      payload.id = this.editId;
    }

    // Logic: If user already has a detail (even if not in 'isEdit' mode), we should update
    const existing = this.allUsers.find(u => u.username === payload.username && u.detailId);
    if (existing && !payload.id) {
      payload.id = existing.detailId;
    }

    this.service.save(payload).subscribe(() => {
      this.resetForm();
      this.loadUserDetails();
    });
  }

  // ================= EDIT =================

  edit(user: any): void {
    this.isEdit = !!user.detailId;
    this.editId = user.detailId || null;

    this.form.patchValue({
      username: user.username,
      routeName: user.routeName,
      gstNo: user.gstNo,
      address: user.address,
      balance: user.balance,
      state: user.state
    });

    // Helper to find ID by name or return as is if number
    const getId = (list: any[], value: any) => {
      if (!value) return null;
      if (!isNaN(Number(value))) return Number(value);
      return list.find(x => x.name === value)?.id || value;
    };

    // Trigger cascaded loads
    // 1. Get Districts for State
    const stateId = getId(this.states, user.state);
    if (stateId) {
      this.locationService.getDistricts(stateId).subscribe(dists => {
        this.districts = dists;
        this.form.patchValue({ dist: user.dist });

        // 2. Get Talukas for District
        const distId = getId(this.districts, user.dist);
        if (distId) {
          this.locationService.getTalukas(distId).subscribe(tqs => {
            this.talukas = tqs;
            this.form.patchValue({ tq: user.tq });

            // 3. Get Cities for Taluka
            const tqId = getId(this.talukas, user.tq);
            if (tqId) {
              this.locationService.getCities(tqId).subscribe(cities => {
                this.cities = cities;
                this.form.patchValue({ city: user.city });
              });
            }
          });
        }
      });
    }
  }

  // ================= RESET =================

  resetForm(): void {
    this.form.reset();
    this.isEdit = false;
    this.editId = null;
    this.applyFilter();
  }

  // ================= PAGINATION =================

  setPage(page: number): void {
    this.page = page;
    const start = (page - 1) * this.pageSize;
    const end = start + this.pageSize;
    this.paginatedUsers = this.users.slice(start, end);
  }

  totalPages(): number {
    return Math.ceil(this.users.length / this.pageSize);
  }
}
