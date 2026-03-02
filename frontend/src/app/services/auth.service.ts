import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { BehaviorSubject, Observable, tap } from 'rxjs';
import { LoginRequest } from '../models/auth.model';
import { TokenStorageService } from './token-storage.service';
import { UserService } from './UserService';
// import { APP_CONFIG } from '../config/config';
import { APP_CONFIG } from '../config/config';

@Injectable({
  providedIn: 'root'
})
export class AuthService {

  // ================= BASE URLs =================
  private AUTH_API = `${APP_CONFIG.BASE_URL}${APP_CONFIG.API.AUTH}`;
  private API_BASE = `${APP_CONFIG.BASE_URL}`;

  private loginStatus = new BehaviorSubject<boolean>(this.isAuthenticated());
  loginStatus$ = this.loginStatus.asObservable();

  constructor(
    private http: HttpClient,
    private tokenStorage: TokenStorageService,
    private userService: UserService
  ) { }

  // =======================================
  // 🔐 LOGIN
  // =======================================
  login(credentials: LoginRequest): Observable<any> {
    return this.http.post<any>(`${this.AUTH_API}/signin`, credentials).pipe(
      tap(response => {
        if (response.token) {

          // Save token
          this.tokenStorage.saveToken(response.token);

          // Save user (session)
          this.tokenStorage.saveUser({
            id: response.id,
            username: response.username,
            email: response.email,
            role: response.role
          });

          // 🔴 ADD THESE LINES
          localStorage.setItem('token', response.token);
          localStorage.setItem('role', response.role);
          localStorage.setItem('username', response.username);
          localStorage.setItem('userId', response.id);

          this.loginStatus.next(true);
        }
      })
    );
  }


  // =======================================
  // 🆕 REGISTER USER (COMPANY_OWNER ONLY)
  // =======================================
  register(user: { username: string; email: string; password: string; role: string }): Observable<any> {
    return this.http.post(`${this.AUTH_API}/signup`, user);
  }

  // =======================================
  // 🚪 LOGOUT
  // =======================================
  logout(): void {
    this.tokenStorage.signOut();
    this.loginStatus.next(false);
  }

  // =======================================
  // ⭐ CHECK AUTHENTICATION
  // =======================================
  isAuthenticated(): boolean {
    return this.tokenStorage.isLoggedIn();
  }

  getCurrentUser(): any {
    return this.tokenStorage.getUser();
  }

  // =======================================
  // OTHER APIS (BATCH + CUSTOMER)
  // =======================================
  createBatch(batch: any): Observable<any> {
    return this.http.post(
      `${this.API_BASE}${APP_CONFIG.API.BATCH}`,
      batch
    );
  }

  createCustomerTransaction(bactno: number, customer: any): Observable<any> {
    return this.http.post(
      `${this.API_BASE}${APP_CONFIG.API.CUSTOMER_TRN}/${bactno}`,
      customer
    );
  }

  getCurrentUserFromAPI(): Observable<any> {
    return this.http.get(`${this.AUTH_API}/me`);
  }

  // =======================================
  // ⭐ PROFILE APIs
  // =======================================
  getUserInfo(id: number): Observable<any> {
    return this.http.get(
      `${this.API_BASE}${APP_CONFIG.API.USERS}/${id}/info`
    );
  }

  updateUserProfile(id: number, data: any): Observable<any> {
    return this.http.put(
      `${this.API_BASE}${APP_CONFIG.API.USERS}/${id}/profile`,
      data
    );
  }

  // =======================================
  // 👥 USERS (COMPANY_OWNER)
  // =======================================
  getAllUsers(): Observable<any[]> {
    const headers = new HttpHeaders({
      Authorization: `Bearer ${this.tokenStorage.getToken()}`
    });

    return this.http.get<any[]>(
      `${this.API_BASE}${APP_CONFIG.API.USERS}/all`,
      { headers }
    );
  }

  // =======================================
  // 🏭 SUPPLIERS
  // =======================================
  getSuppliers(): Observable<any[]> {
    return this.http.get<any[]>(
      `${this.API_BASE}${APP_CONFIG.API.USERS}/parties`,
      {
        headers: {
          Authorization: `Bearer ${localStorage.getItem('token')}`
        }
      }
    );
  }

  getLoggedInUserId(): number | null {
    const user = this.tokenStorage.getUser();
    return user?.id || null;
  }


}
