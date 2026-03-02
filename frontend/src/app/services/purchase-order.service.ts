import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Observable } from 'rxjs';
import { TokenStorageService } from './token-storage.service';
import { APP_CONFIG } from '../config/config';

/* ================= BASE URLs ================= */
const ADMIN_API = `${APP_CONFIG.BASE_URL}${APP_CONFIG.API.ADMIN}`;
const PRODUCT_API = `${APP_CONFIG.BASE_URL}${APP_CONFIG.API.PRODUCTS}`;

/* ================= MODELS ================= */

export interface CartItem {
    id?: number;
    productId: number;
    productName: string;
    price: number;
    quantity: number;
    productImg?: string;
    totalAmount?: number;

    // UI helpers
    isEditing?: boolean;
    tempQty?: number;
    tempPrice?: number;
}

export interface PlacePurchaseOrderDto {
    id?: number;
    userId: number;
    customerId?: number;
    userName?: string;
    trackingId?: string;
    amount?: number;
    orderDescription?: string;
    address?: string;
    email?: string;
    mobile?: string;
    pincode?: string;
    orderStatus?: string;
    date?: string;
    cartItems?: CartItem[];
}

@Injectable({
    providedIn: 'root'
})
export class PurchaseService {

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

    // ================= PRODUCTS =================
    getAllProductsByUserId(userId: number): Observable<any[]> {
        return this.http.get<any[]>(
            `${PRODUCT_API}/user/${userId}`,
            this.headers()
        );
    }

    getAllProducts(): Observable<any[]> {
        return this.http.get<any[]>(
            PRODUCT_API,
            this.headers()
        );
    }

    // ================= CART =================
    saveCartItem(item: CartItem): Observable<any> {
        return this.http.post<any>(
            `${ADMIN_API}/purchase-cart`,
            item,
            this.headers()
        );
    }

    deleteCartItem(id: number): Observable<any> {
        return this.http.delete<any>(
            `${ADMIN_API}/purchase-cart/${id}`,
            this.headers()
        );
    }

    // ================= ORDER =================
    placeOrder(dto: PlacePurchaseOrderDto) {
        return this.http.post(
            `${ADMIN_API}/purchase-order/place`,
            dto,
            this.headers()
        );

    }

    getPurchaseOrders(): Observable<PlacePurchaseOrderDto[]> {
        return this.http.get<PlacePurchaseOrderDto[]>(
            `${ADMIN_API}/purchase-orders`,
            this.headers()
        );
    }

    getPurchaseOrderDetails(id: number): Observable<PlacePurchaseOrderDto> {
        return this.http.get<PlacePurchaseOrderDto>(
            `${ADMIN_API}/purchase-order/details/${id}`,
            this.headers()
        );
    }

    changeOrderStatus(id: number, status: string) {
        return this.http.put(
            `${ADMIN_API}/purchase-order/status/${id}?status=${status}`,
            {},
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
