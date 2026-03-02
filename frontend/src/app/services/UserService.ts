import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Observable } from 'rxjs';
import { APP_CONFIG } from '../config/config';

// ================= BASE URL =================
const USER_API_URL = `${APP_CONFIG.BASE_URL}${APP_CONFIG.API.USERS}`;

@Injectable({
    providedIn: 'root'
})
export class UserService {

    constructor(private http: HttpClient) { }

    // ================= LOCAL STORAGE HELPERS =================

    static saveUser(user: any) {
        localStorage.setItem('user', JSON.stringify(user));
    }

    static getUser() {
        const user = localStorage.getItem('user');
        return user ? JSON.parse(user) : null;
    }

    static clearUser() {
        localStorage.removeItem('user');
    }

    // ================= AUTH HEADERS =================
    private authHeaders() {
        return {
            headers: new HttpHeaders({
                Authorization: `Bearer ${localStorage.getItem('token')}`
            })
        };
    }

    // ================= USER CRUD =================

    getAllUsers(): Observable<any[]> {
        return this.http.get<any[]>(
            USER_API_URL,
            this.authHeaders()
        );
    }

    getUser(id: number): Observable<any> {
        return this.http.get<any>(
            `${USER_API_URL}/${id}`,
            this.authHeaders()
        );
    }

    createUser(user: any) {
        return this.http.post(
            USER_API_URL,
            user,
            this.authHeaders()
        );
    }

    updateUser(id: number, user: any) {
        return this.http.put(
            `${USER_API_URL}/${id}`,
            user,
            this.authHeaders()
        );
    }

    deleteUser(id: number) {
        return this.http.delete(
            `${USER_API_URL}/${id}`,
            this.authHeaders()
        );
    }

    activateUser(id: number) {
        return this.http.put(
            `${USER_API_URL}/${id}/activate`,
            {},
            this.authHeaders()
        );
    }

    deactivateUser(id: number) {
        return this.http.put(
            `${USER_API_URL}/${id}/deactivate`,
            {},
            this.authHeaders()
        );
    }

    // ================= SUPPLIERS =================
    getSuppliers(): Observable<any[]> {
        return this.http.get<any[]>(
            `${USER_API_URL}/suppliers`,
            this.authHeaders()
        );
    }
}
