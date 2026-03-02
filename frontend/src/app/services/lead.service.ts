import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Lead } from '../models/lead.model';
import { APP_CONFIG } from '../config/config';

@Injectable({ providedIn: 'root' })
export class LeadService {

    private API = APP_CONFIG.BASE_URL + APP_CONFIG.API.LEADS;

    constructor(private http: HttpClient) { }

    private authHeaders() {
        return {
            headers: new HttpHeaders({
                Authorization: `Bearer ${localStorage.getItem('token')}`,
                'Content-Type': 'application/json'
            })
        };
    }

    create(lead: Lead): Observable<Lead> {
        return this.http.post<Lead>(this.API, lead, this.authHeaders());
    }

    createFromExcel(customerName: string): Observable<Lead> {
        return this.http.post<Lead>(
            this.API,
            {
                customerName: customerName,   // 🔥 IMPORTANT
                contactNo: 0,
                budget: 0,
                stateId: 0,
                cityId: 0,
                isActive: 1
            },
            this.authHeaders()
        );
    }


    getAll(): Observable<Lead[]> {
        return this.http.get<Lead[]>(this.API, this.authHeaders());
    }

    getById(id: number): Observable<Lead> {
        return this.http.get<Lead>(`${this.API}/${id}`, this.authHeaders());
    }

    update(id: number, lead: Lead): Observable<Lead> {
        return this.http.put<Lead>(`${this.API}/${id}`, lead, this.authHeaders());
    }

    delete(id: number): Observable<void> {
        return this.http.delete<void>(`${this.API}/${id}`, this.authHeaders());
    }
    importLeads(payload: { leads: any[] }): Observable<any> {
        return this.http.post(`${this.API}/import`, payload, this.authHeaders());
    }

    createFromInquiry(payload: {
        customerName: string;
        contactNo: number;
        panNo?: string | null;
        stateId?: number | null;
        cityId?: number | null;
        budget?: number | null;
    }) {
        return this.http.post(
            `${this.API}/from-inquiry`,
            payload,
            this.authHeaders()
        );
    }

}
