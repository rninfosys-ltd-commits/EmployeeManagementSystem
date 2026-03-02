import { ComponentFixture, TestBed } from '@angular/core/testing';

import { TalukaMasterComponent } from './taluka-master.component';

describe('TalukaMasterComponent', () => {
  let component: TalukaMasterComponent;
  let fixture: ComponentFixture<TalukaMasterComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [TalukaMasterComponent]
    });
    fixture = TestBed.createComponent(TalukaMasterComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
