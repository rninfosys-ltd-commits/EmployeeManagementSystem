import { Component, OnInit } from '@angular/core';
import { HorizontalReportService, HorizontalReportRow } from '../services/horizontal-report.service';

interface StageConfig {
    key: keyof HorizontalReportRow;
    label: string;
    theme: string;
    columns: { key: string; label: string }[];
}

@Component({
    selector: 'app-horizontal-report',
    templateUrl: './horizontal-report.component.html',
    styleUrls: ['./horizontal-report.component.css']
})
export class HorizontalReportComponent implements OnInit {

    // ─── Filters ───────────────────────────────────────────────────────────────
    fromDate = '';
    toDate = '';
    batchNo = '';

    // ─── Data ──────────────────────────────────────────────────────────────────
    allRows: HorizontalReportRow[] = [];
    pagedRows: HorizontalReportRow[] = [];
    loading = false;
    error = '';

    // ─── Pagination ────────────────────────────────────────────────────────────
    currentPage = 1;
    pageSize = 20;
    totalPages = 1;

    // ─── Stage definitions (order + columns) ──────────────────────────────────
    stages: StageConfig[] = [
        {
            key: 'production', label: 'Production', theme: 'production',
            columns: [
                { key: 'date', label: 'Date' },
                { key: 'shift', label: 'Shift' },
                { key: 'siloNo1', label: 'Silo 1' },
                { key: 'literWeight1', label: 'Lit.Wt 1' },
                { key: 'faSolid1', label: 'FA Solid 1' },
                { key: 'siloNo2', label: 'Silo 2' },
                { key: 'literWeight2', label: 'Lit.Wt 2' },
                { key: 'faSolid2', label: 'FA Solid 2' },
                { key: 'totalSolid', label: 'Total Solid' },
                { key: 'waterLiter', label: 'Water (L)' },
                { key: 'cementKg', label: 'Cement (kg)' },
                { key: 'limeKg', label: 'Lime (kg)' },
                { key: 'gypsumKg', label: 'Gypsum (kg)' },
                { key: 'solOilKg', label: 'Sol Oil (kg)' },
                { key: 'alPowerGm', label: 'Al Pwr (gm)' },
                { key: 'tempC', label: 'Temp (°C)' },
                { key: 'productionTime', label: 'Time' },
                { key: 'remark', label: 'Remark' },
            ]
        },
        {
            key: 'casting', label: 'Casting', theme: 'casting',
            columns: [
                { key: 'date', label: 'Date' },
                { key: 'shift', label: 'Shift' },
                { key: 'size', label: 'Size' },
                { key: 'bedNo', label: 'Bed No' },
                { key: 'mouldNo', label: 'Mould No' },
                { key: 'castingTime', label: 'Time' },
                { key: 'consistency', label: 'Consistency' },
                { key: 'flowInCm', label: 'Flow (cm)' },
                { key: 'tempC', label: 'Temp (°C)' },
                { key: 'vt', label: 'V.T.' },
                { key: 'massTemp', label: 'Mass Temp' },
                { key: 'fallingTestMm', label: 'Falling (mm)' },
                { key: 'testTime', label: 'Test Time' },
                { key: 'hTime', label: 'H Time' },
                { key: 'remark', label: 'Remark' },
            ]
        },
        {
            key: 'cutting', label: 'Wire Cutting', theme: 'cutting',
            columns: [
                { key: 'date', label: 'Date' },
                { key: 'shift', label: 'Shift' },
                { key: 'cuttingDate', label: 'Cut Date' },
                { key: 'mouldNo', label: 'Mould No' },
                { key: 'size', label: 'Size' },
                { key: 'ballTestMm', label: 'Ball (mm)' },
                { key: 'otherReason', label: 'Reason' },
                { key: 'time', label: 'Time' },
            ]
        },
        {
            key: 'autoclave', label: 'Autoclave', theme: 'autoclave',
            columns: [
                { key: 'autoclaveNo', label: 'Auto No' },
                { key: 'runNo', label: 'Run No' },
                { key: 'shift', label: 'Shift' },
                { key: 'startDate', label: 'Start Date' },
                { key: 'startedAt', label: 'Started At' },
                { key: 'compDate', label: 'Comp Date' },
                { key: 'completedAt', label: 'Comp At' },
                { key: 'remarks', label: 'Remarks' },
            ]
        },
        {
            key: 'blockSeparating', label: 'Block Separating', theme: 'block',
            columns: [
                { key: 'date', label: 'Date' },
                { key: 'shift', label: 'Shift' },
                { key: 'blockSize', label: 'Block Size' },
                { key: 'time', label: 'Time' },
            ]
        },
        {
            key: 'cubeTest', label: 'Cube Test', theme: 'cube',
            columns: [
                { key: 'castDate', label: 'Cast Date' },
                { key: 'testingDate', label: 'Test Date' },
                { key: 'shift', label: 'Shift' },
                { key: 'cubeDimension', label: 'Dimension' },
                { key: 'densityKgM3', label: 'Density (kg/m³)' },
            ]
        },
        {
            key: 'rejection', label: 'Rejection', theme: 'rejection',
            columns: [
                { key: 'date', label: 'Date' },
                { key: 'shift', label: 'Shift' },
                { key: 'blockSize', label: 'Block Size' },
                { key: 'qty', label: 'Qty' },
                { key: 'totalBreakages', label: 'Breakages' },
            ]
        }
    ];

    constructor(private reportService: HorizontalReportService) { }

    ngOnInit(): void {
        this.loadReport();
    }

    loadReport(): void {
        this.loading = true;
        this.error = '';
        this.reportService.getReport(
            this.fromDate || undefined,
            this.toDate || undefined,
            this.batchNo || undefined
        ).subscribe({
            next: (data) => {
                this.allRows = data;
                this.currentPage = 1;
                this.computePagination();
                this.loading = false;
            },
            error: () => {
                this.error = 'Failed to load report. Please try again.';
                this.loading = false;
            }
        });
    }

    clearFilters(): void {
        this.fromDate = '';
        this.toDate = '';
        this.batchNo = '';
        this.loadReport();
    }

    computePagination(): void {
        this.totalPages = Math.max(1, Math.ceil(this.allRows.length / this.pageSize));
        this.updatePage();
    }

    updatePage(): void {
        const start = (this.currentPage - 1) * this.pageSize;
        this.pagedRows = this.allRows.slice(start, start + this.pageSize);
    }

    prevPage(): void {
        if (this.currentPage > 1) { this.currentPage--; this.updatePage(); }
    }

    nextPage(): void {
        if (this.currentPage < this.totalPages) { this.currentPage++; this.updatePage(); }
    }

    goToPage(p: number): void {
        this.currentPage = p;
        this.updatePage();
    }

    get pageNumbers(): number[] {
        return Array.from({ length: this.totalPages }, (_, i) => i + 1);
    }

    /** Safe field lookup from a stage data map */
    field(row: HorizontalReportRow, stageKey: keyof HorizontalReportRow, col: string): string {
        const stage = row[stageKey] as { [key: string]: string };
        return (stage && stage[col]) ? stage[col] : '—';
    }

    /** Whether a row has any data for a stage */
    hasData(row: HorizontalReportRow, stageKey: keyof HorizontalReportRow): boolean {
        const stage = row[stageKey] as { [key: string]: string };
        return stage && Object.keys(stage).length > 0;
    }

    get totalColumns(): number {
        return 1 + this.stages.reduce((sum, s) => sum + s.columns.length, 0);
    }
}
