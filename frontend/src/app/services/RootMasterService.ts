import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Observable } from 'rxjs';
import { APP_CONFIG } from '../config/config';

@Injectable({
    providedIn: 'root'
})
export class RootMasterService {

    private baseUrl = `${APP_CONFIG.BASE_URL}${APP_CONFIG.API.ROOTS}`;

    constructor(private http: HttpClient) { }

    // ================= AUTH HEADERS =================
    private authHeaders() {
        return {
            headers: new HttpHeaders({
                Authorization: `Bearer ${localStorage.getItem('token')}`
            })
        };
    }

    // ================= GET ALL =================
    getAll(): Observable<any[]> {
        return this.http.get<any[]>(
            this.baseUrl,
            this.authHeaders()
        );
    }

    // ================= SAVE / UPDATE =================
    save(payload: any): Observable<any> {
        return this.http.post(
            this.baseUrl,
            payload,
            this.authHeaders()
        );
    }

    // ================= DELETE =================
    delete(id: number): Observable<any> {
        return this.http.delete(
            `${this.baseUrl}/${id}`,
            this.authHeaders()
        );
    }
}
