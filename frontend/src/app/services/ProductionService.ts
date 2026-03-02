// src/app/services/ProductionService.ts

import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Observable } from 'rxjs';
import { APP_CONFIG } from '../config/config';
import { ProductionImportResponse } from '../models/ProductionImportResponse';

@Injectable({
    providedIn: 'root'
})
export class ProductionService {

    private baseUrl = `${APP_CONFIG.BASE_URL}${APP_CONFIG.API.PRODUCTION}`;

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




    approve(id: number, userId: number, role: string) {
        return this.http.put(
            `${this.baseUrl}/${id}/approve?userId=${userId}&role=${role}`,
            {},
            this.getAuthHeaders()
        );
    }

    reject(id: number, reason: string, userId: number, role: string) {
        return this.http.post(
            `${this.baseUrl}/${id}/reject?userId=${userId}&role=${role}&reason=${encodeURIComponent(reason)}`,
            {},
            this.getAuthHeaders()
        );
    }



    importProduction(payload: any): Observable<ProductionImportResponse> {
        return this.http.post<ProductionImportResponse>(
            `${this.baseUrl}/import`,
            payload,
            this.getAuthHeaders()   // 🔥 REQUIRED (JWT)
        );
    }


}
