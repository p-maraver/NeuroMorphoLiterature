import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { SentEmailsComponent } from './sent-emails.component';

describe('SentEmailsComponent', () => {
  let component: SentEmailsComponent;
  let fixture: ComponentFixture<SentEmailsComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ SentEmailsComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(SentEmailsComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
