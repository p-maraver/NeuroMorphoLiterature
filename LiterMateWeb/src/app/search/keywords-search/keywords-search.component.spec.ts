import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { KeywordsSearchComponent } from './keywords-search.component';

describe('KeywordsSearchComponent', () => {
  let component: KeywordsSearchComponent;
  let fixture: ComponentFixture<KeywordsSearchComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ KeywordsSearchComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(KeywordsSearchComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
