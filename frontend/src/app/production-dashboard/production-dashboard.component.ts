import { Component, OnInit } from '@angular/core';
import { ProductionDashboardService } from '../services/productiondashboardservice';
import { WorkflowService } from '../services/workflow.service';
import { FilterService } from '../services/filter.service';
import { forkJoin } from 'rxjs';

@Component({
  selector: 'app-production-dashboard',
  templateUrl: './production-dashboard.component.html',
  styleUrls: ['./production-dashboard.component.css']
})
export class ProductionDashboardComponent implements OnInit {

  rows: any[] = [];
  filteredRows: any[] = [];
  paginatedRows: any[] = [];

  fromDate = '';
  toDate = '';
  hideCompleted = true; // ✅ Auto-hide fully completed batches

  // Pagination
  currentPage = 1;
  pageSize = 8;
  totalPages = 1;

  // Workflow status map: batchNo → { PRODUCTION: true, CASTING: false, ... }
  workflowStatusMap: { [batchNo: string]: { [stage: string]: boolean } } = {};

  constructor(
    private service: ProductionDashboardService,
    private workflowService: WorkflowService,
    private filterService: FilterService
  ) { }

  ngOnInit(): void {
    this.load();

    this.filterService.fromDate$.subscribe(d => {
      this.fromDate = d;
      this.applyFilters();
    });
    this.filterService.toDate$.subscribe(d => {
      this.toDate = d;
      this.applyFilters();
    });
  }

  load() {
    forkJoin({
      production: this.service.getProduction(),
      casting: this.service.getCasting(),
      cutting: this.service.getCutting(),
      autoclave: this.service.getAutoclave(),
      block: this.service.getBlock(),
      cube: this.service.getCube(),
      rejection: this.service.getRejection(),
      batchStatus: this.workflowService.getAllBatchStatus()
    }).subscribe(res => {

      // Build workflow status map
      res.batchStatus.forEach((b: any) => {
        this.workflowStatusMap[b.batchNo] = b.stages;
      });

      this.rows = res.production.map(p => {
        const cast = res.casting.find(c => c.batchNo == p.batchNo);
        const cut = res.cutting.find(c => c.batchNo == p.batchNo);
        const cube = res.cube.find(c => c.batchNo == p.batchNo);
        const rej = res.rejection.find(r => r.batchNo == p.batchNo);
        const block = res.block.find(b => b.batchNumber == p.batchNo);

        const auto = res.autoclave.find((a: any) =>
          a.wagons?.some((w: any) => w.eBatch == p.batchNo)
        );

        const productionTime = new Date(p.createdDate);
        const castingTime = cast ? new Date(cast.createdDate) : null;
        const cuttingTime = cut ? new Date(cut.createdDate) : null;
        const autoclaveTime = auto ? new Date(auto.startedDate) : null;
        const blockTime = block ? new Date(block.reportDate) : null;
        const cubeTime = cube ? new Date(cube.createdDate) : null;
        const rejectionTime = rej ? new Date(rej.createdDate) : null;

        // Determine completion status
        const stages = this.workflowStatusMap[p.batchNo] || {};
        const completedCount = Object.values(stages).filter(v => v).length;
        const isFullyCompleted = completedCount === 7;

        return {
          batchNo: p.batchNo,
          productionTimeObj: productionTime,
          castingTimeObj: castingTime,
          cuttingTimeObj: cuttingTime,
          autoclaveTimeObj: autoclaveTime,
          blockTimeObj: blockTime,
          cubeTimeObj: cubeTime,
          rejectionTimeObj: rejectionTime,

          prodToCastingDiff: this.getMinutesDiff(productionTime, castingTime),
          castingToCuttingDiff: this.getMinutesDiff(castingTime, cuttingTime),
          cuttingToAutoclaveDiff: this.getMinutesDiff(cuttingTime, autoclaveTime),
          autoclaveToBlockDiff: this.getMinutesDiff(autoclaveTime, blockTime),
          blockToCubeDiff: this.getMinutesDiff(blockTime, cubeTime),
          cubeToRejectionDiff: this.getMinutesDiff(cubeTime, rejectionTime),

          stages: stages,
          completedCount: completedCount,
          isFullyCompleted: isFullyCompleted
        };
      });

      this.applyFilters();
    });
  }

  getMinutesDiff(t1: Date | null, t2: Date | null): number | null {
    if (!t1 || !t2) return null;
    return Math.floor((t2.getTime() - t1.getTime()) / 60000);
  }

  setupPagination() {
    this.totalPages = Math.max(1, Math.ceil(this.filteredRows.length / this.pageSize));
    this.currentPage = 1;
    this.updatePaginatedRows();
  }

  updatePaginatedRows() {
    const start = (this.currentPage - 1) * this.pageSize;
    const end = start + this.pageSize;
    this.paginatedRows = this.filteredRows.slice(start, end);
  }

  formatMinutes(min: number): string {
    const h = Math.floor(min / 60);
    const m = min % 60;
    return h + 'h ' + m + 'm';
  }

  nextPage() {
    if (this.currentPage < this.totalPages) {
      this.currentPage++;
      this.updatePaginatedRows();
    }
  }

  prevPage() {
    if (this.currentPage > 1) {
      this.currentPage--;
      this.updatePaginatedRows();
    }
  }

  applyFilters() {
    // Date validation
    if (this.fromDate && this.toDate) {
      const fromCheck = new Date(this.fromDate).getTime();
      const toCheck = new Date(this.toDate).getTime();
      if (fromCheck > toCheck) {
        alert('From Date cannot be greater than To Date');
        this.fromDate = '';
        return;
      }
    }

    const from = this.fromDate ? new Date(this.fromDate).setHours(0, 0, 0, 0) : null;
    const to = this.toDate ? new Date(this.toDate).setHours(23, 59, 59, 999) : null;

    this.filteredRows = this.rows.filter(r => {
      if (!r.productionTimeObj) return false;

      // ✅ Auto-hide fully completed batches
      if (this.hideCompleted && r.isFullyCompleted) return false;

      const time = new Date(r.productionTimeObj).getTime();
      return (!from || time >= from) && (!to || time <= to);
    });

    // ✅ DESCENDING sort (latest first)
    this.filteredRows.sort((a, b) =>
      new Date(b.productionTimeObj).getTime() -
      new Date(a.productionTimeObj).getTime()
    );

    this.setupPagination();
  }

  onDateChange() {
    this.filterService.setFromDate(this.fromDate);
    this.filterService.setToDate(this.toDate);
  }

  clear() {
    this.fromDate = '';
    this.toDate = '';
    this.onDateChange();
  }

  // ✅ Get stage icon for a batch
  getStageIcon(row: any, stageName: string): string {
    const stages = row.stages || {};
    if (stages[stageName]) return '✅';

    // Check if this is the current in-progress stage
    const stageOrder = ['PRODUCTION', 'CASTING', 'CUTTING', 'AUTOCLAVE', 'BLOCK_SEPARATING', 'CUBE_TEST', 'REJECTION'];
    const idx = stageOrder.indexOf(stageName);
    if (idx > 0 && stages[stageOrder[idx - 1]]) return '🟡';

    return '⚪';
  }

  // ✅ Get progress percentage
  getProgress(row: any): number {
    return Math.round((row.completedCount / 7) * 100);
  }

  // ✅ Download combined PDF or Excel report
  downloadReport(batchNo: string, upToStage: string, format: string = 'pdf') {
    this.workflowService.downloadReport(batchNo, upToStage, format).subscribe(blob => {
      this.saveBlob(blob, `workflow_report_${batchNo}.${format === 'excel' ? 'xlsx' : 'pdf'}`);
    });
  }

  // ✅ Export ALL workflow for all matching batches
  exportAllWorkflow(format: string = 'excel') {
    if (!this.fromDate || !this.toDate) {
      alert('Please select both From and To dates to export all data');
      return;
    }

    this.workflowService.exportReport('CONSOLIDATED', this.fromDate, this.toDate, format).subscribe(blob => {
      this.saveBlob(blob, `consolidated_workflow_report.${format === 'excel' ? 'xlsx' : 'pdf'}`);
    });
  }

  private saveBlob(blob: Blob, filename: string) {
    const url = URL.createObjectURL(blob);
    const a = document.createElement('a');
    a.href = url;
    a.download = filename;
    a.click();
    URL.revokeObjectURL(url);
  }

  toggleHideCompleted() {
    this.hideCompleted = !this.hideCompleted;
    this.applyFilters();
  }
}
