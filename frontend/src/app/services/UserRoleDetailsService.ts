import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Observable } from 'rxjs';
import { APP_CONFIG } from '../config/config';

@Injectable({
    providedIn: 'root'
})
export class UserRoleDetailsService {

    private baseUrl = `${APP_CONFIG.BASE_URL}`;

    constructor(private http: HttpClient) { }

    // ================= AUTH HEADERS =================
    private authHeaders() {
        return {
            headers: new HttpHeaders({
                Authorization: `Bearer ${localStorage.getItem('token')}`
            })
        };
    }

    // ================= USER ROLE DETAILS =================

    save(data: any): Observable<any> {
        return this.http.post(
            `${this.baseUrl}/api/user-role-details`,
            data,
            this.authHeaders()
        );
    }

    getAll(): Observable<any[]> {
        return this.http.get<any[]>(
            `${this.baseUrl}/api/user-role-details`,
            this.authHeaders()
        );
    }

    delete(id: number): Observable<any> {
        return this.http.delete(
            `${this.baseUrl}/api/user-role-details/${id}`,
            this.authHeaders()
        );
    }

    getByUsername(username: string): Observable<any> {
        return this.http.get<any>(
            `${this.baseUrl}/api/user-role-details/username/${username}`,
            this.authHeaders()
        );
    }

    // ================= DROPDOWNS =================

    // ✅ ONLY ROLE_PARTY_NAME
    getPartyUsers(): Observable<any[]> {
        return this.http.get<any[]>(
            `${this.baseUrl}${APP_CONFIG.API.USERS}/parties`,
            this.authHeaders()
        );
    }

    // ✅ ROOT MASTER
    getRoots(): Observable<any[]> {
        return this.http.get<any[]>(
            `${this.baseUrl}${APP_CONFIG.API.ROOTS}`,
            this.authHeaders()
        );
    }
}
