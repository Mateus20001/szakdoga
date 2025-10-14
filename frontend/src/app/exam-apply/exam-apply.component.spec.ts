import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ExamApplyComponent } from './exam-apply.component';

describe('ExamApplyComponent', () => {
  let component: ExamApplyComponent;
  let fixture: ComponentFixture<ExamApplyComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [ExamApplyComponent]
    })
    .compileComponents();
    
    fixture = TestBed.createComponent(ExamApplyComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
