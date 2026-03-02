import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { KmBatch } from '../models/km-batch.model';
import { KmEntry } from '../models/km-entry.model';
import { APP_CONFIG } from '../config/config';

// ================= BASE URLs =================
const KM_BATCH_API_URL = `${APP_CONFIG.BASE_URL}${APP_CONFIG.API.KM_BATCH}`;
const KM_ENTRY_API_URL = `${APP_CONFIG.BASE_URL}${APP_CONFIG.API.KM_ENTRY}`;

@Injectable({
    providedIn: 'root'
})
export class KmService {

    constructor(private http: HttpClient) { }

    // =========================================
    // ============== BATCH APIs ===============
    // =========================================

    getAllBatches(): Observable<KmBatch[]> {
        return this.http.get<KmBatch[]>(KM_BATCH_API_URL);
    }

    createBatch(batch: any): Observable<KmBatch> {
        return this.http.post<KmBatch>(KM_BATCH_API_URL, batch);
    }

    approveBatch(kmBatchNo: number): Observable<KmBatch> {
        return this.http.post<KmBatch>(
            `${KM_BATCH_API_URL}/${kmBatchNo}/approve`,
            {}
        );
    }

    rejectBatch(kmBatchNo: number, reason?: string): Observable<KmBatch> {
        const url =
            `${KM_BATCH_API_URL}/${kmBatchNo}/reject` +
            (reason ? `?reason=${encodeURIComponent(reason)}` : '');

        return this.http.post<KmBatch>(url, {});
    }

    deleteBatch(kmBatchNo: number): Observable<void> {
        return this.http.delete<void>(
            `${KM_BATCH_API_URL}/${kmBatchNo}`
        );
    }

    // downloadBatch(kmBatchNo: number) {
    //     return this.http.get(
    //         `${KM_BATCH_API_URL}/${kmBatchNo}/download`,
    //         { responseType: 'blob' }
    //     );
    // }

    // =========================================
    // ============== ENTRY APIs ===============
    // =========================================

    createEntryWithBatch(
        batchNo: number,
        entry: KmEntry
    ): Observable<KmEntry> {
        return this.http.post<KmEntry>(
            `${KM_ENTRY_API_URL}/${batchNo}`,
            entry
        );
    }

    getEntriesByBatch(batchNo: number): Observable<KmEntry[]> {
        return this.http.get<KmEntry[]>(
            `${KM_ENTRY_API_URL}/batch/${batchNo}`
        );
    }

    updateEntry(id: number, payload: KmEntry): Observable<KmEntry> {
        return this.http.put<KmEntry>(
            `${KM_ENTRY_API_URL}/${id}`,
            payload
        );
    }

    deleteEntry(id: number): Observable<void> {
        return this.http.delete<void>(
            `${KM_ENTRY_API_URL}/${id}`
        );
    }
}
