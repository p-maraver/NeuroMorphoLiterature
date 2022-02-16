import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { SupplementaryMaterialComponent } from './supplementary-material.component';

describe('SupplementaryMaterialComponent', () => {
  let component: SupplementaryMaterialComponent;
  let fixture: ComponentFixture<SupplementaryMaterialComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ SupplementaryMaterialComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(SupplementaryMaterialComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
