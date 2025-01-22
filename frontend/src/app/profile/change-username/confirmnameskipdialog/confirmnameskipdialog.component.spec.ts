import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ConfirmnameskipdialogComponent } from './confirmnameskipdialog.component';

describe('ConfirmnameskipdialogComponent', () => {
  let component: ConfirmnameskipdialogComponent;
  let fixture: ComponentFixture<ConfirmnameskipdialogComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [ConfirmnameskipdialogComponent]
    })
    .compileComponents();
    
    fixture = TestBed.createComponent(ConfirmnameskipdialogComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
