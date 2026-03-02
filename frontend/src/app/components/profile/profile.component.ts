import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { MatSnackBar } from '@angular/material/snack-bar';
import { AuthService } from 'src/app/services/auth.service';
import { UserService } from 'src/app/services/UserService';

@Component({
  selector: 'app-profile',
  templateUrl: './profile.component.html',
  styleUrls: ['./profile.component.css']
})
export class ProfileComponent implements OnInit {

  profileForm!: FormGroup;
  userId!: number;
  previewImage: string | null = null;
  uploadedBase64: string | null = null;
  showPassword = false;
  loading = false;

  constructor(
    private fb: FormBuilder,
    private snackBar: MatSnackBar,
    private authService: AuthService
  ) { }

  ngOnInit(): void {

    const storedUser = UserService.getUser();
    if (!storedUser) {
      console.error("No user found in localStorage");
      return;
    }

    this.userId = storedUser.id;

    this.profileForm = this.fb.group({
      username: ['', Validators.required],
      email: ['', [Validators.required, Validators.email]],
      mobile: [''],
      password: [''],
      role: [{ value: '', disabled: true }],
      active: [{ value: '', disabled: true }]
    });

    // Load LIVE user data from backend
    this.authService.getUserInfo(this.userId).subscribe({
      next: (res) => {
        this.profileForm.patchValue({
          username: res.username,
          email: res.email,
          mobile: res.mobile,
          role: res.role,
          active: res.active
        });

        if (res.profileImage) {
          this.previewImage = "data:image/jpeg;base64," + res.profileImage;
        }
      },
      error: (err) => {
        console.error(err);
        this.snackBar.open("Failed to load profile data", "Close", { duration: 3000 });
      }
    });
  }

  onImageSelected(event: any) {
    const file = event.target.files[0];
    if (!file) return;

    const reader = new FileReader();
    reader.onload = () => {
      const base64 = reader.result as string;
      this.previewImage = base64;
      this.uploadedBase64 = base64.split(",")[1];
    };
    reader.readAsDataURL(file);
  }

  togglePassword() {
    this.showPassword = !this.showPassword;
  }

  updateProfile() {
    const payload: any = {
      username: this.profileForm.value.username,
      email: this.profileForm.value.email,
      mobile: this.profileForm.value.mobile
    };

    if (this.profileForm.value.password) {
      payload.password = this.profileForm.value.password;
    }

    if (this.uploadedBase64) {
      payload.profileImage = this.uploadedBase64;
    }

    this.loading = true;

    this.authService.updateUserProfile(this.userId, payload).subscribe({
      next: () => {
        this.snackBar.open("Profile updated!", "Close", { duration: 3000 });
        this.loading = false;
      },
      error: () => {
        this.snackBar.open("Profile update failed", "Close", { duration: 3000 });
        this.loading = false;
      }
    });
  }
}
