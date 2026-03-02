import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Product } from '../models/product.model';
import { APP_CONFIG } from '../config/config';

// ================= BASE URL =================
const PRODUCT_API_URL = `${APP_CONFIG.BASE_URL}${APP_CONFIG.API.PRODUCTS}`;

@Injectable({
    providedIn: 'root'
})
export class ProductService {

    constructor(private http: HttpClient) { }

    // ================= AUTH HEADERS =================
    private authHeaders() {
        return {
            headers: new HttpHeaders({
                Authorization: `Bearer ${localStorage.getItem('token')}`
            })
        };
    }

    // ================= CREATE =================
    create(product: Product, userId: number): Observable<Product> {
        return this.http.post<Product>(
            `${PRODUCT_API_URL}/${userId}`,
            product,
            this.authHeaders()
        );
    }

    // ================= READ ALL =================
    getAll(): Observable<Product[]> {
        return this.http.get<Product[]>(
            PRODUCT_API_URL,
            this.authHeaders()
        );
    }

    // ================= READ BY ID =================
    getById(id: number): Observable<Product> {
        return this.http.get<Product>(
            `${PRODUCT_API_URL}/${id}`,
            this.authHeaders()
        );
    }

    // ================= UPDATE =================
    update(id: number, product: Product): Observable<Product> {
        return this.http.put<Product>(
            `${PRODUCT_API_URL}/${id}`,
            product,
            this.authHeaders()
        );
    }

    // ================= DELETE =================
    delete(id: number): Observable<any> {
        return this.http.delete(
            `${PRODUCT_API_URL}/${id}`,
            this.authHeaders()
        );
    }

    // ================= CREATE (LOGGED-IN USER) =================
    createForLoggedInUser(product: Product): Observable<Product> {
        return this.http.post<Product>(
            `${PRODUCT_API_URL}/addProduct`,
            product,
            this.authHeaders()
        );
    }
}
