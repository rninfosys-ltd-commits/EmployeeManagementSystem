import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { APP_CONFIG } from '../config/config';

@Injectable({
    providedIn: 'root'
})
export class BlockSeparatingService {


    private baseUrl =
        APP_CONFIG.BASE_URL + APP_CONFIG.API.BLOCK_SEPARATING;

    constructor(private http: HttpClient) { }

    create(data: any): Observable<any> {
        return this.http.post(this.baseUrl, data);
    }

    getAll(): Observable<any[]> {
        return this.http.get<any[]>(this.baseUrl);
    }

    update(id: number, data: any): Observable<any> {
        return this.http.put(`${this.baseUrl}/${id}`, data);
    }

    delete(id: number): Observable<any> {
        return this.http.delete(`${this.baseUrl}/${id}`);
    }

    getCuttingBatches(): Observable<any[]> {
        return this.http.get<any[]>(
            APP_CONFIG.BASE_URL + APP_CONFIG.API.WIRE_CUTTING
        );
    }


}
