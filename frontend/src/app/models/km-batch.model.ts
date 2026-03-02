export interface KmBatch {
    kmBatchNo?: number;
    trndate?: string;
    createdby?: string;
    approvalStage?: string; // NONE, L1, L2, L3, L1_REJECTED, L2_REJECTED, L3_REJECTED
    aproval1?: string;
    aproval2?: string;
    aproval3?: string;
    aproval4?: string;
    aproval1Name?: string;
    aproval2Name?: string;
    aproval3Name?: string;
    totalEntries?: number;
}
