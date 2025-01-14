import { ComponentFixture, TestBed } from '@angular/core/testing';

import { MajorsComponent } from './majors.component';

describe('MajorsComponent', () => {
  let component: MajorsComponent;
  let fixture: ComponentFixture<MajorsComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [MajorsComponent]
    })
    .compileComponents();
    
    fixture = TestBed.createComponent(MajorsComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
