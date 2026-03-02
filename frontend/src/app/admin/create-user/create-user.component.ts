import { Component } from '@angular/core';
import { FormBuilder, FormGroup } from '@angular/forms';
import { AuthService } from '../../services/auth.service';

@Component({
  selector: 'app-create-user',
  templateUrl: './create-user.component.html'
})
export class CreateUserComponent {

  form: FormGroup;
  successMessage = '';
  errorMessage = '';

  constructor(private fb: FormBuilder, private authService: AuthService) {
    this.form = this.fb.group({
      username: [''],
      email: [''],
      password: [''],
      role: ['USER']
    });
  }

  onSubmit() {
    this.authService.register(this.form.value).subscribe({
      next: (res: any) => {
        this.successMessage = "User created successfully!";
        this.errorMessage = "";
        this.form.reset();
      },
      error: (err: any) => {
        this.errorMessage = err.error.message || "Error creating user";
        this.successMessage = "";
      }
    });
  }

}
