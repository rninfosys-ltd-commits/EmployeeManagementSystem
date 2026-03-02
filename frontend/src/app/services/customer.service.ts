import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { CustomerTrn } from '../models/customer.model';
import { APP_CONFIG } from '../config/config';

// ================= BASE URLs =================
const CUSTOMER_API_URL = `${APP_CONFIG.BASE_URL}${APP_CONFIG.API.CUSTOMER_TRN}`;
const BATCH_API_URL = `${APP_CONFIG.BASE_URL}${APP_CONFIG.API.BATCH}`;

@Injectable({
    providedIn: 'root'
})
export class CustomerService {

    constructor(private http: HttpClient) { }

    // =========================================
    // ============== CUSTOMER CRUD ============
    // =========================================

    getAllCustomers(): Observable<CustomerTrn[]> {
        return this.http.get<CustomerTrn[]>(CUSTOMER_API_URL);
    }

    getCustomerById(id: number): Observable<CustomerTrn> {
        return this.http.get<CustomerTrn>(`${CUSTOMER_API_URL}/${id}`);
    }

    createCustomer(customer: CustomerTrn): Observable<CustomerTrn> {
        return this.http.post<CustomerTrn>(CUSTOMER_API_URL, customer);
    }

    updateCustomer(id: number, customer: CustomerTrn): Observable<CustomerTrn> {
        return this.http.put<CustomerTrn>(`${CUSTOMER_API_URL}/${id}`, customer);
    }

    deleteCustomer(id: number): Observable<void> {
        return this.http.delete<void>(`${CUSTOMER_API_URL}/${id}`);
    }

    // =========================================
    // ============== BATCH APIs ===============
    // =========================================

    getAllBatches(): Observable<any[]> {
        return this.http.get<any[]>(BATCH_API_URL);
    }

    createBatch(batch: any): Observable<any> {
        return this.http.post<any>(BATCH_API_URL, batch);
    }

    createTransactionWithBatch(
        bactno: number,
        customer: CustomerTrn
    ): Observable<CustomerTrn> {
        return this.http.post<CustomerTrn>(
            `${CUSTOMER_API_URL}/${bactno}`,
            customer
        );
    }

    getCustomersByBatch(bactno: number): Observable<CustomerTrn[]> {
        return this.http.get<CustomerTrn[]>(
            `${CUSTOMER_API_URL}/batch/${bactno}`
        );
    }

    approveCustomer(id: number): Observable<CustomerTrn> {
        return this.http.post<CustomerTrn>(
            `${CUSTOMER_API_URL}/${id}/approve`,
            {}
        );
    }

    // =========================================
    // ============ BATCH ACTIONS ===============
    // =========================================

    approveBatch(bactno: number): Observable<any> {
        return this.http.post<any>(
            `${BATCH_API_URL}/${bactno}/approve`,
            {}
        );
    }

    rejectBatch(bactno: number, reason?: string): Observable<any> {
        const url =
            `${BATCH_API_URL}/${bactno}/reject` +
            (reason ? `?reason=${encodeURIComponent(reason)}` : '');

        return this.http.post<any>(url, {});
    }

    updateBatch(bactno: number, payload: any): Observable<any> {
        return this.http.put<any>(
            `${BATCH_API_URL}/${bactno}`,
            payload
        );
    }

    deleteBatch(bactno: number): Observable<void> {
        return this.http.delete<void>(
            `${BATCH_API_URL}/${bactno}`
        );
    }

    // =========================================
    // ============== DOWNLOAD =================
    // =========================================

    // downloadBatch(bactno: number) {
    //     return this.http.get(
    //         `${BATCH_API_URL}/${bactno}/download`,
    //         { responseType: 'blob' }
    //     );
    // }
}
