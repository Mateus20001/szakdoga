import { ComponentFixture, TestBed } from '@angular/core/testing';

import { GradingStudentsComponent } from './grading-students.component';

describe('GradingStudentsComponent', () => {
  let component: GradingStudentsComponent;
  let fixture: ComponentFixture<GradingStudentsComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [GradingStudentsComponent]
    })
    .compileComponents();
    
    fixture = TestBed.createComponent(GradingStudentsComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
