export interface Inquiry {
    inqueryId?: number;

    inqStatusId: number;
    inqueryDate: string;

    leadAccountId: number;
    projectCode: number;
    unitCode: number;

    particulars: string;
    rate: number;
    quantity: number;
    amount: number;
    total: number;

    orgId: number;
    branchId: number;
    userId: number;

    isActive: number;
}
