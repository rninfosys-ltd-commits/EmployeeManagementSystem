import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Observable } from 'rxjs';
import { APP_CONFIG } from '../config/config';
// import { APP_CONFIG } from '../core/config/config';

@Injectable({ providedIn: 'root' })
export class ProjectService {

    private API_URL = APP_CONFIG.BASE_URL + APP_CONFIG.API.PROJECTS;

    constructor(private http: HttpClient) { }

    private authHeaders() {
        return {
            headers: new HttpHeaders({
                Authorization: `Bearer ${localStorage.getItem('token')}`
            })
        };
    }

    create(project: any): Observable<any> {
        return this.http.post(this.API_URL, project, this.authHeaders());
    }

    getAll(): Observable<any[]> {
        return this.http.get<any[]>(this.API_URL, this.authHeaders());
    }

    getById(id: number): Observable<any> {
        return this.http.get(`${this.API_URL}/${id}`, this.authHeaders());
    }

    update(id: number, project: any): Observable<any> {
        return this.http.put(`${this.API_URL}/${id}`, project, this.authHeaders());
    }

    delete(id: number): Observable<any> {
        return this.http.delete(`${this.API_URL}/${id}`, this.authHeaders());
    }
    importProjects(payload: any) {
        return this.http.post(
            `${this.API_URL}/import`,
            payload,
            {
                headers: new HttpHeaders({
                    Authorization: `Bearer ${localStorage.getItem('token')}`,
                    'Content-Type': 'application/json'
                })
            }
        );
    }
    createFromExcel(projectName: string): Observable<any> {
        return this.http.post(this.API_URL, {
            projectName: projectName,
            isActive: 1
        }, this.authHeaders());
    }

    createFromInquiry(payload: {
        projectName: string;
        budgetAmt?: number;
        panNo?: string | null;
        stateId?: number | null;
        cityId?: number | null;
        budget?: number | null;
    }) {
        return this.http.post(
            `${this.API_URL}/from-inquiry`,
            payload,
            this.authHeaders()
        );
    }


}
