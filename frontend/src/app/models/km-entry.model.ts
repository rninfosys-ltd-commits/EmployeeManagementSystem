export interface KmEntry {
    id?: number;
    kmBatchNo?: number;
    salesperson: string;
    startKm: number | null;
    endKm: number | null;
    visitedPlace: string;
    trnDate: string;
    filePath?: string;
    status?: string; // PENDING / APPROVED etc (optional)
}
