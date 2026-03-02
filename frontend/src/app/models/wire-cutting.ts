export interface WireCuttingReport {

    id?: number;

    batchNo: string;
    cuttingDate: string | Date;

    mouldNo: number;
    size: number;
    ballTestMm: number;

    time: string;
    otherReason?: string;

    // approval
    approvalStage?: string;
    approvedByL1?: string;
    approvedByL2?: string;
    approvedByL3?: string;

    // system
    userId: number;
    branchId: number;
    orgId: number;

    createdDate?: string | Date;
}
