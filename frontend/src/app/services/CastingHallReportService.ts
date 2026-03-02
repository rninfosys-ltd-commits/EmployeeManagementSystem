// src/app/services/CastingHallReportService.ts

import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Observable } from 'rxjs';
import { APP_CONFIG } from '../config/config';
import { CastingImportResponse } from '../models/CastingImportResponse';

@Injectable({
    providedIn: 'root'
})
export class CastingHallReportService {

    private baseUrl = `${APP_CONFIG.BASE_URL}${APP_CONFIG.API.CASTING_REPORT}`;

    constructor(private http: HttpClient) { }

    // ================= AUTH HEADERS =================
    private getAuthHeaders() {
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
            this.getAuthHeaders()
        );
    }

    getById(id: number): Observable<any> {
        return this.http.get<any>(
            `${this.baseUrl}/${id}`,
            this.getAuthHeaders()
        );
    }

    save(data: any): Observable<any> {
        return this.http.post(
            this.baseUrl,
            data,
            this.getAuthHeaders()
        );
    }

    update(id: number, data: any): Observable<any> {
        return this.http.put(
            `${this.baseUrl}/${id}`,
            data,
            this.getAuthHeaders()
        );
    }

    delete(id: number): Observable<any> {
        return this.http.delete(
            `${this.baseUrl}/${id}`,
            this.getAuthHeaders()
        );
    }

    approve(id: number): Observable<any> {

        const userId = Number(localStorage.getItem('userId')); // 🔥 FIX
        const role = localStorage.getItem('role');

        return this.http.put(
            `${this.baseUrl}/${id}/approve?userId=${userId}&role=${role}`,
            {},
            this.getAuthHeaders()
        );
    }

    reject(id: number, reason: string): Observable<any> {

        const userId = Number(localStorage.getItem('userId')); // 🔥 FIX
        const role = localStorage.getItem('role');

        return this.http.post(
            `${this.baseUrl}/reject/${id}?userId=${userId}&role=${role}`,
            { reason },
            this.getAuthHeaders()
        );
    }




    importCasting(payload: any) {
        return this.http.post<CastingImportResponse>(
            `${this.baseUrl}/import`,
            payload,
            this.getAuthHeaders()
        );
    }

}
