import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { Employee } from '../../models/employee.model';
import { EmployeeService } from '../../services/employee.service';
import { AuthService } from 'src/app/services/auth.service';

@Component({
  selector: 'app-employee-list',
  templateUrl: './employee-list.component.html',
  styleUrls: ['./employee-list.component.css']
})
export class EmployeeListComponent implements OnInit {

  employees: Employee[] = [];
  paginatedEmployees: Employee[] = [];

  isLoading = false;
  errorMessage = '';
  isLoggedIn = false;
  currentUser: any;

  // pagination
  currentPage = 1;
  pageSize = 5;
  totalPages = 0;

  constructor(
    private employeeService: EmployeeService,
    private router: Router,
    private authService: AuthService
  ) { }

  ngOnInit(): void {
    this.loadEmployees();
    this.isLoggedIn = this.authService.isAuthenticated();
    if (this.isLoggedIn) {
      this.currentUser = this.authService.getCurrentUser();
    }
  }

  loadEmployees(): void {
    this.isLoading = true;
    this.errorMessage = '';

    this.employeeService.getAllEmployees().subscribe({
      next: (data) => {
        this.employees = data;
        this.currentPage = 1;
        this.calculatePagination();
        this.isLoading = false;
      },
      error: (err) => {
        this.employees = [];
        this.paginatedEmployees = [];
        this.errorMessage = 'Failed to load employees. Please try again.';
        this.isLoading = false;
        console.error('Error loading employees:', err);
      }
    });
  }

  calculatePagination(): void {
    this.totalPages = Math.ceil(this.employees.length / this.pageSize);
    this.updatePaginatedEmployees();
  }

  updatePaginatedEmployees(): void {
    const start = (this.currentPage - 1) * this.pageSize;
    const end = start + this.pageSize;
    this.paginatedEmployees = this.employees.slice(start, end);
  }

  nextPage(): void {
    if (this.currentPage < this.totalPages) {
      this.currentPage++;
      this.updatePaginatedEmployees();
    }
  }

  prevPage(): void {
    if (this.currentPage > 1) {
      this.currentPage--;
      this.updatePaginatedEmployees();
    }
  }

  editEmployee(id: number): void {
    this.router.navigate(['/employees/edit', id]);
  }

  deleteEmployee(id: number): void {
    if (confirm('Are you sure you want to delete this employee?')) {
      this.employeeService.deleteEmployee(id).subscribe({
        next: () => {
          this.loadEmployees();
        },
        error: (err) => {
          alert('Failed to delete employee. Please try again.');
          console.error('Error deleting employee:', err);
        }
      });
    }
  }

  addEmployee(): void {
    this.router.navigate(['/employees/add']);
  }
}
