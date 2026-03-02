export interface CastingImportResult {
    batchNo: string;
    status: 'SUCCESS' | 'FAILED';
    error?: string;
}

export interface CastingImportResponse {
    savedCount: number;
    errorCount: number;
    results: CastingImportResult[];
}
