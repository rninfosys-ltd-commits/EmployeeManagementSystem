import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { APP_CONFIG } from '../config/config';
// import { APP_CONFIG } from 'src/app/core/config/config';

@Injectable({
    providedIn: 'root'
})
export class ProductionDashboardService {

    private BASE_URL = APP_CONFIG.BASE_URL;

    constructor(private http: HttpClient) { }

    // ✅ PRODUCTION
    getProduction(): Observable<any[]> {
        return this.http.get<any[]>(
            this.BASE_URL + APP_CONFIG.API.PRODUCTION
        );
    }

    // ✅ CASTING
    getCasting(): Observable<any[]> {
        return this.http.get<any[]>(
            this.BASE_URL + APP_CONFIG.API.CASTING_REPORT
        );
    }

    // ✅ CUTTING
    getCutting(): Observable<any[]> {
        return this.http.get<any[]>(
            this.BASE_URL + APP_CONFIG.API.WIRE_CUTTING
        );
    }

    // ✅ AUTOCLAVE
    getAutoclave(): Observable<any[]> {
        return this.http.get<any[]>(
            this.BASE_URL + APP_CONFIG.API.AUTOCLAVE
        );
    }

    // ✅ BLOCK SEPARATING
    getBlock(): Observable<any[]> {
        return this.http.get<any[]>(
            this.BASE_URL + APP_CONFIG.API.BLOCK_SEPARATING
        );
    }

    // ✅ CUBE TEST
    getCube(): Observable<any[]> {
        return this.http.get<any[]>(
            this.BASE_URL + APP_CONFIG.API.CUBE_TEST
        );
    }

    // ✅ REJECTION
    getRejection(): Observable<any[]> {
        return this.http.get<any[]>(
            this.BASE_URL + APP_CONFIG.API.REJECTION
        );
    }

}