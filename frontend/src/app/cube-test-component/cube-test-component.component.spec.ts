import { ComponentFixture, TestBed } from '@angular/core/testing';

import { CubeTestComponentComponent } from './cube-test-component.component';

describe('CubeTestComponentComponent', () => {
  let component: CubeTestComponentComponent;
  let fixture: ComponentFixture<CubeTestComponentComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [CubeTestComponentComponent]
    });
    fixture = TestBed.createComponent(CubeTestComponentComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
