import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Inquiry } from '../models/inquiry.model';
import { APP_CONFIG } from '../config/config';

@Injectable({ providedIn: 'root' })
export class InquiryService {

    private API = APP_CONFIG.BASE_URL + APP_CONFIG.API.INQUIRIES;

    constructor(private http: HttpClient) { }

    private authHeaders() {
        return {
            headers: new HttpHeaders({
                Authorization: `Bearer ${localStorage.getItem('token')}`,
                'Content-Type': 'application/json'
            })
        };
    }

    create(data: Inquiry): Observable<Inquiry> {
        return this.http.post<Inquiry>(this.API, data, this.authHeaders());
    }

    getAll(): Observable<Inquiry[]> {
        return this.http.get<Inquiry[]>(this.API, this.authHeaders());
    }

    update(id: number, data: Inquiry): Observable<Inquiry> {
        return this.http.put<Inquiry>(`${this.API}/${id}`, data, this.authHeaders());
    }
}
