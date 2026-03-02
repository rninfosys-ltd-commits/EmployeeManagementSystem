import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Observable } from 'rxjs';
import { APP_CONFIG } from '../config/config';

@Injectable({
    providedIn: 'root'
})
export class LocationService {

    // ================= BASE URL =================
    private baseUrl = `${APP_CONFIG.BASE_URL}${APP_CONFIG.API.LOCATION}`;

    constructor(private http: HttpClient) { }

    // ================= HEADERS =================

    private authHeaders() {
        return {
            headers: new HttpHeaders({
                Authorization: `Bearer ${localStorage.getItem('token')}`
            })
        };
    }

    private publicHeaders() {
        return {
            headers: new HttpHeaders({})
        };
    }

    // =================================================
    // =============== PUBLIC GET (NO TOKEN) ============
    // =================================================

    getStates(): Observable<any[]> {
        return this.http.get<any[]>(
            `${this.baseUrl}/states`,
            this.publicHeaders()
        );
    }

    getDistricts(stateId: number): Observable<any[]> {
        return this.http.get<any[]>(
            `${this.baseUrl}/districts/${stateId}`,
            this.publicHeaders()
        );
    }

    getTalukas(districtId: number): Observable<any[]> {
        return this.http.get<any[]>(
            `${this.baseUrl}/talukas/${districtId}`,
            this.publicHeaders()
        );
    }

    getCities(talukaId: number): Observable<any[]> {
        return this.http.get<any[]>(
            `${this.baseUrl}/cities/${talukaId}`,
            this.publicHeaders()
        );
    }

    // =================================================
    // =============== MASTER GET (TOKEN) ===============
    // =================================================

    getAllStates(): Observable<any[]> {
        return this.http.get<any[]>(
            `${this.baseUrl}/states`,
            this.authHeaders()
        );
    }

    getAllDistricts(): Observable<any[]> {
        return this.http.get<any[]>(
            `${this.baseUrl}/districts`,
            this.authHeaders()
        );
    }

    getAllTalukas(): Observable<any[]> {
        return this.http.get<any[]>(
            `${this.baseUrl}/talukas`,
            this.authHeaders()
        );
    }

    getCitiesByTaluka(talukaId: number): Observable<any[]> {
        return this.http.get<any[]>(
            `${this.baseUrl}/cities/by-taluka/${talukaId}`,
            this.publicHeaders()
        );
    }


    // =================================================
    // ================== POST / SAVE ==================
    // =================================================

    addState(data: any): Observable<any> {
        return this.http.post(
            `${this.baseUrl}/states`,
            data,
            this.authHeaders()
        );
    }

    // ✅ MUST SEND stateId
    addDistrict(data: any, stateId: number): Observable<any> {
        return this.http.post(
            `${this.baseUrl}/districts/${stateId}`,
            data,
            this.authHeaders()
        );
    }

    // ✅ MUST SEND districtId
    addTaluka(data: any, districtId: number): Observable<any> {
        return this.http.post(
            `${this.baseUrl}/talukas/${districtId}`,
            data,
            this.authHeaders()
        );
    }

    // ✅ MUST SEND talukaId
    addCity(data: any, talukaId: number): Observable<any> {
        return this.http.post(
            `${this.baseUrl}/cities/${talukaId}`,
            data,
            this.authHeaders()
        );
    }
}
