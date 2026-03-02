import { ComponentFixture, TestBed } from '@angular/core/testing';

import { BlockSeparatingComponent } from './block-separating.component';

describe('BlockSeparatingComponent', () => {
  let component: BlockSeparatingComponent;
  let fixture: ComponentFixture<BlockSeparatingComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [BlockSeparatingComponent]
    });
    fixture = TestBed.createComponent(BlockSeparatingComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
