import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Observable } from 'rxjs';
import { APP_CONFIG } from '../config/config';

@Injectable({
    providedIn: 'root'
})
export class AutoclaveService {

    // ✅ Consistent API base
    private baseUrl = `${APP_CONFIG.BASE_URL}/api/autoclave`;

    constructor(private http: HttpClient) { }

    // ================= AUTH HEADERS =================
    private authHeaders() {
        const token = localStorage.getItem('token');
        return {
            headers: new HttpHeaders({
                Authorization: token ? `Bearer ${token}` : ''
            })
        };
    }

    // ================= CRUD =================

    getAll(): Observable<any[]> {
        return this.http.get<any[]>(
            this.baseUrl,
            this.authHeaders()
        );
    }

    getById(id: number): Observable<any> {
        return this.http.get<any>(
            `${this.baseUrl}/${id}`,
            this.authHeaders()
        );
    }

    save(data: any): Observable<any> {
        return this.http.post<any>(
            `${this.baseUrl}/save`,
            data,
            this.authHeaders()
        );
    }

    update(id: number, data: any): Observable<any> {
        return this.http.put<any>(
            `${this.baseUrl}/${id}`,
            data,
            this.authHeaders()
        );
    }

    delete(id: number): Observable<any> {
        return this.http.delete<any>(
            `${this.baseUrl}/${id}`,
            this.authHeaders()
        );
    }
}
