import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Observable } from 'rxjs';
import { APP_CONFIG } from '../config/config';

@Injectable({ providedIn: 'root' })
export class WireCuttingReportService {

    private baseUrl = `${APP_CONFIG.BASE_URL}${APP_CONFIG.API.WIRE_CUTTING}`;

    constructor(private http: HttpClient) { }

    private getAuthHeaders() {
        const token = localStorage.getItem('token');
        return {
            headers: new HttpHeaders({
                Authorization: token ? `Bearer ${token}` : ''
            })
        };
    }

    getAll(): Observable<any[]> {
        return this.http.get<any[]>(this.baseUrl, this.getAuthHeaders());
    }

    save(data: any) {
        return this.http.post(this.baseUrl, data, this.getAuthHeaders());
    }

    update(id: number, data: any) {
        return this.http.put(`${this.baseUrl}/${id}`, data, this.getAuthHeaders());
    }

    delete(id: number) {
        return this.http.delete(`${this.baseUrl}/${id}`, this.getAuthHeaders());
    }

    approve(id: number) {

        const userId = Number(localStorage.getItem('userId'));
        const role = localStorage.getItem('role');
        const username = localStorage.getItem('username');

        return this.http.put(
            `${this.baseUrl}/${id}/approve?userId=${userId}&role=${role}&username=${username}`,
            {},
            this.getAuthHeaders()
        );
    }


    reject(id: number, reason: string) {

        const userId = Number(localStorage.getItem('userId'));
        const role = localStorage.getItem('role');
        const username = localStorage.getItem('username');

        return this.http.post(
            `${this.baseUrl}/${id}/reject?userId=${userId}&role=${role}&username=${username}`,
            { reason },
            this.getAuthHeaders()
        );
    }


    importWireCutting(payload: any) {
        return this.http.post(
            `${this.baseUrl}/import`,
            payload,
            this.getAuthHeaders()
        );
    }

}
