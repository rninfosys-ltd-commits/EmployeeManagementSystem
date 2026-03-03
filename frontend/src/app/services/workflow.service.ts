import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { APP_CONFIG } from '../config/config';

@Injectable({
    providedIn: 'root'
})
export class WorkflowService {

    private baseUrl = APP_CONFIG.BASE_URL + APP_CONFIG.API.WORKFLOW;

    constructor(private http: HttpClient) { }

    /**
     * Get workflow status for a batch
     * Returns: { PRODUCTION: true, CASTING: true, CUTTING: false, ... }
     */
    getWorkflowStatus(batchNo: string): Observable<{ [key: string]: boolean }> {
        return this.http.get<{ [key: string]: boolean }>(`${this.baseUrl}/status/${batchNo}`);
    }

    /**
     * Check if a batch can proceed to a target stage
     */
    canProceed(batchNo: string, targetStage: string): Observable<any> {
        return this.http.get<any>(`${this.baseUrl}/can-proceed/${batchNo}/${targetStage}`);
    }

    /**
     * Get only incomplete (in-progress) batch numbers
     */
    getIncompleteBatches(): Observable<string[]> {
        return this.http.get<string[]>(`${this.baseUrl}/batches/incomplete`);
    }

    /**
     * Get all batch statuses for dashboard
     */
    getAllBatchStatus(): Observable<any[]> {
        return this.http.get<any[]>(`${this.baseUrl}/batches/all-status`);
    }

    /**
     * Download combined PDF or Excel report for a batch up to a stage
     */
    downloadReport(batchNo: string, upToStage: string, format: string = 'pdf', plantName?: string): Observable<Blob> {
        let reportsUrl = APP_CONFIG.BASE_URL + '/api/reports';
        let params: any = { format };
        if (plantName) params.plantName = plantName;
        return this.http.get(`${reportsUrl}/${batchNo}/${upToStage}`, {
            params,
            responseType: 'blob'
        });
    }

    /**
     * Export production report for a stage and date range
     */
    exportReport(stage: string, fromDate: string, toDate: string, format: string, plantName?: string): Observable<Blob> {
        let exportUrl = APP_CONFIG.BASE_URL + `/api/reports/${stage}/export`;
        let params: any = { fromDate, toDate, format };
        if (plantName) params.plantName = plantName;
        return this.http.get(exportUrl, {
            params,
            responseType: 'blob'
        });
    }
}
