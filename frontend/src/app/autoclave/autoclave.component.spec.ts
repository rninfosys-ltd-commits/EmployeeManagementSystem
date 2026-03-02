import { ComponentFixture, TestBed } from '@angular/core/testing';

import { AutoclaveComponent } from './autoclave.component';

describe('AutoclaveComponent', () => {
  let component: AutoclaveComponent;
  let fixture: ComponentFixture<AutoclaveComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [AutoclaveComponent]
    });
    fixture = TestBed.createComponent(AutoclaveComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
