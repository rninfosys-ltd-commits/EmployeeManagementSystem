export interface Lead {
    leadId?: number;

    date?: string;

    cName: string;
    contactNo: number;
    panNo?: string;
    gstNo?: string;
    email?: string;
    website?: string;
    phone?: number;
    fax?: number;
    invoiceAddress?: string;

    income?: number;
    incomeSource?: string;
    otherIncome?: number;
    otherIncomeSource?: string;
    budget: number;
    notes?: string;
    area?: string;

    stateId: number;
    distId?: number;
    cityId: number;

    userId?: number;
    branchId?: number;
    orgId?: number;

    createdDate?: string;
    updatedBy?: number;
    updatedDate?: string;

    isActive: number;
}
