import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { BulkStatusComponent } from './bulk-status.component';

describe('BulkStatusComponent', () => {
  let component: BulkStatusComponent;
  let fixture: ComponentFixture<BulkStatusComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ BulkStatusComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(BulkStatusComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
