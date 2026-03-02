import { Injectable } from '@angular/core';
import { Subject } from 'rxjs';

export type ToastType = 'success' | 'error' | 'warning' | 'info';

export interface ToastMessage {
    id: number;
    message: string;
    type: ToastType;
    duration?: number;
}

export interface ConfirmOptions {
    title?: string;
    message: string;
    confirmText?: string;
    cancelText?: string;
    type?: ToastType;
}

@Injectable({
    providedIn: 'root'
})
export class NotificationService {

    private toastSubject = new Subject<ToastMessage>();
    toast$ = this.toastSubject.asObservable();

    private confirmSubject = new Subject<ConfirmOptions>();
    confirm$ = this.confirmSubject.asObservable();

    private confirmResolve: ((value: boolean) => void) | null = null;

    private counter = 0;

    success(message: string, duration = 3000) {
        this.show(message, 'success', duration);
    }

    error(message: string, duration = 4000) {
        this.show(message, 'error', duration);
    }

    warning(message: string, duration = 3500) {
        this.show(message, 'warning', duration);
    }

    info(message: string, duration = 3000) {
        this.show(message, 'info', duration);
    }

    private show(message: string, type: ToastType, duration: number) {
        this.toastSubject.next({
            id: ++this.counter,
            message,
            type,
            duration
        });
    }

    confirm(options: ConfirmOptions | string): Promise<boolean> {
        const opts: ConfirmOptions = typeof options === 'string'
            ? { message: options }
            : options;

        return new Promise<boolean>((resolve) => {
            this.confirmResolve = resolve;
            this.confirmSubject.next(opts);
        });
    }

    resolveConfirm(result: boolean) {
        if (this.confirmResolve) {
            this.confirmResolve(result);
            this.confirmResolve = null;
        }
    }
}
