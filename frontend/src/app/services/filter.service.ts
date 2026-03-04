import { Injectable } from '@angular/core';
import { BehaviorSubject } from 'rxjs';

@Injectable({
    providedIn: 'root'
})
export class FilterService {

    private fromDateSubject = new BehaviorSubject<string>('');
    private toDateSubject = new BehaviorSubject<string>('');
    private currentStageSubject = new BehaviorSubject<string>('');

    fromDate$ = this.fromDateSubject.asObservable();
    toDate$ = this.toDateSubject.asObservable();
    currentStage$ = this.currentStageSubject.asObservable();

    setFromDate(date: string) {
        this.fromDateSubject.next(date);
    }

    setToDate(date: string) {
        this.toDateSubject.next(date);
    }

    setCurrentStage(stage: string) {
        this.currentStageSubject.next(stage);
    }

    getFilters() {
        return {
            fromDate: this.fromDateSubject.value,
            toDate: this.toDateSubject.value,
            stage: this.currentStageSubject.value
        };
    }

    clearDates() {
        this.setFromDate('');
        this.setToDate('');
    }

    private getDefaultFromDate(): string {
        const d = new Date();
        d.setDate(d.getDate() - 30); // Default to last 30 days
        return d.toISOString().split('T')[0];
    }

    private getDefaultToDate(): string {
        return new Date().toISOString().split('T')[0];
    }
}
