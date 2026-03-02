import { Component, OnInit } from '@angular/core';
import { Router, NavigationEnd } from '@angular/router';
import { AuthService } from '../../services/auth.service';
import { FilterService } from '../../services/filter.service';
import { WorkflowService } from '../../services/workflow.service';
import { filter } from 'rxjs/operators';

@Component({
  selector: 'app-navbar',
  templateUrl: './navbar.component.html',
  styleUrls: ['./navbar.component.css']
})
export class NavbarComponent implements OnInit {
  isLoggedIn = false;
  currentUser: any = null;
  selectedMaster: string = "";
  selectedTransaction: string = "";

  isMenuOpen: boolean = false;
  constructor(
    private authService: AuthService,
    private router: Router
  ) { }

  ngOnInit(): void {
    // Initial check
    this.isLoggedIn = this.authService.isAuthenticated();
    this.currentUser = this.isLoggedIn ? this.authService.getCurrentUser() : null;

    // Listen for updates
    this.authService.loginStatus$.subscribe(isLogged => {
      this.isLoggedIn = isLogged;
      this.currentUser = isLogged ? this.authService.getCurrentUser() : null;
    });
  }


  logout(): void {
    this.authService.logout();
    this.currentUser = null;
    this.isLoggedIn = false;
    this.router.navigate(['/login']);
  }

  // navigate(event: any) {
  //   const page = event.target.value;
  //   if (page) {
  //     this.router.navigate([page]).then(() => {
  //       // reset both selects after navigation
  //       this.selectedMaster = "";
  //       this.selectedTransaction = "";
  //     });
  //   }
  // }

  navigate(event: any) {
    const page = event.target.value;

    if (page) {
      this.router.navigate([page]).then(() => {
        // reset dropdown so title always shows
        event.target.selectedIndex = 0;
      });
    }
  }


  openProfile() {
    this.router.navigate(['/profile']);

  }



}

