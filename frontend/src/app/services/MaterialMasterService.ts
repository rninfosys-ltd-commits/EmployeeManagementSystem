import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Observable } from 'rxjs';
import { APP_CONFIG } from '../config/config';

export interface MaterialMaster {
    id: number;
    materialName: string;
    unit: string;
    displayOrder: number;
    isActive: number;
    orgId: number;
    branchId: number;
}

@Injectable({
    providedIn: 'root'
})
export class MaterialMasterService {

    private baseUrl = `${APP_CONFIG.BASE_URL}${APP_CONFIG.API.MATERIAL_MASTER}`;

    constructor(private http: HttpClient) { }

    private getAuthHeaders() {
        const token = localStorage.getItem('token');
        return {
            headers: new HttpHeaders({
                Authorization: token ? `Bearer ${token}` : ''
            })
        };
    }

    getAll(): Observable<MaterialMaster[]> {
        return this.http.get<MaterialMaster[]>(
            this.baseUrl,
            this.getAuthHeaders()
        );
    }

    getByOrg(orgId: number): Observable<MaterialMaster[]> {
        return this.http.get<MaterialMaster[]>(
            `${this.baseUrl}/org/${orgId}`,
            this.getAuthHeaders()
        );
    }

    save(material: Partial<MaterialMaster>): Observable<MaterialMaster> {
        return this.http.post<MaterialMaster>(
            this.baseUrl,
            material,
            this.getAuthHeaders()
        );
    }

    update(id: number, material: Partial<MaterialMaster>): Observable<MaterialMaster> {
        return this.http.put<MaterialMaster>(
            `${this.baseUrl}/${id}`,
            material,
            this.getAuthHeaders()
        );
    }

    delete(id: number): Observable<void> {
        return this.http.delete<void>(
            `${this.baseUrl}/${id}`,
            this.getAuthHeaders()
        );
    }
}
