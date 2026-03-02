import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Observable } from 'rxjs';
import { APP_CONFIG } from '../config/config';

@Injectable({
    providedIn: 'root'
})
export class PriceMasterService {

    private baseUrl = APP_CONFIG.BASE_URL;

    constructor(private http: HttpClient) { }

    // ================= AUTH HEADERS =================
    private authHeaders() {
        return {
            headers: new HttpHeaders({
                Authorization: `Bearer ${localStorage.getItem('token')}`
            })
        };
    }

    // ================= PARTY LIST =================
    getParties(): Observable<any[]> {
        return this.http.get<any[]>(
            `${this.baseUrl}${APP_CONFIG.API.USERS}/parties`,
            this.authHeaders()
        );
    }

    // ================= PRODUCT LIST =================
    getProducts(): Observable<any[]> {
        return this.http.get<any[]>(
            `${this.baseUrl}${APP_CONFIG.API.PRODUCTS}`,
            this.authHeaders()
        );
    }

    // ================= SAVE PARTY PRICE =================
    savePartyPrice(payload: any): Observable<any> {
        return this.http.post(
            `${this.baseUrl}${APP_CONFIG.API.PARTY_PRICES}`,
            payload,
            this.authHeaders()
        );
    }

    // ================= GET ALL PARTY PRICES =================
    getAllPartyPrices(): Observable<any[]> {
        return this.http.get<any[]>(
            `${this.baseUrl}${APP_CONFIG.API.PARTY_PRICES}`,
            this.authHeaders()
        );
    }

    // ================= DELETE PARTY PRICE =================
    deletePartyPrice(id: number): Observable<any> {
        return this.http.delete(
            `${this.baseUrl}${APP_CONFIG.API.PARTY_PRICES}/${id}`,
            this.authHeaders()
        );
    }
}
