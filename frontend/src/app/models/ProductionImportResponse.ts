export interface ProductionImportResult {
    batchNo: string;
    status: 'SUCCESS' | 'FAILED';
    error?: string;
}

export interface ProductionImportResponse {
    savedCount: number;
    errorCount: number;
    results: ProductionImportResult[];
}
