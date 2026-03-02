import { Component, OnInit } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { CustomerService } from '../../services/customer.service';
import { CustomerTrn } from '../../models/customer.model';
import { APP_CONFIG } from '../../config/config';
import { saveAs } from 'file-saver';

@Component({
    selector: 'app-transaction-export',
    templateUrl: './transaction-export.component.html',
    styleUrls: ['./transaction-export.component.css']
})
export class TransactionExportComponent implements OnInit {

    type: string = 'SALES';
    fromDate: string = '';
    toDate: string = '';
    partyName: string = '';
    minAmount: number | null = null;
    maxAmount: number | null = null;

    customers: any[] = [];
    filteredCustomers: any[] = [];
    showPartyDropdown: boolean = false;
    showFormats: boolean = false;
    loading: boolean = false;

    constructor(
        private http: HttpClient,
        private customerService: CustomerService
    ) { }

    ngOnInit(): void {
        // Set default dates to current month
        const now = new Date();
        const firstDay = new Date(now.getFullYear(), now.getMonth(), 1);
        this.fromDate = firstDay.toISOString().split('T')[0];
        this.toDate = now.toISOString().split('T')[0];

        this.loadCustomers();
    }

    loadCustomers() {
        const url = `${APP_CONFIG.BASE_URL}/api/transactions/parties`;
        this.http.get<any[]>(url).subscribe({
            next: (data) => {
                this.customers = data;
                this.filteredCustomers = data;
            },
            error: (err) => console.error('Error loading customers', err)
        });
    }

    filterCustomers() {
        if (!this.partyName) {
            this.filteredCustomers = this.customers;
        } else {
            this.filteredCustomers = this.customers.filter(c =>
                (c.custName && c.custName.toLowerCase().includes(this.partyName.toLowerCase())) ||
                (c.ownerName && c.ownerName.toLowerCase().includes(this.partyName.toLowerCase()))
            );
        }
    }

    selectParty(customer: any) {
        this.partyName = customer.custName || '';
        this.showPartyDropdown = false;
    }

    export(format: string) {
        if (!this.fromDate || !this.toDate) {
            alert('Please select date range');
            return;
        }

        this.loading = true;
        let params = new HttpParams()
            .set('type', this.type)
            .set('fromDate', this.fromDate)
            .set('toDate', this.toDate)
            .set('format', format);

        if (this.partyName) params = params.set('partyName', this.partyName);
        if (this.minAmount !== null) params = params.set('minAmount', this.minAmount.toString());
        if (this.maxAmount !== null) params = params.set('maxAmount', this.maxAmount.toString());

        const url = `${APP_CONFIG.BASE_URL}/api/transactions/export`;

        this.http.get(url, { params, responseType: 'blob' }).subscribe({
            next: (blob) => {
                const fileName = `${this.type.toLowerCase()}_transactions_${format}.${format === 'excel' ? 'xlsx' : 'pdf'}`;
                saveAs(blob, fileName);
                this.loading = false;
            },
            error: (err) => {
                console.error('Export failed', err);
                alert('Export failed. Please check backend logs.');
                this.loading = false;
            }
        });
    }
}
