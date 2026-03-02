import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { APP_CONFIG } from '../config/config';

/*
|--------------------------------------------------------------------------
| BASE API URL
|--------------------------------------------------------------------------
| Backend Controller:
| @RequestMapping("/api/inquiry-schedule")
*/
const INQUIRY_SCHEDULE_API =
    `${APP_CONFIG.BASE_URL}${APP_CONFIG.API.INQUIRY_SCHEDULE}`;

@Injectable({
    providedIn: 'root'
})
export class InquiryScheduleService {

    constructor(private http: HttpClient) { }

    // =====================================================
    // =============== INQUIRY SCHEDULE CRUD ===============
    // =====================================================

    /**
     * Get all inquiry schedules
     * GET /api/inquiry-schedule
     */
    getAllSchedules(): Observable<any[]> {
        return this.http.get<any[]>(INQUIRY_SCHEDULE_API);
    }

    /**
     * Get inquiry schedule by id
     * GET /api/inquiry-schedule/{id}
     */
    getScheduleById(id: number): Observable<any> {
        return this.http.get<any>(`${INQUIRY_SCHEDULE_API}/${id}`);
    }

    /**
     * Create inquiry schedule
     * POST /api/inquiry-schedule
     */
    createSchedule(payload: any): Observable<any> {
        return this.http.post<any>(INQUIRY_SCHEDULE_API, payload);
    }

    /**
     * Update inquiry schedule
     * PUT /api/inquiry-schedule/{id}
     */
    updateSchedule(id: number, payload: any): Observable<any> {
        return this.http.put<any>(`${INQUIRY_SCHEDULE_API}/${id}`, payload);
    }

    /**
     * Delete inquiry schedule
     * DELETE /api/inquiry-schedule/{id}
     */
    deleteSchedule(id: number): Observable<void> {
        return this.http.delete<void>(`${INQUIRY_SCHEDULE_API}/${id}`);
    }
}
