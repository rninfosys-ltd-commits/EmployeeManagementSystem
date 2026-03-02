import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Observable } from 'rxjs';
import { TokenStorageService } from './token-storage.service';
import { APP_CONFIG } from '../config/config';

// ================= BASE URLs =================
const ADMIN_API = `${APP_CONFIG.BASE_URL}${APP_CONFIG.API.ADMIN}`;
const PRODUCT_API = `${APP_CONFIG.BASE_URL}${APP_CONFIG.API.PRODUCTS}`;
const USER_API = `${APP_CONFIG.BASE_URL}${APP_CONFIG.API.USERS}`;

export interface CartItem {
    productId: number;
    productName: string;
    price: number;
    quantity: number;
    isEditing?: boolean;
    tempQty?: number;
    tempPrice?: number;
}

export interface PlaceSellOrderDto {
    id?: number;
    userId: number;
    customerId?: number;
    userName?: string;
    orderDescription?: string;
    date?: string;
    amount?: number;
    cartItems?: CartItem[];
}

@Injectable({
    providedIn: 'root'
})
export class SellService {

    constructor(
        private http: HttpClient,
        private tokenStorage: TokenStorageService
    ) { }

    // ================= AUTH HEADERS =================
    private headers() {
        return {
            headers: new HttpHeaders({
                Authorization: `Bearer ${this.tokenStorage.getToken()}`
            })
        };
    }

    // ================= CUSTOMERS =================
    getCustomers(): Observable<any[]> {
        return this.http.get<any[]>(
            `${USER_API}/parties`,
            this.headers()
        );
    }

    // ================= PRODUCTS =================
    getAllProducts(): Observable<any[]> {
        return this.http.get<any[]>(
            PRODUCT_API,
            this.headers()
        );
    }

    // ================= SELL ORDER =================
    placeOrder(dto: PlaceSellOrderDto) {
        return this.http.post(
            `${ADMIN_API}/sell-order/place`,
            dto,
            this.headers()
        );
    }

    getSellOrders(): Observable<PlaceSellOrderDto[]> {
        return this.http.get<PlaceSellOrderDto[]>(
            `${ADMIN_API}/sell-orders`,
            this.headers()
        );
    }

    getSellOrderDetails(id: number): Observable<PlaceSellOrderDto> {
        return this.http.get<PlaceSellOrderDto>(
            `${ADMIN_API}/sell-order/details/${id}`,
            this.headers()
        );
    }

    exportTransactions(type: string, format: string, from: string, to: string, party: string, min: any, max: any): Observable<Blob> {
        let params = new URLSearchParams();
        params.append('type', type);
        params.append('format', format);
        if (from) params.append('fromDate', from);
        if (to) params.append('toDate', to);
        if (party) params.append('partyName', party);
        if (min !== null && min !== '') params.append('minAmount', min);
        if (max !== null && max !== '') params.append('maxAmount', max);

        const url = `${APP_CONFIG.BASE_URL}/api/transactions/export?${params.toString()}`;
        return this.http.get(url, {
            ...this.headers(),
            responseType: 'blob'
        });
    }
}
