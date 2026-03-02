import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { Router } from '@angular/router';
import { AuthService } from '../../services/auth.service';

@Component({
  selector: 'app-register',
  templateUrl: './register.component.html',
  styleUrls: ['./register.component.css']
})
export class RegisterComponent implements OnInit {

  registerForm!: FormGroup;
  isSuccessful = false;
  isSignUpFailed = false;
  errorMessage = '';
  isLoading = false;
  showRoleDropdown = false;
  selectedRole: string | null = null;
  roles = ["COMPANY_OWNER", "DIRECTOR", "MANAGER", "SUPERVISOR", "SENIOR_STAFF", "STAFF", "JUNIOR_STAFF", "INTERN", "USER"];

  constructor(
    private fb: FormBuilder,
    private authService: AuthService,
    private router: Router
  ) { }

  ngOnInit(): void {
    this.registerForm = this.fb.group({
      username: ['', [Validators.required, Validators.minLength(3)]],
      email: ['', [Validators.required, Validators.email]],
      password: ['', [Validators.required, Validators.minLength(6)]],
      confirmPassword: ['', Validators.required],
      role: ['', Validators.required]
    }, { validator: this.passwordMatchValidator });
  }

  passwordMatchValidator(g: FormGroup) {
    return g.get('password')?.value === g.get('confirmPassword')?.value
      ? null : { 'mismatch': true };
  }

  onSubmit(): void {
    if (this.registerForm.valid) {
      this.isLoading = true;
      this.isSignUpFailed = false;
      this.errorMessage = '';

      const userData = {
        username: this.registerForm.value.username,
        email: this.registerForm.value.email,
        password: this.registerForm.value.password,
        role: this.registerForm.value.role    // ⭐ SEND ROLE TO BACKEND
      };

      this.authService.register(userData).subscribe({
        next: () => {
          this.isSuccessful = true;
          this.isSignUpFailed = false;
          setTimeout(() => {
            this.router.navigate(['/login']);
          }, 1500);
        },
        error: (err) => {
          this.isSignUpFailed = true;
          this.errorMessage = err.error?.message || 'Registration failed. Please try again.';
          this.isLoading = false;
        },
        complete: () => {
          this.isLoading = false;
        }
      });
    }
  }
  toggleRoleDropdown() {
    this.showRoleDropdown = !this.showRoleDropdown;
  }

  selectRole(role: string) {
    this.selectedRole = role;
    this.registerForm.get('role')?.setValue(role);
    this.showRoleDropdown = false;
  }
  goToLogin(): void {
    this.router.navigate(['/login']);
  }
}
