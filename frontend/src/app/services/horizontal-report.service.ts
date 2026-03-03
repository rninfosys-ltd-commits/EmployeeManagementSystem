import { Injectable } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';
import { APP_CONFIG } from '../config/config';

export interface HorizontalReportRow {
    batchNo: string;
    production: { [key: string]: string };
    casting: { [key: string]: string };
    cutting: { [key: string]: string };
    autoclave: { [key: string]: string };
    blockSeparating: { [key: string]: string };
    cubeTest: { [key: string]: string };
    rejection: { [key: string]: string };
}

@Injectable({ providedIn: 'root' })
export class HorizontalReportService {

    private baseUrl = `${APP_CONFIG.BASE_URL}/api/workflow/report/horizontal`;

    constructor(private http: HttpClient) { }

    getReport(
        fromDate?: string,
        toDate?: string,
        batchNo?: string,
        plantName?: string
    ): Observable<HorizontalReportRow[]> {
        let params = new HttpParams();
        if (fromDate) params = params.set('fromDate', fromDate);
        if (toDate) params = params.set('toDate', toDate);
        if (batchNo) params = params.set('batchNo', batchNo);
        if (plantName) params = params.set('plantName', plantName);
        return this.http.get<HorizontalReportRow[]>(this.baseUrl, { params });
    }

    /** Download combined horizontal Excel for one batch or a date range */
    downloadExcel(
        fromDate?: string,
        toDate?: string,
        batchNo?: string,
        stage?: string,
        plantName?: string
    ): Observable<Blob> {
        let params = new HttpParams();
        if (fromDate) params = params.set('fromDate', fromDate);
        if (toDate) params = params.set('toDate', toDate);
        if (batchNo) params = params.set('batchNo', batchNo);
        if (stage) params = params.set('upToStage', stage);
        if (plantName) params = params.set('plantName', plantName);
        params = params.set('format', 'excel');
        return this.http.get(`${this.baseUrl}/download`, { params, responseType: 'blob' });
    }

    /** Download single-row batch lifecycle Excel report */
    downloadLifecycleExcel(batchNo: string, stage?: string): Observable<Blob> {
        let params = new HttpParams();
        if (stage) params = params.set('upToStage', stage);
        return this.http.get(`${this.baseUrl}/lifecycle/download/${batchNo}`, { params, responseType: 'blob' });
    }
}

