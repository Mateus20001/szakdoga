import { ComponentFixture, TestBed } from '@angular/core/testing';

import { GlobalSettingsChangerComponent } from './global-settings-changer.component';

describe('GlobalSettingsChangerComponent', () => {
  let component: GlobalSettingsChangerComponent;
  let fixture: ComponentFixture<GlobalSettingsChangerComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [GlobalSettingsChangerComponent]
    })
    .compileComponents();
    
    fixture = TestBed.createComponent(GlobalSettingsChangerComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
