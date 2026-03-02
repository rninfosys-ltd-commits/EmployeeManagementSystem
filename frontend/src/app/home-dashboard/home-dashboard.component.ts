import { Component, OnInit, AfterViewInit } from '@angular/core';
import { HttpClient } from '@angular/common/http';

import jsPDF from 'jspdf';
import autoTable from 'jspdf-autotable';
import * as XLSX from 'xlsx';

import { Chart, registerables } from 'chart.js';
import { APP_CONFIG } from '../config/config';
Chart.register(...registerables);

@Component({
  selector: 'app-home-dashboard',
  templateUrl: './home-dashboard.component.html',
  styleUrls: ['./home-dashboard.component.css']
})
export class HomeDashboardComponent implements OnInit, AfterViewInit {

  inquiries: any[] = [];
  filtered: any[] = [];

  fromDate = '';
  toDate = '';
  status = '';
  showStatus = false;
  showExport = false;

  summary: any = {
    OPEN: { count: 0, amount: 0 },
    IN_PROGRESS: { count: 0, amount: 0 },
    CLOSED: { count: 0, amount: 0 },
    SUCCESS: { count: 0, amount: 0 },
    CANCELLED: { count: 0, amount: 0 }
  };

  grandTotalAmount = 0;

  chartType: 'line' | 'bar' | 'pie' = 'line';
  showChartType = false;

  currentMonthChart!: Chart;
  lastMonthChart!: Chart;

  // 🔥 FLAG
  private viewReady = false;

  constructor(private http: HttpClient) { }

  // ================= INIT =================
  ngOnInit(): void {
    this.loadInquiries();
  }
  toggleStatus() {
    this.showStatus = !this.showStatus;
    this.showExport = false;
  }

  toggleExport() {
    this.showExport = !this.showExport;
    this.showStatus = false;
  }

  selectStatus(value: string) {
    this.status = value;
    this.showStatus = false;
    this.applyFilters();
  }

  toggleChartType() {
    this.showChartType = !this.showChartType;
    this.showStatus = false;
    this.showExport = false;
  }

  selectChartType(type: any) {
    this.chartType = type;
    this.showChartType = false;
    this.buildCurrentMonthChart();
    this.buildLastMonthChart();
  }

  ngAfterViewInit(): void {
    this.viewReady = true;

    if (this.inquiries.length) {
      this.buildCurrentMonthChart();
      this.buildLastMonthChart();
    }
  }

  // ================= LOAD DATA =================
  loadInquiries(): void {
    this.http.get<any[]>(
      APP_CONFIG.BASE_URL + APP_CONFIG.API.INQUIRIES
    )
      .subscribe(res => {
        this.inquiries = res || [];
        this.applyFilters();

        if (this.viewReady) {
          this.buildCurrentMonthChart();
          this.buildLastMonthChart();
        }
      });
  }

  // ================= STATUS =================
  getStatusName(id: number): string {
    switch (id) {
      case 1: return 'OPEN';
      case 2: return 'IN_PROGRESS';
      case 3: return 'CLOSED';
      case 4: return 'SUCCESS';
      case 5: return 'CANCELLED';
      default: return '';
    }
  }

  // ================= FILTER =================
  applyFilters(): void {
    this.filtered = this.inquiries.filter(i => {
      let ok = true;

      if (this.fromDate) {
        ok = ok && new Date(i.inqueryDate) >= new Date(this.fromDate);
      }

      if (this.toDate) {
        ok = ok && new Date(i.inqueryDate) <= new Date(this.toDate);
      }

      if (this.status) {
        ok = ok && this.getStatusName(i.inqStatusId) === this.status;
      }

      return ok;
    });

    this.calculateSummary();
  }

  // ================= SUMMARY =================
  calculateSummary(): void {
    this.grandTotalAmount = 0;

    Object.keys(this.summary).forEach(k => {
      this.summary[k].count = 0;
      this.summary[k].amount = 0;
    });

    this.filtered.forEach(i => {
      const statusKey = this.getStatusName(i.inqStatusId);
      const amt = Number(i.total || 0);

      if (this.summary[statusKey]) {
        this.summary[statusKey].count++;
        this.summary[statusKey].amount += amt;
      }

      this.grandTotalAmount += amt;
    });
  }

  clear(): void {
    this.fromDate = '';
    this.toDate = '';
    this.status = '';
    this.applyFilters();
  }

  // ================= EXPORT =================
  exportData(type: string): void {

    if (!type || !this.filtered.length) return;

    const data = this.filtered.map((i, idx) => ({
      '#': idx + 1,
      'Inquiry ID': i.inqueryId,
      Status: this.getStatusName(i.inqStatusId),
      Date: i.inqueryDate,
      Amount: i.total
    }));

    if (type === 'pdf') {
      const doc = new jsPDF();
      autoTable(doc, {
        head: [Object.keys(data[0])],
        body: data.map(d => Object.values(d))
      });
      doc.save('dashboard.pdf');
    }

    if (type === 'excel') {
      const ws = XLSX.utils.json_to_sheet(data);
      const wb = XLSX.utils.book_new();
      XLSX.utils.book_append_sheet(wb, ws, 'Dashboard');
      XLSX.writeFile(wb, 'dashboard.xlsx');
    }
  }

  // ================= CHART HELPERS =================
  private buildChart(canvasId: string, monthOffset: number, chartRef?: Chart): Chart {

    const canvas = document.getElementById(canvasId) as HTMLCanvasElement;
    if (!canvas) return chartRef!;

    const monthlyData = new Array(31).fill(0);
    const now = new Date();

    let month = now.getMonth() - monthOffset;
    let year = now.getFullYear();

    if (month < 0) {
      month = 11;
      year--;
    }

    this.inquiries.forEach(i => {
      const d = new Date(i.inqueryDate);
      if (d.getMonth() === month && d.getFullYear() === year) {
        monthlyData[d.getDate() - 1] += Number(i.total || 0);
      }
    });

    const labels = Array.from({ length: 31 }, (_, i) => (i + 1).toString());

    if (chartRef) {
      chartRef.destroy();
    }

    const type = this.chartType;
    const isPie = type === 'pie';

    // Different colors for Pie/Bar
    const bgColors = isPie
      ? [
        '#6366f1', '#a855f7', '#ec4899', '#f43f5e', '#ef4444',
        '#f59e0b', '#10b981', '#06b6d4', '#3b82f6', '#1d4ed8'
      ]
      : (type === 'bar' ? '#2563eb' : 'rgba(37,99,235,0.15)');

    return new Chart(canvas, {
      type: type,
      data: {
        labels,
        datasets: [{
          data: monthlyData,
          borderColor: type === 'line' ? '#2563eb' : 'transparent',
          backgroundColor: bgColors,
          fill: type === 'line',
          tension: 0.4,
          pointRadius: type === 'line' ? 3 : 0,
          pointHoverRadius: 6
        }]
      },
      options: {
        responsive: true,
        maintainAspectRatio: false,

        layout: {
          padding: {
            top: 20,
            bottom: 30
          }
        },

        plugins: {
          legend: { display: false },
          tooltip: {
            callbacks: {
              label: (ctx) => {
                const value = ctx.parsed.y ?? 0;
                return `₹ ${value.toLocaleString()}`;
              }
            }
          }
        },

        scales: isPie ? {} : {
          x: {
            ticks: {
              padding: 20
            }
          },
          y: {
            beginAtZero: true,

            // 🔥 THIS IS THE KEY LINE
            grace: '25%',   // ⬅ adds space above data

            ticks: {
              padding: 12,
              callback: (value) => '₹ ' + Number(value).toLocaleString()
            }
          }
        }
      }

    });
  }

  // ================= CURRENT MONTH =================
  buildCurrentMonthChart(): void {
    this.currentMonthChart = this.buildChart(
      'currentMonthChart',
      0,
      this.currentMonthChart
    );
  }

  // ================= LAST MONTH =================
  buildLastMonthChart(): void {
    this.lastMonthChart = this.buildChart(
      'lastMonthChart',
      1,
      this.lastMonthChart
    );
  }
}
