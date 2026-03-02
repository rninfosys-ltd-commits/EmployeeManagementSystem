import { ComponentFixture, TestBed } from '@angular/core/testing';

import { KmListComponent } from './km-list.component';

describe('KmListComponent', () => {
  let component: KmListComponent;
  let fixture: ComponentFixture<KmListComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [KmListComponent]
    });
    fixture = TestBed.createComponent(KmListComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
