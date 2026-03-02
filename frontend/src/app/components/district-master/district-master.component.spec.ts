import { ComponentFixture, TestBed } from '@angular/core/testing';

import { DistrictMasterComponent } from './district-master.component';

describe('DistrictMasterComponent', () => {
  let component: DistrictMasterComponent;
  let fixture: ComponentFixture<DistrictMasterComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [DistrictMasterComponent]
    });
    fixture = TestBed.createComponent(DistrictMasterComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
