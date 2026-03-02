import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { UserService } from '../services/UserService';
import { NotificationService } from '../services/notification.service';

@Component({
  selector: 'app-user-list',
  templateUrl: './user-list.component.html',
  styleUrls: ['./user-list.component.css']
})
export class UserListComponent implements OnInit {

  users: any[] = [];

  // 🔹 PAGINATION
  page = 1;
  pageSize = 5;
  paginatedUsers: any[] = [];

  isLoading = false;

  constructor(
    private userService: UserService,
    private router: Router,
    private notify: NotificationService
  ) { }

  ngOnInit(): void {
    this.loadUsers();
  }

  loadUsers() {
    this.isLoading = true;
    this.userService.getAllUsers().subscribe({
      next: (data) => {
        this.users = data;
        this.setPage(1); // ✅ init pagination
        this.isLoading = false;
      }
    });
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

  addUser() {
    this.router.navigate(['/users/add']);
  }

  editUser(id: number) {
    this.router.navigate(['/users/edit', id]);
  }

  async deleteUser(id: number) {
    const confirmed = await this.notify.confirm({
      title: 'Delete User',
      message: 'Are you sure you want to delete this user?',
      confirmText: 'Delete',
      type: 'error'
    });
    if (confirmed) {
      this.userService.deleteUser(id).subscribe(() => {
        this.notify.success('User deleted successfully!');
        this.loadUsers();
      });
    }
  }

  async activate(id: number) {
    const confirmed = await this.notify.confirm({
      title: 'Activate User',
      message: 'Are you sure you want to activate this user?',
      confirmText: 'Activate',
      type: 'success'
    });
    if (confirmed) {
      this.userService.activateUser(id).subscribe(() => {
        this.notify.success('User activated!');
        this.loadUsers();
      });
    }
  }

  async deactivate(id: number) {
    const confirmed = await this.notify.confirm({
      title: 'Deactivate User',
      message: 'Are you sure you want to deactivate this user?',
      confirmText: 'Deactivate',
      type: 'warning'
    });
    if (confirmed) {
      this.userService.deactivateUser(id).subscribe(() => {
        this.notify.warning('User deactivated.');
        this.loadUsers();
      });
    }
  }
}
