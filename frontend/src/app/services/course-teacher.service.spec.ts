import { TestBed } from '@angular/core/testing';

import { CourseTeacherService } from './course-teacher.service';

describe('CourseTeacherService', () => {
  let service: CourseTeacherService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(CourseTeacherService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
