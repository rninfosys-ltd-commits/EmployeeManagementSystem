import { Injectable } from '@angular/core';
import {
  HttpRequest,
  HttpHandler,
  HttpEvent,
  HttpInterceptor,
  HttpErrorResponse
} from '@angular/common/http';
import { Observable, throwError } from 'rxjs';
import { catchError } from 'rxjs/operators';
import { TokenStorageService } from '../services/token-storage.service';

@Injectable()
export class AuthInterceptor implements HttpInterceptor {

  constructor(private tokenStorage: TokenStorageService) { }

  intercept(request: HttpRequest<any>, next: HttpHandler): Observable<HttpEvent<any>> {

    const token = this.tokenStorage.getToken();

    if (token) {
      request = request.clone({
        setHeaders: {
          Authorization: `Bearer ${token}`
        }
      });
    }

    return next.handle(request).pipe(
      catchError((error: HttpErrorResponse) => {

        // Backend returns 403 -> NO_AUTH
        if (error.status === 403 && error.error?.message === "NO_AUTH") {
          // alert("❌ You are not authorized to perform this action.");
        }

        // Missing or invalid token → 401
        if (error.status === 401) {
          // alert("❌ You are not authorized to perform this action.");
        }

        return throwError(() => error);
      })
    );
  }
}
