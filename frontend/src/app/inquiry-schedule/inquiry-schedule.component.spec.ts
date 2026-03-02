import { ComponentFixture, TestBed } from '@angular/core/testing';

import { InquiryScheduleComponent } from './inquiry-schedule.component';

describe('InquiryScheduleComponent', () => {
  let component: InquiryScheduleComponent;
  let fixture: ComponentFixture<InquiryScheduleComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [InquiryScheduleComponent]
    });
    fixture = TestBed.createComponent(InquiryScheduleComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
