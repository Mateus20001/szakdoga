import { ComponentFixture, TestBed } from '@angular/core/testing';

import { TeachedCoursesComponent } from './teached-courses.component';

describe('TeachedCoursesComponent', () => {
  let component: TeachedCoursesComponent;
  let fixture: ComponentFixture<TeachedCoursesComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [TeachedCoursesComponent]
    })
    .compileComponents();
    
    fixture = TestBed.createComponent(TeachedCoursesComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
