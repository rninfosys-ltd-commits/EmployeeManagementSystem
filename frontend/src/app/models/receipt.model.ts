export interface Receipt {
    id?: number;
    customerId?: number | string;
    partyId?: number | null;
    partyName?: string;
    // mobile?: string;
    transactionType?: number;
    paymentMode?: number;
    transactionId?: string;
    amount?: number;
    receiptImage?: string;
    receiptDate?: string;
    description?: string;
    createdBy?: number;
}
