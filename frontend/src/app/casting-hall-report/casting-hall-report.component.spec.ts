import { ComponentFixture, TestBed } from '@angular/core/testing';

import { CastingHallReportComponent } from './casting-hall-report.component';

describe('CastingHallReportComponent', () => {
  let component: CastingHallReportComponent;
  let fixture: ComponentFixture<CastingHallReportComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [CastingHallReportComponent]
    });
    fixture = TestBed.createComponent(CastingHallReportComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
