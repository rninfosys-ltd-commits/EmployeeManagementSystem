import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { APP_CONFIG } from '../config/config';

@Injectable({
    providedIn: 'root'
})
export class UploadService {

    private uploadUrl = `${APP_CONFIG.BASE_URL}${APP_CONFIG.API.FILE_UPLOAD}`;

    constructor(private http: HttpClient) { }

    uploadFile(file: File) {
        const formData = new FormData();
        formData.append('file', file);

        return this.http.post(
            this.uploadUrl,
            formData,
            { responseType: 'text' }
        );
    }
}
