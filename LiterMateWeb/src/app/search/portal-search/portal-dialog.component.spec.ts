import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { PortalDialogComponent } from './portal-dialog.component';

describe('PortalDialogComponent', () => {
  let component: PortalDialogComponent;
  let fixture: ComponentFixture<PortalDialogComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ PortalDialogComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(PortalDialogComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
