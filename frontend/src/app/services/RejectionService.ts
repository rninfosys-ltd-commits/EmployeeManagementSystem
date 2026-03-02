import { HttpClient, HttpHeaders } from "@angular/common/http";
import { Injectable } from "@angular/core";
import { APP_CONFIG } from "../config/config"
@Injectable({ providedIn: 'root' })
export class RejectionService {

    private API = APP_CONFIG.BASE_URL + '/api/rejection';

    constructor(private http: HttpClient) { }

    private headers() {
        return {
            headers: new HttpHeaders({
                Authorization: `Bearer ${localStorage.getItem('token')}`,
                'Content-Type': 'application/json'
            })
        };
    }

    create(p: any) {
        return this.http.post(this.API, p, this.headers());
    }

    getAll() {
        return this.http.get<any[]>(this.API, this.headers());
    }

    update(id: number, p: any) {
        return this.http.put(`${this.API}/${id}`, p, this.headers());
    }

    delete(id: number) {
        return this.http.delete(`${this.API}/${id}`, this.headers());
    }
}
