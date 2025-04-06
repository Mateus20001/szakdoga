import { ComponentFixture, TestBed } from '@angular/core/testing';

import { TimetablePlannerComponent } from './timetable-planner.component';

describe('TimetablePlannerComponent', () => {
  let component: TimetablePlannerComponent;
  let fixture: ComponentFixture<TimetablePlannerComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [TimetablePlannerComponent]
    })
    .compileComponents();
    
    fixture = TestBed.createComponent(TimetablePlannerComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
