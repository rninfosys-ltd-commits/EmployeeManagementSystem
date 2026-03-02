import { ComponentFixture, TestBed } from '@angular/core/testing';

import { RootMasterComponent } from './root-master.component';

describe('RootMasterComponent', () => {
  let component: RootMasterComponent;
  let fixture: ComponentFixture<RootMasterComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [RootMasterComponent]
    });
    fixture = TestBed.createComponent(RootMasterComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
