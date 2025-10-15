import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ExamAppliedComponent } from './exam-applied.component';

describe('ExamAppliedComponent', () => {
  let component: ExamAppliedComponent;
  let fixture: ComponentFixture<ExamAppliedComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [ExamAppliedComponent]
    })
    .compileComponents();
    
    fixture = TestBed.createComponent(ExamAppliedComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
