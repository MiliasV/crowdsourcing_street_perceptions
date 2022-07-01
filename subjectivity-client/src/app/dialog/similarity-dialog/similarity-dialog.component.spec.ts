import { ComponentFixture, TestBed } from '@angular/core/testing';

import { SimilarityDialogComponent } from './similarity-dialog.component';

describe('SimilarityDialogComponent', () => {
  let component: SimilarityDialogComponent;
  let fixture: ComponentFixture<SimilarityDialogComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ SimilarityDialogComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(SimilarityDialogComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
