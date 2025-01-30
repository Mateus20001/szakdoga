import { ComponentFixture, TestBed } from '@angular/core/testing';

import { AppliedCoursesComponent } from './applied-courses.component';

describe('AppliedCoursesComponent', () => {
  let component: AppliedCoursesComponent;
  let fixture: ComponentFixture<AppliedCoursesComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [AppliedCoursesComponent]
    })
    .compileComponents();
    
    fixture = TestBed.createComponent(AppliedCoursesComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
