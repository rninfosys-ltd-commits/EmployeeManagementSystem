import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ProductionEntryComponent } from './production-entry.component';

describe('ProductionEntryComponent', () => {
  let component: ProductionEntryComponent;
  let fixture: ComponentFixture<ProductionEntryComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [ProductionEntryComponent]
    });
    fixture = TestBed.createComponent(ProductionEntryComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
