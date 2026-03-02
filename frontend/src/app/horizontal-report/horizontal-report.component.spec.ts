import { ComponentFixture, TestBed } from '@angular/core/testing';

import { HorizontalReportComponent } from './horizontal-report.component';

describe('HorizontalReportComponent', () => {
    let component: HorizontalReportComponent;
    let fixture: ComponentFixture<HorizontalReportComponent>;

    beforeEach(async () => {
        await TestBed.configureTestingModule({
            declarations: [HorizontalReportComponent]
        })
            .compileComponents();

        fixture = TestBed.createComponent(HorizontalReportComponent);
        component = fixture.componentInstance;
        fixture.detectChanges();
    });

    it('should create', () => {
        expect(component).toBeTruthy();
    });
});
