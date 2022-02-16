import { TestBed } from '@angular/core/testing';

import { FullTextService } from './full-text.service';

describe('FullTextService', () => {
  beforeEach(() => TestBed.configureTestingModule({}));

  it('should be created', () => {
    const service: FullTextService = TestBed.get(FullTextService);
    expect(service).toBeTruthy();
  });
});
