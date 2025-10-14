import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ExamAnnouncementComponent } from './exam-announcement.component';

describe('ExamAnnouncementComponent', () => {
  let component: ExamAnnouncementComponent;
  let fixture: ComponentFixture<ExamAnnouncementComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [ExamAnnouncementComponent]
    })
    .compileComponents();
    
    fixture = TestBed.createComponent(ExamAnnouncementComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
