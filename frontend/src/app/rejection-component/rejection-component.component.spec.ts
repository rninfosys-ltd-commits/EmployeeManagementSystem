import { ComponentFixture, TestBed } from '@angular/core/testing';

import { RejectionComponentComponent } from './rejection-component.component';

describe('RejectionComponentComponent', () => {
  let component: RejectionComponentComponent;
  let fixture: ComponentFixture<RejectionComponentComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [RejectionComponentComponent]
    });
    fixture = TestBed.createComponent(RejectionComponentComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
