import { ComponentFixture, TestBed } from '@angular/core/testing';

import { WireCuttingReportComponent } from './wire-cutting-report.component';

describe('WireCuttingReportComponent', () => {
  let component: WireCuttingReportComponent;
  let fixture: ComponentFixture<WireCuttingReportComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [WireCuttingReportComponent]
    });
    fixture = TestBed.createComponent(WireCuttingReportComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
