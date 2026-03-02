import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { UserService } from '../services/UserService';

@Component({
  selector: 'app-user-form',
  templateUrl: './user-form.component.html',
  styleUrls: ['./user-form.component.css']
})
export class UserFormComponent implements OnInit {

  form!: FormGroup;
  isEdit = false;
  userId: number | null = null;

  imagePreview: string | null = null;

  openRoleDropdown = false; // ✅ FIX FLAG

  roles = [
    'ROLE_COMPANY_OWNER',
    'ROLE_USER',
    'ROLE_DIRECTOR',
    'ROLE_MANAGER',
    'ROLE_SUPERVISOR',
    'ROLE_SENIOR_STAFF',
    'ROLE_STAFF',
    'ROLE_PARTY_NAME'
  ];

  constructor(
    private fb: FormBuilder,
    private userService: UserService,
    private route: ActivatedRoute,
    public router: Router
  ) { }

  ngOnInit(): void {
    this.form = this.fb.group({
      username: ['', Validators.required],
      email: ['', [Validators.required, Validators.email]],
      password: [''],
      role: ['', Validators.required],
      mobile: [''],
      profileImage: ['']
    });

    const id = this.route.snapshot.paramMap.get('id');
    if (id) {
      this.isEdit = true;
      this.userId = +id;
      this.loadUser(this.userId);
    } else {
      this.form.get('password')?.setValidators(Validators.required);
    }
  }

  loadUser(id: number) {
    this.userService.getUser(id).subscribe(user => {
      this.form.patchValue({
        username: user.username,
        email: user.email,
        role: user.role,
        mobile: user.mobile
      });

      this.imagePreview = user.profileImage || null;
    });
  }

  onImageSelected(event: any) {
    const file = event.target.files[0];
    if (!file) return;

    const reader = new FileReader();
    reader.onload = () => {
      const base64 = reader.result as string;
      this.imagePreview = base64;
      this.form.patchValue({ profileImage: base64 });
    };
    reader.readAsDataURL(file);
  }

  roleDropdownOpen = false;

  toggleRoleDropdown() {
    this.roleDropdownOpen = !this.roleDropdownOpen;
  }

  selectRole(role: string) {
    this.form.get('role')?.setValue(role);
    this.roleDropdownOpen = false;
  }


  onSubmit() {
    if (this.form.invalid) return;

    const payload = { ...this.form.value };

    if (!payload.profileImage) delete payload.profileImage;
    if (!payload.password) delete payload.password;

    if (this.isEdit) {
      this.userService.updateUser(this.userId!, payload)
        .subscribe(() => this.router.navigate(['/users']));
    } else {
      this.userService.createUser(payload)
        .subscribe(() => this.router.navigate(['/users']));
    }
  }
}
