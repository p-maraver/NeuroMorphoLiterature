import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { ReconstructionsComponent } from './reconstructions.component';

describe('ReconstructionsComponent', () => {
  let component: ReconstructionsComponent;
  let fixture: ComponentFixture<ReconstructionsComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ ReconstructionsComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(ReconstructionsComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
