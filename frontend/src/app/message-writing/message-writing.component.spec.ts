import { ComponentFixture, TestBed } from '@angular/core/testing';

import { MessageWritingComponent } from './message-writing.component';

describe('MessageWritingComponent', () => {
  let component: MessageWritingComponent;
  let fixture: ComponentFixture<MessageWritingComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [MessageWritingComponent]
    })
    .compileComponents();
    
    fixture = TestBed.createComponent(MessageWritingComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
