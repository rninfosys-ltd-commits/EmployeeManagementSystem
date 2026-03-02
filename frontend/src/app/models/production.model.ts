export interface Production {

    id: number;
    batchNo: string;

    // ✅ main stage times
    productionTime?: string | null;
    castingTime?: string | null;
    cuttingTime?: string | null;
    autoclaveTime?: string | null;
    blockSeparatingTime?: string | null;
    cubeTestTime?: string | null;
    rejectionTime?: string | null;

    // ✅ approvals (keep if used somewhere else)
    approvalTimeL1?: string | null;
    approvalTimeL2?: string | null;
    approvalTimeL3?: string | null;
    approvalTimeL4?: string | null;
    approvalTimeL5?: string | null;
    approvalTimeL6?: string | null;
    approvalTimeL7?: string | null;

    // ✅ for date filter
    createdDate?: string | null;

}