import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Observable } from 'rxjs';
import { APP_CONFIG } from '../config/config';

@Injectable({ providedIn: 'root' })
export class CubeTestService {

    private API = APP_CONFIG.BASE_URL + '/api/cube-test';

    constructor(private http: HttpClient) { }

    private authHeaders() {
        return {
            headers: new HttpHeaders({
                Authorization: `Bearer ${localStorage.getItem('token')}`,
                'Content-Type': 'application/json'
            })
        };
    }

    // ================= CREATE =================

    create(payload: any): Observable<any> {
        return this.http.post(this.API, payload, this.authHeaders());
    }

    // ================= GET ALL =================

    getAll(): Observable<any[]> {
        return this.http.get<any[]>(this.API, this.authHeaders());
    }

    // ================= GET BY ID =================

    getById(id: number): Observable<any> {
        return this.http.get(`${this.API}/${id}`, this.authHeaders());
    }

    // ================= UPDATE =================

    update(id: number, payload: any): Observable<any> {
        return this.http.put(`${this.API}/${id}`, payload, this.authHeaders());
    }

    // ================= DELETE =================

    delete(id: number): Observable<void> {
        return this.http.delete<void>(`${this.API}/${id}`, this.authHeaders());
    }

    approve(id: number, userId: any, role: string) {
        return this.http.post(`${this.API}/${id}/approve?userId=${userId}&role=${role}`, {}, this.authHeaders());
    }

    reject(id: number, reason: string, userId: any, role: string) {
        return this.http.post(`${this.API}/${id}/reject?userId=${userId}&role=${role}`, { reason }, this.authHeaders());
    }


}
