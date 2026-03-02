import { Component, OnInit, OnDestroy } from '@angular/core';
import { Subscription } from 'rxjs';
import { NotificationService, ToastMessage, ConfirmOptions } from '../../services/notification.service';

@Component({
    selector: 'app-notification',
    templateUrl: './notification.component.html',
    styleUrls: ['./notification.component.css']
})
export class NotificationComponent implements OnInit, OnDestroy {

    toasts: ToastMessage[] = [];
    confirmVisible = false;
    confirmOptions: ConfirmOptions = { message: '' };

    private toastSub!: Subscription;
    private confirmSub!: Subscription;

    constructor(private notificationService: NotificationService) { }

    ngOnInit(): void {
        this.toastSub = this.notificationService.toast$.subscribe(toast => {
            this.toasts.push(toast);

            setTimeout(() => {
                this.dismissToast(toast.id);
            }, toast.duration || 3000);
        });

        this.confirmSub = this.notificationService.confirm$.subscribe(options => {
            this.confirmOptions = options;
            this.confirmVisible = true;
        });
    }

    ngOnDestroy(): void {
        this.toastSub?.unsubscribe();
        this.confirmSub?.unsubscribe();
    }

    dismissToast(id: number) {
        this.toasts = this.toasts.filter(t => t.id !== id);
    }

    onConfirm() {
        this.confirmVisible = false;
        this.notificationService.resolveConfirm(true);
    }

    onCancel() {
        this.confirmVisible = false;
        this.notificationService.resolveConfirm(false);
    }

    getIcon(type: string): string {
        switch (type) {
            case 'success': return '✓';
            case 'error': return '✕';
            case 'warning': return '⚠';
            case 'info': return 'ℹ';
            default: return 'ℹ';
        }
    }
}
