import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { MetadataValuesComponent } from './metadata-values.component';

describe('MetadataValuesComponent', () => {
  let component: MetadataValuesComponent;
  let fixture: ComponentFixture<MetadataValuesComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ MetadataValuesComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(MetadataValuesComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
